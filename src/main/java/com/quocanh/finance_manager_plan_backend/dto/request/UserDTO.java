package com.quocanh.finance_manager_plan_backend.dto.request;

import com.quocanh.finance_manager_plan_backend.enums.UserRoleEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private UserRoleEnum role;
    private BigDecimal salary;
    private Date createdAt;
} 