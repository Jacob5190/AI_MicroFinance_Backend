package com.scu.aicontractsummarizerdemo.dto;

import com.scu.aicontractsummarizerdemo.entity.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanApplicationRequest {
    private Long userId;
    private Long lenderId;
    private BigDecimal loanAmount;
    private String description;
    private String loanTerm;
    private LoanStatus status;
}
