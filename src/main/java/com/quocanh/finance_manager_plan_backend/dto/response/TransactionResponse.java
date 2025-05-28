package com.quocanh.finance_manager_plan_backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionResponse {
    private Long id;
    private String categoryName;
    private String walletName;
    private BigDecimal amount;
    private String type;
    private Date date;
    private String note;
    private String userName;
} 