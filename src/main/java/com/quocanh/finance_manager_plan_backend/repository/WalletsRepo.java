package com.quocanh.finance_manager_plan_backend.repository;

import com.quocanh.finance_manager_plan_backend.entity.Users;
import com.quocanh.finance_manager_plan_backend.entity.Wallets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletsRepo extends JpaRepository<Wallets, Long> {
    List<Wallets> findByUser(Users user);
} 