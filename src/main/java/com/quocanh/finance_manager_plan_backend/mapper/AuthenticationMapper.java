package com.quocanh.finance_manager_plan_backend.mapper;

import com.quocanh.finance_manager_plan_backend.dto.request.UserDTO;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.AuthenticationRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.IntrospectRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.LogoutRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.TokenRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.auth.AuthenticationResponse;
import com.quocanh.finance_manager_plan_backend.dto.response.auth.IntrospectResponse;
import com.quocanh.finance_manager_plan_backend.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    // Map UserDTO to AuthenticationResponse
    AuthenticationResponse userToAuthResponse(UserDTO userDTO, String token, boolean authenticated, Date expiresAt);
    
    // Map Users entity to AuthenticationResponse
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "authenticated", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(source = "role", target = "role", defaultExpression = "java(user.getRole().toString())")
    AuthenticationResponse userToAuthResponse(Users user);
    
    // These are just pass-through methods to maintain compatibility
    AuthenticationRequest toAuthRequest(AuthenticationRequest request);
    TokenRequest toTokenRequest(TokenRequest request);
    LogoutRequest toLogoutRequest(LogoutRequest request);
    IntrospectRequest toIntrospectRequest(IntrospectRequest request);
    IntrospectResponse toIntrospectResponse(IntrospectResponse response);
} 