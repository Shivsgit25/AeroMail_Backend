package com.aeromail.service;


import com.aeromail.model.dto.EmailRequest;



public interface EmailService{

	void saveEmail(EmailRequest request);
	
	
	public String getAllEmails();

}

