package com.quocanh.finance_manager_plan_backend.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletDTO {
    private Long id;
    private String name;
    private BigDecimal balance;
    private Long userId;
} 