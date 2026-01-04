package com.aeromail.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String fromEmail;
	@Column(nullable = false)
	private String toEmail;
	@Column(nullable = false)
	private String subject;
	@Column(columnDefinition = "Text")
	private String message;
	@Column(nullable = false)
	private String status;
	@Column(nullable = false)
	private LocalDateTime sentAt;

}
