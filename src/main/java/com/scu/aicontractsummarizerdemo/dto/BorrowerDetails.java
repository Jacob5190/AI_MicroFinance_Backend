package com.scu.aicontractsummarizerdemo.dto;

import lombok.Data;

@Data
public class BorrowerDetails {
    private String businessName;
    private String businessType;
    private String industry;
    private Double annualRevenue;
    private Integer yearEstablished;
    private Integer employeeCount;
    private String businessDescription;
}

