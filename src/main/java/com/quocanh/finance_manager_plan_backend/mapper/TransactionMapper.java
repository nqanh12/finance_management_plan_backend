package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.TransactionDTO;
import com.quocanh.finance_manager_plan_backend.dto.request.TransactionRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.TransactionResponse;
import com.quocanh.finance_manager_plan_backend.entity.Transactions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "walletId", source = "wallet.id")
    @Mapping(target = "categoryId", source = "category.id")
    TransactionDTO toDTO(Transactions transaction);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "wallet.id", source = "walletId")
    @Mapping(target = "category.id", source = "categoryId")
    Transactions toEntity(TransactionDTO transactionDTO);
    
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "walletName", source = "wallet.name")
    @Mapping(target = "userName", source = "user.fullName")
    TransactionResponse toResponse(Transactions transaction);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "date", expression = "java(new java.util.Date())")
    void updateEntityFromRequest(TransactionRequest request, @MappingTarget Transactions transaction);
}
