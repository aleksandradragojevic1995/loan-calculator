package com.example.loancalc.controller;

import com.example.loancalc.dto.error.ErrorResponseDTO;
import com.example.loancalc.dto.request.LoanCalculationRequestDTO;
import com.example.loancalc.dto.response.LoanCalculationResponseDTO;
import com.example.loancalc.service.LoanCalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-calculations")
public class LoanCalculationController {

    private final LoanCalculationService loanCalculationService;

    public LoanCalculationController(LoanCalculationService loanCalculationService) {
        this.loanCalculationService = loanCalculationService;
    }

    @Operation(
            summary = "Calculate Loan Installments",
            description = "Calculates the monthly installments based on the input parameters."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated loan installments", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LoanCalculationResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LoanCalculationResponseDTO calculateLoan(@Valid @RequestBody LoanCalculationRequestDTO loanCalculationRequestDTO) {
        return loanCalculationService.createLoanCalculation(loanCalculationRequestDTO);
    }
}
