package com.quocanh.finance_manager_plan_backend.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    String email;

    String password;

    @NotBlank(message = "OTP is mandatory")
    @Pattern(regexp = "\\d{6}", message = "OTP must be a 6-digit number")
    String otp;
}
