package com.scu.aicontractsummarizerdemo.dto;

import lombok.Data;

import java.util.List;

@Data
public class LenderDetails {
    private String organizationType;
    private List<String> preferredIndustries;
    private Double maxLoanAmount;
    private String riskTolerance;
    private String companyName;
    private List<String> preferredLoanTerms;
}
