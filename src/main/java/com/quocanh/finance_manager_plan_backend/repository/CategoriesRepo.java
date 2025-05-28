package com.quocanh.finance_manager_plan_backend.repository;

import com.quocanh.finance_manager_plan_backend.entity.Categories;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepo extends JpaRepository<Categories, Long> {
    List<Categories> findByUser(Users user);
    List<Categories> findByUserAndType(Users user, String type);
} 