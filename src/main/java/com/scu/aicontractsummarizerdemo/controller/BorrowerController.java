package com.scu.aicontractsummarizerdemo.controller;

import com.scu.aicontractsummarizerdemo.dto.BorrowerDetails;
import com.scu.aicontractsummarizerdemo.entity.Borrower;
import com.scu.aicontractsummarizerdemo.service.BorrowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrower")
public class BorrowerController {
    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BorrowerDetails> getBorrower(@PathVariable("userId") Long userId) {
        Borrower borrower = borrowerService.findByUserId(userId);
        if (borrower == null) {
            return ResponseEntity.notFound().build();
        } else {
            BorrowerDetails borrowerDetails = new BorrowerDetails();
            borrowerDetails.setBusinessName(borrower.getBusinessName());
            borrowerDetails.setAnnualRevenue(borrower.getAnnualRevenue());
            borrowerDetails.setIndustry(borrower.getIndustry());
            borrowerDetails.setEmployeeCount(borrower.getEmployeeCount());
            borrowerDetails.setYearEstablished(borrower.getYearEstablished());
            borrowerDetails.setBusinessType(borrower.getBusinessType());
            borrowerDetails.setBusinessDescription(borrower.getBusinessDescription());
            return ResponseEntity.ok(borrowerDetails);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateLenderPreferences(
            @PathVariable Long userId,
            @RequestBody BorrowerDetails request) {
        Borrower borrower = borrowerService.findByUserId(userId);
        if (borrower == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            borrower.setBusinessName(request.getBusinessName());
            borrower.setEmployeeCount(request.getEmployeeCount());
            borrower.setAnnualRevenue(request.getAnnualRevenue());
            borrower.setIndustry(request.getIndustry());
            borrower.setYearEstablished(request.getYearEstablished());
            borrower.setBusinessType(request.getBusinessType());
            borrower.setBusinessDescription(request.getBusinessDescription());
            System.out.println(request.getBusinessDescription());
            borrowerService.save(borrower);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("Lender preferences updated successfully.");
    }


}
