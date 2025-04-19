package com.scu.aicontractsummarizerdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "loan_application")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long borrowerId;

    private Long lenderId;

    private BigDecimal loanAmount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String loanTerm;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    private String lenderTerms;

    private LocalDateTime borrowerConfirmedAt;

    private LocalDateTime approvedAt;

    @Column(name = "approved")
    private Boolean approved;

    private LocalDateTime rejectedAt;

    @Column(name = "rejected")
    private Boolean rejected;
}
