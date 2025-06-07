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
import java.time.LocalDateTime;
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
        loan.setBorrowerId(loanApplicationRequest.getUserId());
        loan.setLoanTerm(loanApplicationRequest.getLoanTerm());
        loan.setDescription(loanApplicationRequest.getDescription());
        loan.setStatus(LoanStatus.PENDING);
        return loanApplicationRepository.save(loan);
    }

    public LoanApplication getLoanApplicationById(Long id) {
        return loanApplicationRepository.findById(id).orElse(null);
    }

    public List<LoanApplication> getAcceptedLoanApplications(long id) {
        return loanApplicationRepository.findByLenderIdAndStatusIsNot(id, LoanStatus.PENDING);
    }

    public List<LoanApplication> getLoanApplicationsByUser(Long userId) {
        return loanApplicationRepository.findByBorrowerIdOrderByCreatedAtDesc(userId);
    }

    public void deleteLoanApplication(Long loanApplicationId) {
        loanApplicationRepository.deleteById(loanApplicationId);
    }

    public LoanApplication updateLoanApplication(Long loanApplicationId, LoanApplicationRequest loanApplicationRequest) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElseThrow(() -> new RuntimeException("Loan application not found with id: " + loanApplicationId));
        loanApplication.setLoanAmount(loanApplicationRequest.getLoanAmount());
        loanApplication.setBorrowerId(loanApplicationRequest.getUserId());
        loanApplication.setLoanTerm(loanApplicationRequest.getLoanTerm());
        loanApplication.setDescription(loanApplicationRequest.getDescription());
        loanApplication.setStatus(loanApplicationRequest.getStatus());
        return loanApplicationRepository.save(loanApplication);
    }

    public LoanApplication setLenderTerms(Long loanId, String terms) {
        LoanApplication loan = loanApplicationRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new IllegalStateException("Cannot set terms unless status is PENDING");
        }
        loan.setLenderTerms(terms);
        return loanApplicationRepository.save(loan);
    }

    public LoanDocument uploadDocument(Long loanId, MultipartFile file) throws IOException {
        String fileUrl = fileStorageService.store(file);
        LoanDocument doc = new LoanDocument();
        doc.setLoanId(loanId);
        doc.setFileName(file.getOriginalFilename());
        doc.setFileUrl(fileUrl);
        return loanDocumentRepository.save(doc);
    }



    public LoanApplication acceptLoan(Long loanId, Long lenderId, String terms) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan application not found with id: " + loanId));
        if (loanApplication.getStatus() != LoanStatus.PENDING) {
            throw new IllegalStateException("Loan has already been claimed or processed");
        }
        loanApplication.setLenderId(lenderId);
        loanApplication.setStatus(LoanStatus.ACCEPTED_BY_LENDER);
        loanApplication.setLenderTerms(terms);
        loanApplication.setBorrowerConfirmedAt(LocalDateTime.now());
        return loanApplicationRepository.save(loanApplication);
    }

    public LoanApplication updateLenderTerms(Long loanId, String terms) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan application not found with id: " + loanId));
        if (loanApplication.getStatus() != LoanStatus.ACCEPTED_BY_LENDER) {
            throw new IllegalStateException("Loan has already been claimed or processed");
        }
        loanApplication.setLenderTerms(terms);
        return loanApplicationRepository.save(loanApplication);
    }

    public List<LoanApplication> getLoanApplicationsByLender(Long lenderId) {
        return loanApplicationRepository.findByLenderId(lenderId);
    }

    public LoanApplication borrowerAccept(Long loanId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan application not found with id: " + loanId));
        if (loanApplication.getStatus() != LoanStatus.ACCEPTED_BY_LENDER) {
            throw new IllegalStateException("Loan has already been claimed or processed");
        }
        loanApplication.setStatus(LoanStatus.BORROWER_CONFIRMED);
        return loanApplicationRepository.save(loanApplication);
    }

    public LoanApplication setApproval(Long loanId, boolean flag) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan application not found with id: " + loanId));
        if (loanApplication.getStatus() != LoanStatus.BORROWER_CONFIRMED) {
            throw new IllegalStateException("Loan has already been claimed or processed");
        }
        if (flag) {
            loanApplication.setStatus(LoanStatus.APPROVED);
            loanApplication.setApproved(true);
            loanApplication.setApprovedAt(LocalDateTime.now());
        } else {
            loanApplication.setStatus(LoanStatus.REJECTED);
            loanApplication.setRejected(true);
            loanApplication.setRejectedAt(LocalDateTime.now());
        }
        return loanApplicationRepository.save(loanApplication);
    }
}
