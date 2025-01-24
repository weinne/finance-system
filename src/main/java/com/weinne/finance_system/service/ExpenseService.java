package com.weinne.finance_system.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weinne.finance_system.config.TenantContext;
import com.weinne.finance_system.model.Expense;
import com.weinne.finance_system.repos.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Transactional
    public Expense createExpense(Expense expense, String tenantId) {
        // Define o contexto do tenant
        TenantContext.setCurrentTenant(tenantId);

        // Salva a despesa
        Expense savedExpense = expenseRepository.save(expense);

        // Limpa o contexto do tenant
        TenantContext.clear();

        return savedExpense;
    }
}
