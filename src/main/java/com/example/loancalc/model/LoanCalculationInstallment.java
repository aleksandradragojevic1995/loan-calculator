package com.example.loancalc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanCalculationInstallment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal installmentAmount;
    private Integer installmentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private LoanCalculation loanCalculation;
}
