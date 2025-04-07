package com.scu.aicontractsummarizerdemo.repository;

import com.scu.aicontractsummarizerdemo.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByUserIdOrderByCreatedAtDesc(long userId);
}
