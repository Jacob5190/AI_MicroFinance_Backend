package com.scu.aicontractsummarizerdemo.repository;

import com.scu.aicontractsummarizerdemo.entity.LoanApplication;
import com.scu.aicontractsummarizerdemo.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByBorrowerIdOrderByCreatedAtDesc(long userId);

    List<LoanApplication> findByLenderIdAndStatusIsNot(long lenderId, LoanStatus status);

    List<LoanApplication> findByLenderId(Long lenderId);
}
