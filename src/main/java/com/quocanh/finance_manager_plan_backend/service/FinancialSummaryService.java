 package com.quocanh.finance_manager_plan_backend.service;

import com.quocanh.finance_manager_plan_backend.dto.response.FinancialSummaryResponse;
import com.quocanh.finance_manager_plan_backend.entity.*;
import com.quocanh.finance_manager_plan_backend.repository.*;
import com.quocanh.finance_manager_plan_backend.util.getUserCurrent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialSummaryService {
    private final TransactionsRepo transactionsRepo;
    private final BudgetsRepo budgetsRepo;
    private final SavingsGoalsRepo savingsGoalsRepo;
    private final FixedExpensesRepo fixedExpensesRepo;
    private final getUserCurrent getUserCurrent;

    public FinancialSummaryResponse getFinancialSummary() {
        Users currentUser = getUserCurrent.getCurrentUser();
        Date now = new Date();

        // Get transactions for current month
        List<Transactions> monthlyTransactions = transactionsRepo.findByUserAndDateBetween(
                currentUser,
                getStartOfMonth(now),
                now
        );

        // Calculate totals
        BigDecimal totalIncome = calculateTotalIncome(monthlyTransactions);
        BigDecimal totalExpense = calculateTotalExpense(monthlyTransactions);
        BigDecimal netSavings = totalIncome.subtract(totalExpense);

        // Get category expenses
        Map<String, BigDecimal> categoryExpenses = calculateCategoryExpenses(monthlyTransactions);

        // Get budget statuses
        List<FinancialSummaryResponse.BudgetStatus> budgetStatuses = getBudgetStatuses(currentUser);

        // Get savings goals progress
        List<FinancialSummaryResponse.SavingsGoalProgress> savingsGoals = getSavingsGoalsProgress(currentUser);

        // Get fixed expenses
        BigDecimal fixedExpenses = calculateFixedExpenses(currentUser);

        FinancialSummaryResponse response = new FinancialSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetSavings(netSavings);
        response.setCategoryExpenses(categoryExpenses);
        response.setBudgetStatuses(budgetStatuses);
        response.setSavingsGoals(savingsGoals);
        response.setFixedExpenses(fixedExpenses);

        return response;
    }

    private BigDecimal calculateTotalIncome(List<Transactions> transactions) {
        return transactions.stream()
                .filter(t -> "income".equals(t.getType()))
                .map(Transactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalExpense(List<Transactions> transactions) {
        return transactions.stream()
                .filter(t -> "expense".equals(t.getType()))
                .map(Transactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, BigDecimal> calculateCategoryExpenses(List<Transactions> transactions) {
        return transactions.stream()
                .filter(t -> "expense".equals(t.getType()))
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transactions::getAmount,
                                BigDecimal::add
                        )
                ));
    }

    private List<FinancialSummaryResponse.BudgetStatus> getBudgetStatuses(Users user) {
        return budgetsRepo.findByUser(user).stream()
                .map(budget -> {
                    FinancialSummaryResponse.BudgetStatus status = new FinancialSummaryResponse.BudgetStatus();
                    status.setCategoryName(budget.getCategory().getName());
                    status.setLimitAmount(budget.getLimitAmount());
                    status.setCurrentSpent(calculateBudgetSpent(budget));
                    status.setStatus(determineBudgetStatus(budget));
                    return status;
                })
                .collect(Collectors.toList());
    }

    private List<FinancialSummaryResponse.SavingsGoalProgress> getSavingsGoalsProgress(Users user) {
        return savingsGoalsRepo.findByUser(user).stream()
                .map(goal -> {
                    FinancialSummaryResponse.SavingsGoalProgress progress = new FinancialSummaryResponse.SavingsGoalProgress();
                    progress.setGoalName(goal.getGoalName());
                    progress.setTargetAmount(goal.getTargetAmount());
                    progress.setCurrentAmount(goal.getCurrentAmount());
                    progress.setMonthsRemaining(calculateMonthsRemaining(goal));
                    progress.setStatus(determineGoalStatus(goal));
                    return progress;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateFixedExpenses(Users user) {
        return fixedExpensesRepo.findByUser(user).stream()
                .map(FixedExpenses::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateBudgetSpent(Budgets budget) {
        return transactionsRepo.findByUserAndCategoryAndDateBetween(
                budget.getUser(),
                budget.getCategory(),
                budget.getStartDate(),
                new Date()
        ).stream()
                .filter(t -> "expense".equals(t.getType()))
                .map(Transactions::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String determineBudgetStatus(Budgets budget) {
        BigDecimal spent = calculateBudgetSpent(budget);
        if (spent.compareTo(budget.getLimitAmount()) >= 0) {
            return "exceeded";
        } else if (spent.compareTo(budget.getLimitAmount().multiply(new BigDecimal("0.8"))) >= 0) {
            return "warning";
        }
        return "on_track";
    }

    private int calculateMonthsRemaining(SavingsGoals goal) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.setTime(goal.getEndDate());
        return (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 +
                end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
    }

    private String determineGoalStatus(SavingsGoals goal) {
        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            return "completed";
        }

        int monthsRemaining = calculateMonthsRemaining(goal);
        if (monthsRemaining <= 0) {
            return "overdue";
        }

        BigDecimal expectedAmount = goal.getMonthlyContribution()
                .multiply(BigDecimal.valueOf(monthsRemaining))
                .add(goal.getCurrentAmount());

        if (expectedAmount.compareTo(goal.getTargetAmount()) < 0) {
            return "at_risk";
        }

        return "on_track";
    }

    private Date getStartOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}