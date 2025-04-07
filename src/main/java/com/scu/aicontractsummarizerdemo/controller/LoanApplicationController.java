package com.scu.aicontractsummarizerdemo.controller;

import com.scu.aicontractsummarizerdemo.dto.LoanApplicationRequest;
import com.scu.aicontractsummarizerdemo.entity.LoanApplication;
import com.scu.aicontractsummarizerdemo.entity.LoanDocument;
import com.scu.aicontractsummarizerdemo.entity.LoanStatus;
import com.scu.aicontractsummarizerdemo.service.LoanApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping("/apply")
    public LoanApplication applyLoanApplication(@RequestBody LoanApplicationRequest loanApplicationRequest) {
        return loanApplicationService.save(loanApplicationRequest);
    }

    @GetMapping("/user/{userId}")
    public List<LoanApplication> getUserLoanApplications(@PathVariable Long userId) {
        return loanApplicationService.getLoanApplicationsByUser(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanApplication(@PathVariable Long id) {
        loanApplicationService.deleteLoanApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public LoanApplication updateLoanApplication(@PathVariable Long id, @RequestBody LoanApplicationRequest loanApplicationRequest) {
        return loanApplicationService.updateLoanApplication(id, loanApplicationRequest);
    }

    @PatchMapping("/{loanId}/status")
    public LoanApplication updateLoanStatus(
            @PathVariable Long loanId,
            @RequestParam LoanStatus status
    ) {
        return loanApplicationService.updateStatus(loanId, status);
    }

    @PostMapping("/{loanId}/terms")
    public LoanApplication addLenderTerms(@PathVariable Long loanId, @RequestParam String terms, @RequestParam Long userId) {
        return loanApplicationService.setLenderTerms(loanId, terms, userId);
    }

    @PostMapping("/{loanId}/documents")
    public ResponseEntity<LoanDocument> uploadLoanDoc(@PathVariable Long loanId, @RequestParam MultipartFile file) {
        try {
            return ResponseEntity.ok(loanApplicationService.uploadDocument(loanId, file));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{loanId}/documents")
    public List<LoanDocument> listDocs(@PathVariable Long loanId) {
        return loanApplicationService.getDocumentsForLoan(loanId);
    }

}
