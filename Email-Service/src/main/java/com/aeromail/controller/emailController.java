package com.aeromail.controller;

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

import com.aeromail.model.dto.EmailRequest;
import com.aeromail.model.dto.EmailResponse;
import com.aeromail.model.dto.InboxEmailResponse;
import com.aeromail.model.dto.SentEmailResponse;
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
	@GetMapping("/inbox/{toEmail}")
	ResponseEntity<Page<InboxEmailResponse>> getInboxEmails(
			@PathVariable String toEmail,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size
			){
		
		return ResponseEntity.ok(eService.getInBoxMails(toEmail,page,size));
	}
	
	
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

	
	//Test
	@GetMapping("test")
	String getAllEmail() {
		return eService.getAllEmails();
	}
	
	
	
}
