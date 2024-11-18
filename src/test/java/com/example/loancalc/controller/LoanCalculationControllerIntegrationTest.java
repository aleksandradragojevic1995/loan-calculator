package com.example.loancalc.controller;

import com.example.loancalc.configuration.TestContainerConfiguration;
import com.example.loancalc.dto.request.LoanCalculationRequestDTO;
import com.example.loancalc.repository.LoanCalculationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = TestContainerConfiguration.class)
class LoanCalculationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoanCalculationRepository loanCalculationRepository;

    @BeforeAll
    void beforeAll() {
        this.loanCalculationRepository.deleteAll();
    }

    @Test
    @DisplayName("Calculate Loan - Success")
    void testCalculateLoan_Success() throws Exception {
        LoanCalculationRequestDTO requestDTO = createLoanCalculationRequestDTO("1000.00", "5.00", 2);

        mockMvc.perform(post("/api/v1/loan-calculations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(1006.25))
                .andExpect(jsonPath("$.totalInterest").value(6.25))
                .andExpect(jsonPath("$.monthlyInstallments['month 1']").value(503.13))
                .andExpect(jsonPath("$.monthlyInstallments['month 2']").value(503.13));

        assertEquals(1, loanCalculationRepository.count());
    }

    @Test
    @DisplayName("Calculate Loan - Invalid Amount Input")
    void testCalculateLoan_InvalidInput() throws Exception {
        LoanCalculationRequestDTO requestDTO = createLoanCalculationRequestDTO("0", "1", 2);

        mockMvc.perform(post("/api/v1/loan-calculations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("amount - must be greater than 0;"));
    }

    private LoanCalculationRequestDTO createLoanCalculationRequestDTO(String amount, String annualInterestPercent, int numberOfMonths) {
        LoanCalculationRequestDTO requestDTO = new LoanCalculationRequestDTO();
        requestDTO.setAmount(new BigDecimal(amount));
        requestDTO.setAnnualInterestPercent(new BigDecimal(annualInterestPercent));
        requestDTO.setNumberOfMonths(numberOfMonths);
        return requestDTO;
    }
}

