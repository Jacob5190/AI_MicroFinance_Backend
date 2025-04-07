package com.scu.aicontractsummarizerdemo.service;

import com.scu.aicontractsummarizerdemo.dto.LoanApplicationRequest;
import com.scu.aicontractsummarizerdemo.entity.LoanApplication;
import com.scu.aicontractsummarizerdemo.entity.LoanDocument;
import com.scu.aicontractsummarizerdemo.entity.LoanStatus;
import com.scu.aicontractsummarizerdemo.repository.LoanApplicationRepository;
import com.scu.aicontractsummarizerdemo.repository.LoanDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class LoanApplicationService {
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanDocumentRepository loanDocumentRepository;

    private final FileStorageService fileStorageService;

    public LoanApplicationService(LoanApplicationRepository loanApplicationRepository, LoanDocumentRepository loanDocumentRepository, FileStorageService fileStorageService) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.loanDocumentRepository = loanDocumentRepository;
        this.fileStorageService = fileStorageService;
    }

    public LoanApplication save(LoanApplicationRequest loanApplicationRequest) {
        LoanApplication loan = new LoanApplication();
        loan.setLoanAmount(loanApplicationRequest.getLoanAmount());
        loan.setUserId(loanApplicationRequest.getUserId());
        loan.setLoanTerm(loanApplicationRequest.getLoanTerm());
        loan.setDescription(loanApplicationRequest.getDescription());
        loan.setStatus(LoanStatus.PENDING);
        return loanApplicationRepository.save(loan);
    }

    public List<LoanApplication> getLoanApplicationsByUser(Long userId) {
        return loanApplicationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void deleteLoanApplication(Long loanApplicationId) {
        loanApplicationRepository.deleteById(loanApplicationId);
    }

    public LoanApplication updateLoanApplication(Long loanApplicationId, LoanApplicationRequest loanApplicationRequest) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElseThrow(() -> new RuntimeException("Loan application not found with id: " + loanApplicationId));
        loanApplication.setLoanAmount(loanApplicationRequest.getLoanAmount());
        loanApplication.setUserId(loanApplicationRequest.getUserId());
        loanApplication.setLoanTerm(loanApplicationRequest.getLoanTerm());
        loanApplication.setDescription(loanApplicationRequest.getDescription());
        loanApplication.setStatus(loanApplicationRequest.getStatus());
        return loanApplicationRepository.save(loanApplication);
    }

    public LoanApplication updateStatus(Long loanId, LoanStatus newStatus) {
        LoanApplication loan = loanApplicationRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(newStatus);
        return loanApplicationRepository.save(loan);
    }

    public LoanApplication setLenderTerms(Long loanId, String terms, Long lenderId) {
        LoanApplication loan = loanApplicationRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new IllegalStateException("Cannot set terms unless status is PENDING");
        }
        loan.setLenderTerms(terms);
        return loanApplicationRepository.save(loan);
    }

    public LoanDocument uploadDocument(Long loanId, MultipartFile file) throws IOException {
        String fileUrl = fileStorageService.store(file, "loan_" + loanId);
        LoanDocument doc = new LoanDocument();
        doc.setLoanId(loanId);
        doc.setFileName(file.getOriginalFilename());
        doc.setFileUrl(fileUrl);
        return loanDocumentRepository.save(doc);
    }

    public List<LoanDocument> getDocumentsForLoan(Long loanId) {
        return loanDocumentRepository.findByLoanId(loanId);
    }


}
