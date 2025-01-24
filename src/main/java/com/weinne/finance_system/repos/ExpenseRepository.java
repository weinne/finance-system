package com.weinne.finance_system.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import com.weinne.finance_system.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Métodos adicionais, se necessário
}