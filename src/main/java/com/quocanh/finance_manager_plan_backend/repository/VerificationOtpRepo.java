package com.quocanh.finance_manager_plan_backend.repository;

import com.quocanh.finance_manager_plan_backend.entity.VerificationOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationOtpRepo extends JpaRepository<VerificationOTP,Long> {
    Optional<VerificationOTP> findByOtp(String otp);
    Optional<VerificationOTP> findByEmail(String email);
    void deleteAllByExpiryDateBefore(Date expiryDate);
    void deleteAllByEmail(String email);
    List<VerificationOTP> findAllByExpiryDateBefore(Date expiryDate);
}
