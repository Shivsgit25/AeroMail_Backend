package com.aeromail.model.dto;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class EmailResponse {

	private String message;
    private String status;
    private LocalDateTime timestamp;
    
    public EmailResponse(String message, String status){
    	this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
