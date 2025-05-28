package com.quocanh.finance_manager_plan_backend.controller;

import com.quocanh.finance_manager_plan_backend.dto.request.BudgetRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.BudgetResponse;
import com.quocanh.finance_manager_plan_backend.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(@RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.createBudget(request));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getUserBudgets() {
        return ResponseEntity.ok(budgetService.getUserBudgets());
    }

    @GetMapping("/{budgetId}/status")
    public ResponseEntity<BudgetResponse> getBudgetStatus(@PathVariable Long budgetId) {
        return ResponseEntity.ok(budgetService.getBudgetStatus(budgetId));
    }
} 