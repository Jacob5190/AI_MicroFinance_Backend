package com.scu.aicontractsummarizerdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lenders")
public class Lender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String organizationType;
    private String preferredIndustries;
    private Double maxLoanAmount;
    private String preferredLoanTerms;
    @Enumerated(EnumType.STRING)
    private RiskTolerance riskTolerance;
    private String companyName;
}



