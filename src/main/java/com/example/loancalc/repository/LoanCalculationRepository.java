package com.example.loancalc.repository;

import com.example.loancalc.model.LoanCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanCalculationRepository extends JpaRepository<LoanCalculation, Long> {
}
