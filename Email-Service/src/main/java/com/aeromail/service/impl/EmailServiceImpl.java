package com.aeromail.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.aeromail.model.dto.EmailRequest;
import com.aeromail.model.dto.InboxEmailResponse;
import com.aeromail.model.dto.SentEmailResponse;
import com.aeromail.model.entity.Email;
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
        email.setStatus("PENDING");
        email.setSentAt(LocalDateTime.now());

        //Saving the email in DB
        emailRepo.save(email);
        //Sending the mail Asynchronously (Non Blocking mechanism).  
        sendEmailAsync(email);
	}
	
	//this method is made to seprate the smtp call and make it asynchronous.
	@Async
	public void sendEmailAsync(Email email) {

	    try {
	        SimpleMailMessage mail = new SimpleMailMessage();
	        mail.setFrom(email.getFromEmail());
	        mail.setTo(email.getToEmail());
	        mail.setSubject(email.getSubject());
	        mail.setText(email.getMessage());

	        mailSender.send(mail);

	        email.setStatus("SENT");

	    } catch (Exception ex) {
	        email.setStatus("FAILED");
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
