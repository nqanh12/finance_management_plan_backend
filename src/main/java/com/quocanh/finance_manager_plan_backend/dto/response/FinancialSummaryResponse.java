package com.quocanh.finance_manager_plan_backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class FinancialSummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netSavings;
    private BigDecimal salary;
    private BigDecimal fixedExpenses;
    private Map<String, BigDecimal> categoryExpenses;
    private List<BudgetStatus> budgetStatuses;
    private List<SavingsGoalProgress> savingsGoals;
    
    @Data
    public static class BudgetStatus {
        private String categoryName;
        private BigDecimal limitAmount;
        private BigDecimal currentSpent;
        private String status;
    }
    
    @Data
    public static class SavingsGoalProgress {
        private String goalName;
        private BigDecimal targetAmount;
        private BigDecimal currentAmount;
        private Integer monthsRemaining;
        private String status;
    }
} 