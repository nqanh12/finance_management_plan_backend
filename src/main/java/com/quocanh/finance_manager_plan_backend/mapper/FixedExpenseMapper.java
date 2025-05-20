package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.FixedExpenseDTO;
import com.quocanh.finance_manager_plan_backend.entity.FixedExpenses;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FixedExpenseMapper {
    @Mapping(target = "userId", source = "user.id")
    FixedExpenseDTO toDTO(FixedExpenses fixedExpense);

    @Mapping(target = "user.id", source = "userId")
    FixedExpenses toEntity(FixedExpenseDTO fixedExpenseDTO);
}
