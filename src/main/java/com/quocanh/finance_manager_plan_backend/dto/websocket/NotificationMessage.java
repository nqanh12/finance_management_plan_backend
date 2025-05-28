package com.quocanh.finance_manager_plan_backend.dto.websocket;

import lombok.Data;
import java.util.Date;

@Data
public class NotificationMessage {
    private String type; // "BUDGET_ALERT", "SAVINGS_GOAL_UPDATE", "TRANSACTION_ADDED"
    private String message;
    private Date timestamp;
    private Object data;
} 