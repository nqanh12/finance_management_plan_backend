package com.quocanh.finance_manager_plan_backend.repository;

import com.quocanh.finance_manager_plan_backend.entity.Budgets;
import com.quocanh.finance_manager_plan_backend.entity.Categories;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BudgetsRepo extends JpaRepository<Budgets, Long> {
    List<Budgets> findByUser(Users user);
    List<Budgets> findByCategory(Categories category);
    List<Budgets> findByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Users user, Date currentDate, Date currentDate2);
} 