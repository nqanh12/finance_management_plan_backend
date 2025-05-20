package com.quocanh.finance_manager_plan_backend.dto.response.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    boolean authenticated;
    String role;
    Date expiresAt;
    boolean isUpdateInfo;

}
