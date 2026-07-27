package com.aeromail.controller;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aeromail.model.dto.DraftEmailRequest;
import com.aeromail.model.dto.DraftEmailResponse;
import com.aeromail.model.dto.DraftUpdateRequest;
import com.aeromail.model.dto.EmailRequest;
import com.aeromail.model.dto.EmailResponse;
import com.aeromail.model.dto.InboxEmailResponse;
import com.aeromail.model.dto.SentEmailResponse;
import com.aeromail.model.enums.EmailStatus;
import com.aeromail.service.EmailService;
import com.aeromail.service.impl.EmailServiceImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping("email")
public class emailController {


	EmailService eService;
	
	emailController(EmailService eService){
		this.eService = eService;
	}
	
	//Save and send Email REST API
	@PostMapping("save")
	ResponseEntity<EmailResponse> sendAndSaveEmail( @Valid @RequestBody EmailRequest request){
		eService.saveEmail(request);
		EmailResponse response = new EmailResponse("Email sent and saved successfully", "SUCCESS");
		return ResponseEntity.ok(response);
	}
	
	
	//Now we will get all the mails for inbox feature 
	//We have included Pagination feature with this too
//	@GetMapping("/inbox/{toEmail}")
//	ResponseEntity<Page<InboxEmailResponse>> getInboxEmails(
//			@PathVariable String toEmail,
//			@RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "5") int size
//			){
//		
//		return ResponseEntity.ok(eService.getInBoxMails(toEmail,page,size));
//	}
//	
	
	//Now we will get the emails for sent box feature
	//We have included Pagination feature with this too
	@GetMapping("/sent/{from}")
	ResponseEntity<Page<SentEmailResponse>> getSentEmails(
			@PathVariable String from,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size
			){
		return ResponseEntity.ok(eService.getSentBoxMails(from,page,size));
	}
	
	
	//we will make controller for search functionality for inbox mails.
	@GetMapping("/inbox/search/{toEmail}")
	public ResponseEntity<Page<InboxEmailResponse>> SearchInbox(
			@PathVariable String toEmail,
	        @RequestParam String keyword,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    return ResponseEntity.ok(
	            eService.searchInbox(toEmail, keyword, page, size)
	    );
	}
	
	@GetMapping("/sent/search/{fromEmail}")
	public ResponseEntity<Page<SentEmailResponse>> searchSent(
	        @PathVariable String fromEmail,
	        @RequestParam String keyword,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    return ResponseEntity.ok(
	            eService.searchSent(fromEmail, keyword, page, size)
	    );
	}

	
	//We will add draft functionality here
	@PostMapping("/draft")
	public ResponseEntity<String> saveDraft(
	        @Valid @RequestBody DraftEmailRequest request) {

	    eService.saveDraft(request);
	    return ResponseEntity.ok("Draft saved successfully");
	}

	
	@PutMapping("/draft/{id}")
	public ResponseEntity<String> updateDraft(
	        @PathVariable Long id,
	        @RequestBody DraftUpdateRequest request) {

	    eService.updateDraft(id, request);
	    return ResponseEntity.ok("Draft updated successfully");
	}

	@PostMapping("/draft/{id}/send")
	public ResponseEntity<String> sendDraft(@PathVariable Long id) {

	    eService.sendDraft(id);
	    return ResponseEntity.ok("Draft sending initiated");
	}
	
	@GetMapping("/drafts/{fromEmail}")
	public ResponseEntity<@Nullable Object> getDrafts(
	        @PathVariable String fromEmail,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    return ResponseEntity.ok(
	            eService.getDrafts(fromEmail, page, size)
	    );
	}
	
	//filtering
	@GetMapping("/inbox/{toEmail}")
	public ResponseEntity< Page<InboxEmailResponse>> getInbox(
	        @PathVariable String toEmail,
	        @RequestParam(required = false) EmailStatus status,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    return ResponseEntity.ok(
	            eService.getInbox(toEmail, status, page, size)
	    );
	}
	
	
	// Move email to trash
	@PutMapping("/trash/{id}")
	public ResponseEntity<String> moveToTrash(
	        @PathVariable Long id) {

	    eService.moveToTrash(id);
	    return ResponseEntity.ok("Email moved to trash");
	}


	// Restore email from trash
	@PutMapping("/restore/{id}")
	public ResponseEntity<String> restoreEmail(
	        @PathVariable Long id) {

	    eService.restoreEmail(id);
	    return ResponseEntity.ok("Email restored successfully");
	}


	// Get all trashed emails
	@GetMapping("/trash/{emailAddress}")
	public ResponseEntity<Page<InboxEmailResponse>> getTrashEmails(
	        @PathVariable String emailAddress,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {

	    return ResponseEntity.ok(
	            eService.getTrashEmails(emailAddress, page, size)
	    );
	}
	
	
	// Archive email
	@PutMapping("/archive/{id}")
	public ResponseEntity<String> archiveEmail(
	        @PathVariable Long id) {

	    eService.archiveEmail(id);

	    return ResponseEntity.ok("Email archived successfully");
	}
	
	
	// Unarchive email
	@PutMapping("/unarchive/{id}")
	public ResponseEntity<String> unarchiveEmail(
	        @PathVariable Long id) {

	    eService.unarchiveEmail(id);

	    return ResponseEntity.ok("Email restored from archive");
	}
	
	
	// View archived emails
	@GetMapping("/archive/{emailAddress}")
	public ResponseEntity<Page<InboxEmailResponse>> getArchivedEmails(

	        @PathVariable String emailAddress,

	        @RequestParam(defaultValue = "0") int page,

	        @RequestParam(defaultValue = "10") int size) {

	    return ResponseEntity.ok(

	            eService.getArchivedEmails(
	                    emailAddress,
	                    page,
	                    size
	            )
	    );
	}
	
	
	
	//Test
	@GetMapping("test")
	String getAllEmail() {
		return eService.getAllEmails();
	}
	
	
	
}
