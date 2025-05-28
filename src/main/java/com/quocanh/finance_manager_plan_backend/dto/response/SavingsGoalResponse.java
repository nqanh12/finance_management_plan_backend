package com.quocanh.finance_manager_plan_backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SavingsGoalResponse {
    private Long id;
    private String goalName;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private BigDecimal monthlyContribution;
    private Date startDate;
    private Date endDate;
    private String status;
    private Integer monthsRemaining;
    private String userName;
} 