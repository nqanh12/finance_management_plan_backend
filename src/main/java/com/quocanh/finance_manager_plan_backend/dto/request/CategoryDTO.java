package com.quocanh.finance_manager_plan_backend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {
     Long id;
     String name;
     String type;
     Long userId;
}
