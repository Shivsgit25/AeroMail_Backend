package com.aeromail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aeromail.model.entity.Email;

@Repository
public interface emailRepository extends JpaRepository<Email, Long> {

	
}
