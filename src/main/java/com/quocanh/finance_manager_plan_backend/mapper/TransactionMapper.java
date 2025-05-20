package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.TransactionDTO;
import com.quocanh.finance_manager_plan_backend.entity.Transactions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
}
