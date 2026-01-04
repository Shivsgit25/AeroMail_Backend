package com.aeromail.service.impl;

import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.aeromail.model.dto.EmailRequest;
import com.aeromail.model.entity.Email;
import com.aeromail.repository.emailRepository;
import com.aeromail.service.EmailService;

@Service
public class emailServiceImpl implements EmailService {
	
	
	private final emailRepository emailRepo;
	private final JavaMailSender mailSender;

    public emailServiceImpl(emailRepository emailRepo , JavaMailSender mailSender) {
        this.emailRepo = emailRepo;
        this.mailSender = mailSender;
    }
	
	
	
	@Override
	public String getAllEmails() {
		return "Hello there";
	}

	@Override
	public void saveEmail(EmailRequest request) {
		Email email = new Email();
        email.setFromEmail(request.getFromEmail());
        email.setToEmail(request.getToEmail());
        email.setSubject(request.getSubject());
        email.setMessage(request.getMessage());
        email.setStatus("SENT");
        email.setSentAt(LocalDateTime.now());
        
        try {
            // 1️⃣ Send Email via SMTP
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(request.getFromEmail());
            mail.setTo(request.getToEmail());
            mail.setSubject(request.getSubject());
            mail.setText(request.getMessage());

            mailSender.send(mail);

            // 2️⃣ Update status
            email.setStatus("SENT");

        } catch (Exception ex) {
            // 3️⃣ Failure case
            email.setStatus("FAILED");
        }

        emailRepo.save(email);
        
	}
	
	

}
