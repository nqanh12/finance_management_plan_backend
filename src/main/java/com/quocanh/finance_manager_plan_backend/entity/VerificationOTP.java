package com.quocanh.finance_manager_plan_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification_otp")
public class VerificationOTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 6)
    String otp;

    @Column(nullable = false, length = 100)
    String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = false, updatable = false)
    Date expiryDate;
}
