package com.quocanh.finance_manager_plan_backend.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invalidated_otp")
public class InvalidatedOTP {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String otp;

    @Temporal(TemporalType.TIMESTAMP)
    Date expiryDate;
}

