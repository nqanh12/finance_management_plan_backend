package com.quocanh.finance_manager_plan_backend.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "savings_goals")
public class SavingsGoals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @Column(name = "target_amount", nullable = false, precision = 15, scale = 2)
    BigDecimal targetAmount;

    @Column(name = "current_amount", nullable = false, precision = 15, scale = 2)
    BigDecimal currentAmount = BigDecimal.ZERO;

    @Column(name = "monthly_contribution", nullable = false, precision = 15, scale = 2)
    BigDecimal monthlyContribution;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    Date endDate;

    @Column(name = "goal_name", nullable = false, length = 100)
    String goalName;

    @Column(name = "status", nullable = false, length = 20)
    String status = "in_progress";
} 