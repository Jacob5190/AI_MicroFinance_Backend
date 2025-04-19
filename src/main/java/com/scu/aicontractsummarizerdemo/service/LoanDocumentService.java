package com.scu.aicontractsummarizerdemo.service;

import com.scu.aicontractsummarizerdemo.entity.LoanDocument;
import com.scu.aicontractsummarizerdemo.repository.LoanDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanDocumentService {
    private final LoanDocumentRepository loanDocumentRepository;

    public LoanDocumentService(LoanDocumentRepository loanDocumentRepository) {
        this.loanDocumentRepository = loanDocumentRepository;
    }

    public List<LoanDocument> getDocumentsForLoan(Long loanId) {
        return loanDocumentRepository.findByLoanId(loanId);
    }

}
