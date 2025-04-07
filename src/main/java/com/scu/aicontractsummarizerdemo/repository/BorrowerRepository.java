package com.scu.aicontractsummarizerdemo.repository;

import com.scu.aicontractsummarizerdemo.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    Optional<Borrower> findByUserId(Long userId);
}
