package com.weinne.finance_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weinne.finance_system.model.Expense;
import com.weinne.finance_system.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> createExpense(
        @RequestHeader("X-Tenant-ID") String tenantId,
        @RequestBody Expense expense
    ) {
        return ResponseEntity.ok(expenseService.createExpense(expense, tenantId));
    }
}
