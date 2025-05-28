package com.quocanh.finance_manager_plan_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SavingsGoalRequest {
    @NotNull(message = "Goal name is required")
    @Size(min = 3, max = 100, message = "Goal name must be between 3 and 100 characters")
    private String goalName;
    
    @NotNull(message = "Target amount is required")
    @Positive(message = "Target amount must be positive")
    private BigDecimal targetAmount;
    
    @NotNull(message = "Start date is required")
    private Date startDate;
    
    @NotNull(message = "End date is required")
    private Date endDate;
    
    private BigDecimal monthlyContribution;
} 