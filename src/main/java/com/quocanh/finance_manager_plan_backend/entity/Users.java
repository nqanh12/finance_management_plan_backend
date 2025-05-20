package com.quocanh.finance_manager_plan_backend.entity;

import com.quocanh.finance_manager_plan_backend.enums.UserRoleEnum;
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
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    String fullName;

    @Column(unique = true, nullable = false, length = 100)
    String email;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    String passwordHash;

    @Enumerated(EnumType.STRING)
    UserRoleEnum role;

    @Column(name = "salary", precision = 15, scale = 2)
    BigDecimal salary = BigDecimal.ZERO;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    Date createdAt = new Date();

}
