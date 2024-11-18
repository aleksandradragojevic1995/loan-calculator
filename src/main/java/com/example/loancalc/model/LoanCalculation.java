package com.example.loancalc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LoanCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private BigDecimal annualInterestPercent;
    private Integer numberOfMonths;
    private BigDecimal totalInterest;
    private BigDecimal totalAmount;

    @CreatedDate
    private LocalDateTime requestDate;

    @OneToMany(mappedBy = "loanCalculation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanCalculationInstallment> monthlyInstallments;
}

