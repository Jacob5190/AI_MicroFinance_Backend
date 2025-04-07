package com.scu.aicontractsummarizerdemo.service;

import com.scu.aicontractsummarizerdemo.entity.Lender;
import com.scu.aicontractsummarizerdemo.repository.LenderRepository;
import com.scu.aicontractsummarizerdemo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LenderService {
    private final LenderRepository lenderRepository;
    private final UserRepository userRepository; // Needed to fetch the user

    public LenderService(LenderRepository lenderRepository, UserRepository userRepository) {
        this.lenderRepository = lenderRepository;
        this.userRepository = userRepository;
    }

    public Lender findByUserId(Long userId) {
        return lenderRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public void save(Lender lender) {
        lenderRepository.save(lender);
    }

}

