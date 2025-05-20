package com.quocanh.finance_manager_plan_backend.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDTO {
    private Long id;
    private Long userId;
    private Long walletId;
    private Long categoryId;
    private BigDecimal amount;
    private String type;
    private Date date;
    private String note;
} 