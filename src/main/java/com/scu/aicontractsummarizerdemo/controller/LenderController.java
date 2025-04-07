package com.scu.aicontractsummarizerdemo.controller;

import com.scu.aicontractsummarizerdemo.dto.LenderDetails;
import com.scu.aicontractsummarizerdemo.entity.Lender;
import com.scu.aicontractsummarizerdemo.entity.RiskTolerance;
import com.scu.aicontractsummarizerdemo.service.LenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/lender")
public class LenderController {
    private final LenderService lenderService;

    public LenderController(LenderService lenderService) {
        this.lenderService = lenderService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<LenderDetails> getLender(@PathVariable Long userId) {
        Lender lender = lenderService.findByUserId(userId);
        if (lender == null) {
            return ResponseEntity.notFound().build();
        } else {
            LenderDetails lenderDetails = new LenderDetails();
            lenderDetails.setCompanyName(lender.getCompanyName());

            lenderDetails.setOrganizationType(lender.getOrganizationType());
            lenderDetails.setPreferredIndustries(Arrays.asList(lender.getPreferredIndustries().split("\\s*,\\s*")));
            lenderDetails.setMaxLoanAmount(lender.getMaxLoanAmount());
            lenderDetails.setRiskTolerance(lenderDetails.getRiskTolerance());
            lenderDetails.setPreferredLoanTerms(Arrays.asList(lender.getPreferredLoanTerms().split("\\s*,\\s*")));
            return ResponseEntity.ok(lenderDetails);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateLenderPreferences(@PathVariable Long userId, @RequestBody LenderDetails request) {
        Lender lender = lenderService.findByUserId(userId);
        if (lender == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            lender.setCompanyName(request.getCompanyName());
            lender.setOrganizationType(request.getOrganizationType());
            lender.setPreferredIndustries(String.join(",", request.getPreferredIndustries()));
            lender.setPreferredLoanTerms(String.join(",", request.getPreferredLoanTerms()));
            lender.setMaxLoanAmount(request.getMaxLoanAmount());
            lender.setRiskTolerance(RiskTolerance.valueOf(request.getRiskTolerance()));
            lenderService.save(lender);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("Lender updated successfully.");
    }
}
