package com.quocanh.finance_manager_plan_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BudgetRequest {
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @NotNull(message = "Limit amount is required")
    @Positive(message = "Limit amount must be positive")
    private BigDecimal limitAmount;
    
    @NotNull(message = "Start date is required")
    private Date startDate;
    
    @NotNull(message = "End date is required")
    private Date endDate;
} 