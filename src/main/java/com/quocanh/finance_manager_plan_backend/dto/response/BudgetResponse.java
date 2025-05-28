package com.quocanh.finance_manager_plan_backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BudgetResponse {
    private Long id;
    private String categoryName;
    private BigDecimal limitAmount;
    private BigDecimal currentSpent;
    private Date startDate;
    private Date endDate;
    private String userName;
    private String status; // "on_track", "exceeded", "completed"
} 