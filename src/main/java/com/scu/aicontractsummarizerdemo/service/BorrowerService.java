package com.scu.aicontractsummarizerdemo.service;

import com.scu.aicontractsummarizerdemo.entity.Borrower;
import com.scu.aicontractsummarizerdemo.repository.BorrowerRepository;
import com.scu.aicontractsummarizerdemo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;
    private final UserRepository userRepository;

    public BorrowerService(BorrowerRepository borrowerRepository, UserRepository userRepository) {
        this.borrowerRepository = borrowerRepository;
        this.userRepository = userRepository;
    }

    public Borrower findByUserId(Long userId) {
        return borrowerRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public void save(Borrower borrower) {
        borrowerRepository.save(borrower);
    }
}
