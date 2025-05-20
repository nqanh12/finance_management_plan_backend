package com.quocanh.finance_manager_plan_backend.controller;
import com.nimbusds.jose.JOSEException;
import com.quocanh.finance_manager_plan_backend.dto.ApiResponse;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.AuthenticationRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.IntrospectRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.LogoutRequest;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.TokenRequest;
import com.quocanh.finance_manager_plan_backend.dto.response.auth.AuthenticationResponse;
import com.quocanh.finance_manager_plan_backend.dto.response.auth.IntrospectResponse;
import com.quocanh.finance_manager_plan_backend.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/zenithlife_auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .timestamp(new Date())
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody TokenRequest request) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .timestamp(new Date())
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .timestamp(new Date())
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .timestamp(new Date())
                .build();
    }
}
