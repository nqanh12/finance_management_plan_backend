package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.WalletDTO;
import com.quocanh.finance_manager_plan_backend.entity.Wallets;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    @Mapping(target = "userId", source = "user.id")
    com.quocanh.finance_manager_plan_backend.dto.request.WalletDTO toDTO(Wallets wallet);

    @Mapping(target = "user.id", source = "userId")
    Wallets toEntity(WalletDTO walletDTO);
}
