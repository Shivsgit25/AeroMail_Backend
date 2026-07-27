package com.aeromail.model.entity;

import java.time.LocalDateTime;

import com.aeromail.model.enums.EmailStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email")
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String fromEmail;

	private String toEmail;

	private String subject;
	@Column(columnDefinition = "Text")
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
    private EmailStatus status;
    // DRAFT, PENDING, SENT, FAILED, DELIVERED

    private boolean archived = false;
    private boolean deleted = false;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
	private LocalDateTime sentAt;
	@Column(nullable = false)
	private LocalDateTime updatedAt;


}


