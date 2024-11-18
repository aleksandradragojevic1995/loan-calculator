package com.example.loancalc.mapper;

import com.example.loancalc.dto.response.LoanCalculationResponseDTO;
import com.example.loancalc.model.LoanCalculation;
import com.example.loancalc.model.LoanCalculationInstallment;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LoanCalculationResponseDTOMapper {
    @Mapping(target = "monthlyInstallments", ignore = true)
    LoanCalculationResponseDTO toDTO(LoanCalculation loanCalculation);

    @AfterMapping
    default void mapMonthlyInstallments(LoanCalculation loanCalculation,
                                        @MappingTarget LoanCalculationResponseDTO responseDTO) {
        var map = loanCalculation.getMonthlyInstallments().stream()
                .collect(Collectors.toMap(
                        inst -> "month " + inst.getInstallmentNumber(),
                        LoanCalculationInstallment::getInstallmentAmount,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
        responseDTO.setMonthlyInstallments(map);
    }
}
