package com.scu.aicontractsummarizerdemo.controller;

import com.scu.aicontractsummarizerdemo.dto.AcceptLoanRequest;
import com.scu.aicontractsummarizerdemo.dto.LoanApplicationRequest;
import com.scu.aicontractsummarizerdemo.entity.LoanApplication;
import com.scu.aicontractsummarizerdemo.entity.LoanDocument;
import com.scu.aicontractsummarizerdemo.service.FileStorageService;
import com.scu.aicontractsummarizerdemo.service.LoanApplicationService;
import com.scu.aicontractsummarizerdemo.service.LoanDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;
    private final FileStorageService fileStorageService;
    private final LoanDocumentService loanDocumentService;

    public LoanApplicationController(LoanApplicationService loanApplicationService, FileStorageService fileStorageService, LoanDocumentService loanDocumentService) {
        this.loanApplicationService = loanApplicationService;
        this.fileStorageService = fileStorageService;
        this.loanDocumentService = loanDocumentService;
    }

    @PostMapping("/borrower/apply")
    public LoanApplication applyLoanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest) {
        return loanApplicationService.save(loanApplicationRequest);
    }

    @GetMapping("/borrower/{userId}")
    public List<LoanApplication> getUserLoanApplications(@PathVariable Long userId) {
        return loanApplicationService.getLoanApplicationsByUser(userId);
    }

    @DeleteMapping("/borrower/{id}")
    public ResponseEntity<Void> deleteLoanApplication(@PathVariable Long id) {
        loanApplicationService.deleteLoanApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/borrower/{id}")
    public LoanApplication updateLoanApplication(@PathVariable Long id, @RequestBody LoanApplicationRequest loanApplicationRequest) {
        return loanApplicationService.updateLoanApplication(id, loanApplicationRequest);
    }

    @GetMapping("/accepted/lender/{id}")
    public List<LoanApplication> getAcceptedLoanApplications(@PathVariable Long id) {
        return loanApplicationService.getAcceptedLoanApplications(id);
    }

    // Lender Side: Set loan agreement terms
    @PutMapping("/{loanId}/terms")
    public LoanApplication addLenderTerms(@PathVariable Long loanId, @RequestParam String terms) {
        return loanApplicationService.setLenderTerms(loanId, terms);
    }

    // Lender Side: Get all loan document data
    @GetMapping("/{loanId}/documents")
    public List<LoanDocument> listDocs(@PathVariable Long loanId) {
        return loanDocumentService.getDocumentsForLoan(loanId);
    }

    // Lender Side: Get all loan applications
    @GetMapping("/lender/{userId}")
    public List<LoanApplication> getLenderLoanApplications(@PathVariable Long userId) {
        return loanApplicationService.getLoanApplicationsByLender(userId);
    }

    // Lender Side: Accept a given loan with userId and loan agreement terms
    @PostMapping("/lender/{loanId}/accept")
    public LoanApplication acceptLoan(@PathVariable Long loanId, @RequestBody AcceptLoanRequest acceptLoanRequest) {
        long userId = acceptLoanRequest.getUserId();
        String terms = acceptLoanRequest.getTerms();
        return loanApplicationService.acceptLoan(loanId, userId, terms);
    }

    // Lender Side: Update loan terms
    @PatchMapping("/lender/{loanId}/terms")
    public LoanApplication setTerms(@PathVariable Long loanId, @RequestBody AcceptLoanRequest acceptLoanRequest) {
        String terms = acceptLoanRequest.getTerms();
        return loanApplicationService.updateLenderTerms(loanId, terms);
    }

    // Borrower Side: Upload required documents
    @PostMapping("/{loanId}/documents")
    public ResponseEntity<LoanDocument> uploadDocument(@PathVariable Long loanId, @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(loanApplicationService.uploadDocument(loanId, file));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lender/Borrower Side: Download/View loan document
    @GetMapping("/documents/{fileId}")
    public ResponseEntity<Resource> getDocument(@PathVariable Long fileId) {
        Resource file = fileStorageService.load(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @DeleteMapping("/documents/{fileId}")
    public ResponseEntity<LoanDocument> deleteDocument(@PathVariable Long fileId) {
        LoanDocument loanDocument;
        try {
            loanDocument = fileStorageService.delete(fileId);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        if (loanDocument == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(loanDocument);
    }

    @PostMapping("/borrowers/{loanId}/confirm")
    public ResponseEntity<LoanApplication> confirmLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanApplicationService.borrowerAccept(loanId));
    }

    @PostMapping("/lenders/{loanId}/approve")
    public ResponseEntity<LoanApplication> approveLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanApplicationService.setApproval(loanId, true));
    }

    @PostMapping("/lenders/{loanId}/reject")
    public ResponseEntity<LoanApplication> rejectLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanApplicationService.setApproval(loanId, false));
    }

}