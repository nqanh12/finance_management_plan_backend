package com.quocanh.finance_manager_plan_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionRequest {
    @NotNull(message = "Wallet ID is required")
    private Long walletId;
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotNull(message = "Type is required")
    private String type; // "income" or "expense"
    
    private Date date;
    
    private String note;
} 