package com.aeromail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeromail.model.dto.EmailRequest;
import com.aeromail.model.dto.EmailResponse;
import com.aeromail.service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("email")
public class emailController {

	EmailService eService;
	
	emailController(EmailService eService){
		this.eService = eService;
	}
	
	@PutMapping("save")
	ResponseEntity<EmailResponse> sendAndSaveEmail( @Valid @RequestBody EmailRequest request){
		eService.saveEmail(request);
		EmailResponse response = new EmailResponse("Email sent and saved successfully", "SUCCESS");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("test")
	String getAllEmail() {
		return eService.getAllEmails();
	}
	
	
	
}
