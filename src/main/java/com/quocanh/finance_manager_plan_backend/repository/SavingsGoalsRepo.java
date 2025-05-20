package com.quocanh.finance_manager_plan_backend.repository;

import com.quocanh.finance_manager_plan_backend.entity.SavingsGoals;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalsRepo extends JpaRepository<SavingsGoals, Long> {
    List<SavingsGoals> findByUser(Users user);
    List<SavingsGoals> findByUserAndStatus(Users user, String status);
} 