package com.quocanh.finance_manager_plan_backend.repository;

import com.quocanh.finance_manager_plan_backend.entity.InvalidatedOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedOTPRepo extends JpaRepository<InvalidatedOTP, String> {
}
