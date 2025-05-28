package com.quocanh.finance_manager_plan_backend.service;

import com.quocanh.finance_manager_plan_backend.dto.request.BudgetRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.BudgetResponse;
import com.quocanh.finance_manager_plan_backend.entity.Budgets;
import com.quocanh.finance_manager_plan_backend.entity.Categories;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import com.quocanh.finance_manager_plan_backend.exception.AppException;
import com.quocanh.finance_manager_plan_backend.exception.ErrorCode;
import com.quocanh.finance_manager_plan_backend.mapper.BudgetMapper;
import com.quocanh.finance_manager_plan_backend.repository.BudgetsRepo;
import com.quocanh.finance_manager_plan_backend.repository.CategoriesRepo;
import com.quocanh.finance_manager_plan_backend.util.getUserCurrent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetsRepo budgetsRepo;
    private final CategoriesRepo categoriesRepo;
    private final BudgetMapper budgetMapper;
    private final getUserCurrent getUserCurrent;

    @Transactional
    public BudgetResponse createBudget(BudgetRequest request) {
        Users currentUser = getUserCurrent.getCurrentUser();
        Categories category = categoriesRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        // Validate category belongs to user
        if (!category.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Check for overlapping budgets
        List<Budgets> existingBudgets = budgetsRepo.findByUserAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                currentUser, request.getStartDate(), request.getEndDate());
        
        if (!existingBudgets.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        Budgets budget = new Budgets();
        budget.setUser(currentUser);
        budget.setCategory(category);
        budget.setLimitAmount(request.getLimitAmount());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());

        return budgetMapper.toResponse(budgetsRepo.save(budget));
    }

    public List<BudgetResponse> getUserBudgets() {
        Users currentUser = getUserCurrent.getCurrentUser();
        return budgetsRepo.findByUser(currentUser).stream()
                .map(budgetMapper::toResponse)
                .collect(Collectors.toList());
    }

    public BudgetResponse getBudgetStatus(Long budgetId) {
        Users currentUser = getUserCurrent.getCurrentUser();
        Budgets budget = budgetsRepo.findById(budgetId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        if (!budget.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Calculate current spent amount
        BigDecimal currentSpent = calculateCurrentSpent(budget);
        BudgetResponse response = budgetMapper.toResponse(budget);
        response.setCurrentSpent(currentSpent);
        response.setStatus(determineBudgetStatus(budget.getLimitAmount(), currentSpent));

        return response;
    }

    private BigDecimal calculateCurrentSpent(Budgets budget) {
        // Implementation to calculate current spent amount
        return BigDecimal.ZERO; // Placeholder
    }

    private String determineBudgetStatus(BigDecimal limitAmount, BigDecimal currentSpent) {
        if (currentSpent.compareTo(limitAmount) >= 0) {
            return "exceeded";
        } else if (currentSpent.compareTo(limitAmount.multiply(new BigDecimal("0.8"))) >= 0) {
            return "warning";
        }
        return "on_track";
    }
} 