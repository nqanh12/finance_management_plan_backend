package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.SavingsGoalDTO;
import com.quocanh.finance_manager_plan_backend.entity.SavingsGoals;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SavingsGoalMapper {
    @Mapping(target = "userId", source = "user.id")
    com.quocanh.finance_manager_plan_backend.dto.request.SavingsGoalDTO toDTO(SavingsGoals savingsGoal);

    @Mapping(target = "user.id", source = "userId")
    SavingsGoals toEntity(SavingsGoalDTO savingsGoalDTO);
}
