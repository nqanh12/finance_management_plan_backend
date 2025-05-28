package com.quocanh.finance_manager_plan_backend.controller;

import com.quocanh.finance_manager_plan_backend.dto.response.FinancialSummaryResponse;
import com.quocanh.finance_manager_plan_backend.service.FinancialSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/financial-summary")
@RequiredArgsConstructor
public class FinancialSummaryController {
    private final FinancialSummaryService financialSummaryService;

    @GetMapping
    public ResponseEntity<FinancialSummaryResponse> getFinancialSummary() {
        return ResponseEntity.ok(financialSummaryService.getFinancialSummary());
    }
}