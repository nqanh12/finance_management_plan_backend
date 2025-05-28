package com.quocanh.finance_manager_plan_backend.service;

import com.quocanh.finance_manager_plan_backend.dto.request.SavingsGoalRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.SavingsGoalResponse;
import com.quocanh.finance_manager_plan_backend.entity.SavingsGoals;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import com.quocanh.finance_manager_plan_backend.exception.AppException;
import com.quocanh.finance_manager_plan_backend.exception.ErrorCode;
import com.quocanh.finance_manager_plan_backend.mapper.SavingsGoalMapper;
import com.quocanh.finance_manager_plan_backend.repository.SavingsGoalsRepo;
import com.quocanh.finance_manager_plan_backend.util.getUserCurrent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingsGoalService {
    private final SavingsGoalsRepo savingsGoalsRepo;
    private final SavingsGoalMapper savingsGoalMapper;
    private final getUserCurrent getUserCurrent;

    @Transactional
    public SavingsGoalResponse createSavingsGoal(SavingsGoalRequest request) {
        Users currentUser = getUserCurrent.getCurrentUser();

        // Validate dates
        if (request.getStartDate().after(request.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        SavingsGoals savingsGoal = new SavingsGoals();
        savingsGoal.setUser(currentUser);
        savingsGoal.setGoalName(request.getGoalName());
        savingsGoal.setTargetAmount(request.getTargetAmount());
        savingsGoal.setCurrentAmount(BigDecimal.ZERO);
        savingsGoal.setStartDate(request.getStartDate());
        savingsGoal.setEndDate(request.getEndDate());
        savingsGoal.setMonthlyContribution(request.getMonthlyContribution());
        savingsGoal.setStatus("in_progress");

        return savingsGoalMapper.toResponse(savingsGoalsRepo.save(savingsGoal));
    }

    public List<SavingsGoalResponse> getUserSavingsGoals() {
        Users currentUser = getUserCurrent.getCurrentUser();
        return savingsGoalsRepo.findByUser(currentUser).stream()
                .map(savingsGoalMapper::toResponse)
                .collect(Collectors.toList());
    }

    public SavingsGoalResponse getSavingsGoalProgress(Long goalId) {
        Users currentUser = getUserCurrent.getCurrentUser();
        SavingsGoals savingsGoal = savingsGoalsRepo.findById(goalId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        if (!savingsGoal.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        SavingsGoalResponse response = savingsGoalMapper.toResponse(savingsGoal);
        response.setMonthsRemaining((int) calculateMonthsRemaining(savingsGoal));
        response.setStatus(determineGoalStatus(savingsGoal));

        return response;
    }

    @Transactional
    public SavingsGoalResponse updateProgress(Long goalId, BigDecimal amount) {
        Users currentUser = getUserCurrent.getCurrentUser();
        SavingsGoals savingsGoal = savingsGoalsRepo.findById(goalId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));

        if (!savingsGoal.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        savingsGoal.setCurrentAmount(savingsGoal.getCurrentAmount().add(amount));
        savingsGoal.setStatus(determineGoalStatus(savingsGoal));
        
        return savingsGoalMapper.toResponse(savingsGoalsRepo.save(savingsGoal));
    }

    private long calculateMonthsRemaining(SavingsGoals goal) {
        LocalDate now = LocalDate.now();
        LocalDate endDate = goal.getEndDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return ChronoUnit.MONTHS.between(now, endDate);
    }

    private String determineGoalStatus(SavingsGoals goal) {
        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            return "completed";
        }

        long monthsRemaining = calculateMonthsRemaining(goal);
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
}