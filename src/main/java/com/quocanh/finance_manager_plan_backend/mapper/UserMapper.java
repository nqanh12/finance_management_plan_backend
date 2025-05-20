package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.UserDTO;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(Users user);

    Users toEntity(UserDTO userDTO);
}
