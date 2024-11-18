package com.example.loancalc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationResponseDTO {
    private Map<String, BigDecimal> monthlyInstallments;
    private BigDecimal totalAmount;
    private BigDecimal totalInterest;
}
