package com.aeromail.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aeromail.model.entity.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

	Page<Email> findByToEmailOrderBySentAtDesc(String toEmail,Pageable pageable);

	Page<Email> findByFromEmailOrderBySentAtDesc(String from, Pageable pageable);
	
	//we will make search functionality for the sent and inbox
	@Query("""
		    SELECT e FROM Email e
		    WHERE e.fromEmail = :fromEmail
		    AND (
		        LOWER(e.subject) LIKE LOWER(CONCAT('%', :keyword, '%'))
		        OR LOWER(e.message) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    )
		""")
		Page<Email> searchSentEmails(
		        @Param("fromEmail") String fromEmail,
		        @Param("keyword") String keyword,
		        Pageable pageable
		);


	@Query("""
		    SELECT e FROM Email e
		    WHERE e.toEmail = :toEmail
		    AND (
		        LOWER(e.subject) LIKE LOWER(CONCAT('%', :keyword, '%'))
		        OR LOWER(e.message) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    )
		""")
		Page<Email> searchInboxEmails(
		        @Param("toEmail") String toEmail,
		        @Param("keyword") String keyword,
		        Pageable pageable
		);



	
}
