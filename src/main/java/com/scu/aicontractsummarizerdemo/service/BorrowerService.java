package com.scu.aicontractsummarizerdemo.service;

import com.scu.aicontractsummarizerdemo.entity.Borrower;
import com.scu.aicontractsummarizerdemo.repository.BorrowerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;

    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public Borrower findByUserId(Long userId) {
        return borrowerRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public void save(Borrower borrower) {
        borrowerRepository.save(borrower);
    }

    public List<Borrower> findAll() {
        return borrowerRepository.findAll();
    }
}
