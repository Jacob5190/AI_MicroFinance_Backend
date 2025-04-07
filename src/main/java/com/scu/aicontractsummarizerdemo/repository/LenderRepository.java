package com.scu.aicontractsummarizerdemo.repository;

import com.scu.aicontractsummarizerdemo.entity.Lender;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LenderRepository extends JpaRepository<Lender, Long> {
    Optional<Lender> findByUserId(Long userId);
}
