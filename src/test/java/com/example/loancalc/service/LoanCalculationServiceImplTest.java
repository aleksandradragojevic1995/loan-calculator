package com.example.loancalc.service;

import com.example.loancalc.dto.request.LoanCalculationRequestDTO;
import com.example.loancalc.dto.response.LoanCalculationResponseDTO;
import com.example.loancalc.mapper.LoanCalculationMapper;
import com.example.loancalc.mapper.LoanCalculationResponseDTOMapper;
import com.example.loancalc.model.LoanCalculation;
import com.example.loancalc.model.LoanCalculationInstallment;
import com.example.loancalc.repository.LoanCalculationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanCalculationServiceImplTest {
    @Mock
    private LoanCalculationRepository loanCalculationRepository;

    @Mock
    private LoanCalculationMapper loanCalculationMapper;

    @Mock
    private LoanCalculationResponseDTOMapper loanCalculationResponseDTOMapper;

    @InjectMocks
    private LoanCalculationServiceImpl loanCalculationService;

    @Test
    @DisplayName("Create LoanCalculation - Success")
    void testCreateLoanCalculation_Success() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal annualInterestPercent = new BigDecimal("5.00");
        int numberOfMonths = 2;
        BigDecimal monthlyInterest = new BigDecimal("503.13");
        BigDecimal totalAmount = monthlyInterest.multiply(new BigDecimal(numberOfMonths));
        BigDecimal totalInterest = totalAmount.subtract(amount);

        LoanCalculationRequestDTO requestDTO = new LoanCalculationRequestDTO();
        requestDTO.setAmount(amount);
        requestDTO.setAnnualInterestPercent(annualInterestPercent);
        requestDTO.setNumberOfMonths(numberOfMonths);

        LoanCalculation loanCalculationEntity = new LoanCalculation();
        loanCalculationEntity.setAmount(amount);
        loanCalculationEntity.setAnnualInterestPercent(annualInterestPercent);
        loanCalculationEntity.setNumberOfMonths(numberOfMonths);
        loanCalculationEntity.setTotalAmount(totalAmount);
        loanCalculationEntity.setTotalInterest(totalInterest);

        List<LoanCalculationInstallment> installments = new ArrayList<>();
        LoanCalculationInstallment installment1 = new LoanCalculationInstallment();
        installment1.setInstallmentNumber(1);
        installment1.setInstallmentAmount(monthlyInterest);
        installments.add(installment1);

        LoanCalculationInstallment installment2 = new LoanCalculationInstallment();
        installment2.setInstallmentNumber(2);
        installment2.setInstallmentAmount(monthlyInterest);
        installments.add(installment2);

        loanCalculationEntity.setMonthlyInstallments(installments);

        LoanCalculationResponseDTO responseDTO = new LoanCalculationResponseDTO();
        Map<String, BigDecimal> monthlyInstallmentsMap = new LinkedHashMap<>();
        monthlyInstallmentsMap.put("month 1", monthlyInterest);
        monthlyInstallmentsMap.put("month 2", monthlyInterest);
        responseDTO.setMonthlyInstallments(monthlyInstallmentsMap);
        responseDTO.setTotalAmount(totalAmount);
        responseDTO.setTotalInterest(totalInterest);

        when(loanCalculationMapper.toEntity(requestDTO)).thenReturn(loanCalculationEntity);
        when(loanCalculationRepository.save(loanCalculationEntity)).thenReturn(loanCalculationEntity);
        when(loanCalculationResponseDTOMapper.toDTO(loanCalculationEntity)).thenReturn(responseDTO);

        LoanCalculationResponseDTO result = loanCalculationService.createLoanCalculation(requestDTO);

        assertNotNull(result);
        assertEquals(totalAmount, result.getTotalAmount());
        assertEquals(totalInterest, result.getTotalInterest());
        assertEquals(numberOfMonths, result.getMonthlyInstallments().size());
        assertEquals(monthlyInterest, result.getMonthlyInstallments().get("month 1"));
        assertEquals(monthlyInterest, result.getMonthlyInstallments().get("month 2"));

        verify(loanCalculationMapper, times(1)).toEntity(requestDTO);
        verify(loanCalculationRepository, times(1)).save(loanCalculationEntity);
        verify(loanCalculationResponseDTOMapper, times(1)).toDTO(loanCalculationEntity);
    }

}

