package com.quocanh.finance_manager_plan_backend.repository;

import com.quocanh.finance_manager_plan_backend.entity.FixedExpenses;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FixedExpensesRepo extends JpaRepository<FixedExpenses, Long> {
    List<FixedExpenses> findByUser(Users user);
    List<FixedExpenses> findByUserAndDueDateBefore(Users user, Date date);
    List<FixedExpenses> findByUserAndDueDateBetween(Users user, Date startDate, Date endDate);
} 