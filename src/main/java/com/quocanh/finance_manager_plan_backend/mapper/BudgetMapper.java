package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.BudgetDTO;
import com.quocanh.finance_manager_plan_backend.entity.Budgets;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "categoryId", source = "category.id")
    BudgetDTO toDTO(Budgets budget);
    
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "category.id", source = "categoryId")
    Budgets toEntity(BudgetDTO budgetDTO);
} 