package com.quocanh.finance_manager_plan_backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BudgetDTO {
     Long id;
     Long userId;
     Long categoryId;
     BigDecimal limitAmount;
     Date startDate;
     Date endDate;
}
