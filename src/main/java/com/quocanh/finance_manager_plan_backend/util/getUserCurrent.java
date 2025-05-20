package com.quocanh.finance_manager_plan_backend.util;

import com.quocanh.finance_manager_plan_backend.entity.Users;
import com.quocanh.finance_manager_plan_backend.exception.AppException;
import com.quocanh.finance_manager_plan_backend.exception.ErrorCode;
import com.quocanh.finance_manager_plan_backend.repository.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class getUserCurrent {
    private final UsersRepo userRepo;

    public Users getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));
    }
}