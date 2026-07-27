package com.aeromail.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.aeromail.exception.EmailNotFoundException;
import com.aeromail.model.dto.DraftEmailRequest;
import com.aeromail.model.dto.DraftEmailResponse;
import com.aeromail.model.dto.DraftUpdateRequest;
import com.aeromail.model.dto.EmailRequest;
import com.aeromail.model.dto.InboxEmailResponse;
import com.aeromail.model.dto.SentEmailResponse;
import com.aeromail.model.entity.Email;
import com.aeromail.model.enums.EmailStatus;
import com.aeromail.repository.EmailRepository;
import com.aeromail.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	
	
	
	private final EmailRepository emailRepo;
	private final JavaMailSender mailSender;

	//Constructor Dependency Injection here.
    public EmailServiceImpl(EmailRepository emailRepo , JavaMailSender mailSender) {
        this.emailRepo = emailRepo;
        this.mailSender = mailSender;
    }
	
    
    //Method for test
	@Override
	public String getAllEmails() {
		return "Hello there";
	}
	
	
	
	//Method which will save the mail in DB and also send it with the help of javaMailing service.
	@Override
	public void saveEmail(EmailRequest request) {
		Email email = new Email();
        email.setFromEmail(request.getFromEmail());
        email.setToEmail(request.getToEmail());
        email.setSubject(request.getSubject());
        email.setMessage(request.getMessage());
        email.setStatus(EmailStatus.PENDING);
        email.setSentAt(null);
        email.setCreatedAt(LocalDateTime.now());
        email.setUpdatedAt(LocalDateTime.now());


        //Saving the email in DB
        emailRepo.save(email);
        //Sending the mail Asynchronously (Non Blocking mechanism).  
        sendEmailAsync(email.getId());
	}
	
	//this method is made to seprate the smtp call and make it asynchronous.
	@Async
	public void sendEmailAsync(long emailId) {
		
		 Email email = emailRepo.findById(emailId)
		            .orElseThrow(() -> new RuntimeException("Email not found"));

	    try {
	        SimpleMailMessage mail = new SimpleMailMessage();
	        mail.setFrom(email.getFromEmail());
	        mail.setTo(email.getToEmail());
	        mail.setSubject(email.getSubject());
	        mail.setText(email.getMessage());

	    
	        mailSender.send(mail);

	        email.setStatus(EmailStatus.SENT);
	        email.setSentAt(LocalDateTime.now());

	    } catch (Exception ex) {
	        email.setStatus(EmailStatus.FAILED);
	    }
	    
	    emailRepo.save(email);
	}



	//To GET Inbox Mail Api
	@Override
	public Page<InboxEmailResponse> getInBoxMails(String toEmail,int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Email> em = emailRepo.findByToEmailOrderBySentAtDesc(toEmail,pageable);
		
		return em.map(this::EmailEntityToInboxEmailResponse);
				
	}

	//To GET Sent Mail api
	@Override
	public Page<SentEmailResponse> getSentBoxMails(String from, int page , int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Email> em =  emailRepo.findByFromEmailOrderBySentAtDesc(from,pageable);
		return  em.map(this::EmailEntityToSentEmailResponse);
	}
	

	//Draft Logics
	
	public void saveDraft(DraftEmailRequest request) {

	    Email email = new Email();

	    email.setFromEmail(request.getFromEmail());
	    email.setToEmail(request.getToEmail());
	    email.setSubject(request.getSubject());
	    email.setMessage(request.getMessage());

	    email.setStatus(EmailStatus.DRAFT);
	    email.setArchived(false);
	    email.setDeleted(false);
	    email.setSentAt(null);
	    email.setUpdatedAt(LocalDateTime.now());

	    email.setCreatedAt(LocalDateTime.now());
	    email.setUpdatedAt(LocalDateTime.now());

	    
	    emailRepo.save(email);
	}

	//here i am updating the draft which is saved in backend and sending it
	public void updateDraft(Long id, DraftUpdateRequest request) {

	    Email email = emailRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Draft not found"));

	    // Validate state
	    if (email.getStatus() != EmailStatus.DRAFT) {
	        throw new IllegalStateException("Only drafts can be updated");
	    }

	    if (email.isDeleted()) {
	        throw new IllegalStateException("Cannot update deleted draft");
	    }

	    // Update fields
	    email.setToEmail(request.getToEmail());
	    email.setSubject(request.getSubject());
	    email.setMessage(request.getMessage());
	    email.setUpdatedAt(LocalDateTime.now());

	    emailRepo.save(email);
	}

	
	// here i am sending a draft after making checks
	public void sendDraft(Long id) {

	    Email email = emailRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Draft not found"));

	    // Validate state
	    if (email.getStatus() != EmailStatus.DRAFT) {
	        throw new IllegalStateException("Only drafts can be sent");
	    }

	    if (email.isDeleted()) {
	        throw new IllegalStateException("Cannot send deleted draft");
	    }

	    if (email.getToEmail() == null || 
	        email.getSubject() == null || 
	        email.getMessage() == null) {

	        throw new IllegalStateException("Draft is incomplete");
	    }

	    // Move to PENDING
	    email.setStatus(EmailStatus.PENDING);
	    email.setUpdatedAt(LocalDateTime.now());
	    emailRepo.save(email);

	    // Reuse your existing async send method
	    sendEmailAsync(email.getId());
	}


	
	public Page<DraftEmailResponse> getDrafts(String fromEmail, int page, int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    Page<Email> drafts = emailRepo.findDrafts(fromEmail, pageable);

	    return drafts.map(email -> {
	        DraftEmailResponse dto = new DraftEmailResponse();
	        dto.setId(email.getId());
	        dto.setToEmail(email.getToEmail());
	        dto.setSubject(email.getSubject());
	        dto.setMessage(email.getMessage());
	        dto.setUpdatedAt(email.getUpdatedAt());
	        return dto;
	    });
	}

	
	//SEARCH Logics
	@Override
	public Page<InboxEmailResponse> searchInbox(String toEmail, String keyword, int page, int size) {
		
		Pageable pageable = PageRequest.of(page,size);
		
		Page<Email> emailspage = emailRepo.searchInboxEmails(toEmail, keyword, pageable);
		
		return emailspage.map(this::EmailEntityToInboxEmailResponse);
		
	}


	@Override
	public Page<SentEmailResponse> searchSent(String fromEmail, String keyword, int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Email> emailspage = emailRepo.searchSentEmails(fromEmail, keyword, pageable);
		
		return emailspage.map(this::EmailEntityToSentEmailResponse);
	}

	
	//Filtering based on Status
	public Page<InboxEmailResponse> getInbox(
	        String toEmail,
	        EmailStatus status,
	        int page,
	        int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    Page<Email> emails =
	            emailRepo.findInboxWithStatus(toEmail, status, pageable);

	    return emails.map(this::EmailEntityToInboxEmailResponse);
	}

	
	
	
	@Override
	public void moveToTrash(Long id) {

	    Email email = emailRepo.findById(id)
	            .orElseThrow(() -> new EmailNotFoundException("Email not found"));

	    if (email.isDeleted()) {
	        throw new IllegalStateException("Email already in trash");
	    }

	    email.setDeleted(true);
	    email.setUpdatedAt(LocalDateTime.now());

	    emailRepo.save(email);
	}
	
	@Override
	public void restoreEmail(Long id) {

	    Email email = emailRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Email not found"));

	    if (!email.isDeleted()) {
	        throw new IllegalStateException("Email is not in trash");
	    }

	    email.setDeleted(false);
	    email.setUpdatedAt(LocalDateTime.now());

	    emailRepo.save(email);
	}
	
	
	@Override
	public Page<InboxEmailResponse> getTrashEmails(
	        String emailAddress,
	        int page,
	        int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    Page<Email> emails =
	            emailRepo.findTrashEmails(emailAddress, pageable);

	    return emails.map(this::EmailEntityToInboxEmailResponse);
	}
	
	//Archive Implementation
	@Override
	public void archiveEmail(Long id) {

	    Email email = emailRepo.findById(id)
	            .orElseThrow(() ->
	                    new EmailNotFoundException("Email not found"));

	    if (email.isDeleted()) {
	        throw new IllegalStateException("Cannot archive a deleted email");
	    }

	    if (email.isArchived()) {
	        throw new IllegalStateException("Email already archived");
	    }

	    email.setArchived(true);
	    email.setUpdatedAt(LocalDateTime.now());

	    emailRepo.save(email);
	}
	
	@Override
	public void unarchiveEmail(Long id) {

	    Email email = emailRepo.findById(id)
	            .orElseThrow(() ->
	                    new EmailNotFoundException("Email not found"));

	    if (!email.isArchived()) {
	        throw new IllegalStateException("Email is not archived");
	    }

	    email.setArchived(false);
	    email.setUpdatedAt(LocalDateTime.now());

	    emailRepo.save(email);
	}
	@Override
	public Page<InboxEmailResponse> getArchivedEmails(
	        String emailAddress,
	        int page,
	        int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    return emailRepo
	            .findArchivedEmails(emailAddress, pageable)
	            .map(this::EmailEntityToInboxEmailResponse);
	}

	
	//DTO Mappings 
	
	private SentEmailResponse EmailEntityToSentEmailResponse(Email email) {
		SentEmailResponse dto = new SentEmailResponse();
		dto.setId(email.getId());
		dto.setMessage(email.getMessage());
		dto.setSentAt(email.getSentAt());
		dto.setSubject(email.getSubject());
		dto.setToEmail(email.getToEmail());
		dto.setStatus(email.getStatus());
		
		return dto;
	}
	
	private InboxEmailResponse EmailEntityToInboxEmailResponse(Email email) {
		InboxEmailResponse dto = new InboxEmailResponse();
		dto.setFromEmail(email.getFromEmail());
		dto.setId(email.getId());
		dto.setMessage(email.getMessage());
		dto.setSentAt(email.getSentAt());
		dto.setSubject(email.getSubject());
		return dto;
	}
	
}
