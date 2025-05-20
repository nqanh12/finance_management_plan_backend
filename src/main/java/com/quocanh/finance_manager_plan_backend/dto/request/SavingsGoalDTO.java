package com.quocanh.finance_manager_plan_backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SavingsGoalDTO {
     Long id;
     Long userId;
     BigDecimal targetAmount;
     BigDecimal currentAmount;
     Date startDate;
     Date endDate;
     String goalName;
     String status;
}
