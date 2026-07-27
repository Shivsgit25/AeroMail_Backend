package com.aeromail.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aeromail.model.entity.Email;
import com.aeromail.model.enums.EmailStatus;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

	Page<Email> findByToEmailOrderBySentAtDesc(String toEmail,Pageable pageable);

	Page<Email> findByFromEmailOrderBySentAtDesc(String from, Pageable pageable);
	
	//we will make search functionality for the sent and inbox
	// Search Sent
	@Query("""
	        SELECT e FROM Email e
	        WHERE e.fromEmail = :fromEmail
	        AND e.deleted = false
	        AND e.archived = false
	        AND (
	                LOWER(e.subject) LIKE LOWER(CONCAT('%', :keyword, '%'))
	                OR LOWER(e.message) LIKE LOWER(CONCAT('%', :keyword, '%'))
	        )
	        ORDER BY e.sentAt DESC
	        """)
	Page<Email> searchSentEmails(
	        @Param("fromEmail") String fromEmail,
	        @Param("keyword") String keyword,
	        Pageable pageable
	);



	// Search Inbox
	@Query("""
	        SELECT e FROM Email e
	        WHERE e.toEmail = :toEmail
	        AND e.deleted = false
	        AND e.archived = false
	        AND (
	                LOWER(e.subject) LIKE LOWER(CONCAT('%', :keyword, '%'))
	                OR LOWER(e.message) LIKE LOWER(CONCAT('%', :keyword, '%'))
	        )
	        ORDER BY e.sentAt DESC
	        """)
	Page<Email> searchInboxEmails(
	        @Param("toEmail") String toEmail,
	        @Param("keyword") String keyword,
	        Pageable pageable
	);
	
	
	// Archive
	@Query("""
	        SELECT e
	        FROM Email e
	        WHERE e.archived = true
	        AND e.deleted = false
	        AND (
	                e.toEmail = :emailAddress
	                OR e.fromEmail = :emailAddress
	        )
	        ORDER BY e.updatedAt DESC
	        """)
	Page<Email> findArchivedEmails(
	        @Param("emailAddress") String emailAddress,
	        Pageable pageable
	);


	@Query("""
			SELECT e FROM Email e
			WHERE e.fromEmail = :fromEmail
			AND e.status = 'DRAFT'
			AND e.deleted = false
			AND e.archived = false
			ORDER BY e.updatedAt DESC
			""")
			Page<Email> findDrafts(String fromEmail, Pageable pageable);

		// Inbox
		@Query("""
		        SELECT e FROM Email e
		        WHERE e.toEmail = :toEmail
		        AND e.deleted = false
		        AND e.archived = false
		        AND (:status IS NULL OR e.status = :status)
		        ORDER BY e.sentAt DESC
		        """)
		Page<Email> findInboxWithStatus(
		        @Param("toEmail") String toEmail,
		        @Param("status") EmailStatus status,
		        Pageable pageable
		);
		
		
		// Sent
		@Query("""
		        SELECT e FROM Email e
		        WHERE e.fromEmail = :fromEmail
		        AND e.deleted = false
		        AND e.archived = false
		        AND (:status IS NULL OR e.status = :status)
		        ORDER BY e.sentAt DESC
		        """)
		Page<Email> findSentWithStatus(
		        @Param("fromEmail") String fromEmail,
		        @Param("status") EmailStatus status,
		        Pageable pageable
		);
		
		
		
	@Query("""
			SELECT e
			FROM Email e
			WHERE e.deleted = true
			AND (
			        e.toEmail = :emailAddress
			        OR e.fromEmail = :emailAddress
			    )
			ORDER BY e.updatedAt DESC
			""")
			Page<Email> findTrashEmails(
			        @Param("emailAddress") String emailAddress,
			        Pageable pageable
			);

	
}
