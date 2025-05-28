package com.quocanh.finance_manager_plan_backend.controller;

import com.quocanh.finance_manager_plan_backend.dto.request.SavingsGoalRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.SavingsGoalResponse;
import com.quocanh.finance_manager_plan_backend.service.SavingsGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/savings-goals")
@RequiredArgsConstructor
public class SavingsGoalController {
    private final SavingsGoalService savingsGoalService;

    @PostMapping
    public ResponseEntity<SavingsGoalResponse> createSavingsGoal(@RequestBody SavingsGoalRequest request) {
        return ResponseEntity.ok(savingsGoalService.createSavingsGoal(request));
    }

    @GetMapping
    public ResponseEntity<List<SavingsGoalResponse>> getUserSavingsGoals() {
        return ResponseEntity.ok(savingsGoalService.getUserSavingsGoals());
    }

    @GetMapping("/{goalId}/progress")
    public ResponseEntity<SavingsGoalResponse> getSavingsGoalProgress(@PathVariable Long goalId) {
        return ResponseEntity.ok(savingsGoalService.getSavingsGoalProgress(goalId));
    }

    @PostMapping("/{goalId}/update-progress")
    public ResponseEntity<SavingsGoalResponse> updateProgress(
            @PathVariable Long goalId,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(savingsGoalService.updateProgress(goalId, amount));
    }
} 