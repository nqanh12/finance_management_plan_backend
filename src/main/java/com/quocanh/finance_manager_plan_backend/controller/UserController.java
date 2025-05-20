package com.quocanh.finance_manager_plan_backend.controller;

import com.quocanh.finance_manager_plan_backend.dto.ApiResponse;
import com.quocanh.finance_manager_plan_backend.dto.request.UserDTO;
import com.quocanh.finance_manager_plan_backend.dto.request.auth.RegisterRequest;
import com.quocanh.finance_manager_plan_backend.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/zenithlife_user")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {
    UserService userService;

    @PostMapping("/send-otp")
    ApiResponse<Boolean> sendOtp(@RequestBody String email) {
        var result = userService.sendOtp(email);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .timestamp(new Date())
                .build();
    }

    @PostMapping("/verify-otp")
    ApiResponse<Boolean> verifyOtp(@RequestBody RegisterRequest request) {
        var result = userService.verifyOtpForRegistration(request);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .timestamp(new Date())
                .build();
    }

    @PostMapping("/register")
    ApiResponse<UserDTO> register(@RequestBody RegisterRequest request) throws Exception {
        var result = userService.verifyOtpAndSetPassword(request);
        return ApiResponse.<UserDTO>builder()
                .result(result)
                .timestamp(new Date())
                .build();
    }
}
