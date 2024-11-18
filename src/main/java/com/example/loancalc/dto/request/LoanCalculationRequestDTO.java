package com.example.loancalc.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCalculationRequestDTO {
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal annualInterestPercent;

    @NotNull
    @Min(value = 1)
    private Integer numberOfMonths;
}