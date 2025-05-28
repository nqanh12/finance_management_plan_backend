package com.quocanh.finance_manager_plan_backend.service;

import com.quocanh.finance_manager_plan_backend.dto.request.TransactionRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.TransactionResponse;
import com.quocanh.finance_manager_plan_backend.entity.Transactions;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import com.quocanh.finance_manager_plan_backend.entity.Wallets;
import com.quocanh.finance_manager_plan_backend.exception.AppException;
import com.quocanh.finance_manager_plan_backend.exception.ErrorCode;
import com.quocanh.finance_manager_plan_backend.mapper.TransactionMapper;
import com.quocanh.finance_manager_plan_backend.repository.TransactionsRepo;
import com.quocanh.finance_manager_plan_backend.repository.WalletsRepo;
import com.quocanh.finance_manager_plan_backend.util.getUserCurrent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionsRepo transactionsRepo;
    private final WalletsRepo walletsRepo;
    private final TransactionMapper transactionMapper;
    private final getUserCurrent getUserCurrent;

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        Users currentUser = getUserCurrent.getCurrentUser();
        Wallets wallet = walletsRepo.findById(request.getWalletId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        // Validate wallet belongs to user
        if (!wallet.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Transactions transaction = new Transactions();
        transactionMapper.updateEntityFromRequest(request, transaction);
        transaction.setUser(currentUser);
        transaction.setWallet(wallet);
        transaction.setDate(request.getDate() != null ? request.getDate() : new Date());

        // Update wallet balance
        if ("expense".equals(request.getType())) {
            if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                throw new AppException(ErrorCode.INVALID_KEY);
            }
            wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        } else {
            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        }
        walletsRepo.save(wallet);

        return transactionMapper.toResponse(transactionsRepo.save(transaction));
    }

    public List<TransactionResponse> getUserTransactions() {
        Users currentUser = getUserCurrent.getCurrentUser();
        return transactionsRepo.findByUser(currentUser).stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByDateRange(Date startDate, Date endDate) {
        Users currentUser = getUserCurrent.getCurrentUser();
        return transactionsRepo.findByUserAndDateBetween(currentUser, startDate, endDate).stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByType(String type) {
        Users currentUser = getUserCurrent.getCurrentUser();
        return transactionsRepo.findByUserAndType(currentUser, type).stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }
}