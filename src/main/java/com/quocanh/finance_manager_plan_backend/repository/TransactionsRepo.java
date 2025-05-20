package com.quocanh.finance_manager_plan_backend.repository;

import com.quocanh.finance_manager_plan_backend.entity.Categories;
import com.quocanh.finance_manager_plan_backend.entity.Transactions;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import com.quocanh.finance_manager_plan_backend.entity.Wallets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionsRepo extends JpaRepository<Transactions, Long> {
    List<Transactions> findByUser(Users user);
    List<Transactions> findByWallet(Wallets wallet);
    List<Transactions> findByCategory(Categories category);
    List<Transactions> findByUserAndDateBetween(Users user, Date startDate, Date endDate);
    List<Transactions> findByUserAndType(Users user, String type);
} 