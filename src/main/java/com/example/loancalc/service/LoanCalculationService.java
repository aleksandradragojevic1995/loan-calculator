package com.example.loancalc.service;

import com.example.loancalc.dto.request.LoanCalculationRequestDTO;
import com.example.loancalc.dto.response.LoanCalculationResponseDTO;

public interface LoanCalculationService {
    LoanCalculationResponseDTO createLoanCalculation(LoanCalculationRequestDTO loanCalculationRequestDTO);
}
