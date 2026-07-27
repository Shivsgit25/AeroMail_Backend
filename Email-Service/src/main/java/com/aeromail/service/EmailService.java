package com.aeromail.service;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;

import com.aeromail.model.dto.DraftEmailRequest;
import com.aeromail.model.dto.DraftUpdateRequest;
import com.aeromail.model.dto.EmailRequest;
import com.aeromail.model.dto.InboxEmailResponse;
import com.aeromail.model.dto.SentEmailResponse;
import com.aeromail.model.enums.EmailStatus;

import jakarta.validation.Valid;



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


	void saveDraft(@Valid DraftEmailRequest request);


	void updateDraft(Long id, DraftUpdateRequest request);


	void sendDraft(Long id);


	@Nullable
	Object getDrafts(String fromEmail, int page, int size);


	@Nullable
	Page<InboxEmailResponse> getInbox(String toEmail, EmailStatus status, int page, int size);
	
	
	void moveToTrash(Long id);

	void restoreEmail(Long id);

	Page<InboxEmailResponse> getTrashEmails(
	        String emailAddress,
	        int page,
	        int size);
	
	
	void archiveEmail(Long id);

	void unarchiveEmail(Long id);

	Page<InboxEmailResponse> getArchivedEmails(
	        String emailAddress,
	        int page,
	        int size
	);

}

