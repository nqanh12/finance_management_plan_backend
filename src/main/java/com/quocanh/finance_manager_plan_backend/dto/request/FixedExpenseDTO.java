package com.quocanh.finance_manager_plan_backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FixedExpenseDTO {
     Long id;
     Long userId;
     String name;
     BigDecimal amount;
     Date dueDate;
}
