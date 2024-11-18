package com.example.loancalc.service;

import com.example.loancalc.dto.request.LoanCalculationRequestDTO;
import com.example.loancalc.dto.response.LoanCalculationResponseDTO;
import com.example.loancalc.mapper.LoanCalculationMapper;
import com.example.loancalc.mapper.LoanCalculationResponseDTOMapper;
import com.example.loancalc.model.LoanCalculation;
import com.example.loancalc.model.LoanCalculationInstallment;
import com.example.loancalc.repository.LoanCalculationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanCalculationServiceImpl implements LoanCalculationService {
    private final LoanCalculationRepository loanCalculationRepository;
    private final LoanCalculationMapper loanCalculationMapper;
    private final LoanCalculationResponseDTOMapper loanCalculationResponseDTOMapper;

    public LoanCalculationServiceImpl(LoanCalculationRepository loanCalculationRepository, LoanCalculationMapper loanCalculationMapper, LoanCalculationResponseDTOMapper loanCalculationResponseDTOMapper) {
        this.loanCalculationRepository = loanCalculationRepository;
        this.loanCalculationMapper = loanCalculationMapper;
        this.loanCalculationResponseDTOMapper = loanCalculationResponseDTOMapper;
    }

    @Override
    public LoanCalculationResponseDTO createLoanCalculation(LoanCalculationRequestDTO loanCalculationRequestDTO) {
        var loanCalculation = generateLoanCalculation(loanCalculationRequestDTO);
        var savedLoanCalculation = loanCalculationRepository.save(loanCalculation);
        return loanCalculationResponseDTOMapper.toDTO(savedLoanCalculation);
    }

    private LoanCalculation generateLoanCalculation(LoanCalculationRequestDTO loanCalculationRequestDTO) {
        var loanCalculation = loanCalculationMapper.toEntity(loanCalculationRequestDTO);
        var monthlyPayment = calculateMonthlyPayment(loanCalculation);

        var totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(loanCalculation.getNumberOfMonths())).setScale(2, RoundingMode.HALF_UP);
        loanCalculation.setTotalAmount(totalAmount);
        loanCalculation.setTotalInterest(totalAmount.subtract(loanCalculation.getAmount()));

        List<LoanCalculationInstallment> installments = createLoanCalculationInstallments(loanCalculation, monthlyPayment.setScale(2, RoundingMode.HALF_UP));

        loanCalculation.setMonthlyInstallments(installments);
        return loanCalculation;
    }

    private List<LoanCalculationInstallment> createLoanCalculationInstallments(LoanCalculation loanCalculation, BigDecimal monthlyPayment) {
        List<LoanCalculationInstallment> installments = new ArrayList<>();
        for (int i = 0; i < loanCalculation.getNumberOfMonths(); i++) {
            var installment = new LoanCalculationInstallment();
            installment.setLoanCalculation(loanCalculation);
            installment.setInstallmentAmount(monthlyPayment);
            installment.setInstallmentNumber(i + 1);
            installments.add(installment);
        }
        return installments;
    }

    private BigDecimal calculateMonthlyPayment(LoanCalculation loanCalculation) {
        var annualInterestRate = loanCalculation.getAnnualInterestPercent();
        var amount = loanCalculation.getAmount();
        var numberOfMonths = loanCalculation.getNumberOfMonths();

        var mc = new MathContext(20, RoundingMode.HALF_UP);
        var monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12 * 100L), mc);

        BigDecimal monthlyPayment;

        if (BigDecimal.ZERO.compareTo(monthlyInterestRate) == 0) {
            monthlyPayment = amount.divide(BigDecimal.valueOf(numberOfMonths), mc);
        } else {
            var onePlusRatePowN = BigDecimal.ONE.add(monthlyInterestRate).pow(numberOfMonths, mc);
            var numerator = amount.multiply(monthlyInterestRate).multiply(onePlusRatePowN, mc);
            var denominator = onePlusRatePowN.subtract(BigDecimal.ONE, mc);

            monthlyPayment = numerator.divide(denominator, mc);
        }

        return monthlyPayment;
    }


}
