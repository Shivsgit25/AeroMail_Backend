package com.aeromail.service;
import org.springframework.data.domain.Page;
import com.aeromail.model.dto.EmailRequest;
import com.aeromail.model.dto.InboxEmailResponse;
import com.aeromail.model.dto.SentEmailResponse;



public interface EmailService{

	void saveEmail(EmailRequest request);
	
	
	public String getAllEmails();


	Page<InboxEmailResponse> getInBoxMails(String toEmail, int page, int size);


	Page<SentEmailResponse> getSentBoxMails(String from, int page , int size);

	Page<InboxEmailResponse> searchInbox(
	        String toEmail,
	        String keyword,
	        int page,
	        int size
	);

	Page<SentEmailResponse> searchSent(
	        String fromEmail,
	        String keyword,
	        int page,
	        int size
	);

}

