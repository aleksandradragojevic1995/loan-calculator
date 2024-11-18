package com.example.loancalc.mapper;

import com.example.loancalc.dto.request.LoanCalculationRequestDTO;
import com.example.loancalc.model.LoanCalculation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanCalculationMapper {
    LoanCalculation toEntity(LoanCalculationRequestDTO loanCalculationRequestDTO);
}
