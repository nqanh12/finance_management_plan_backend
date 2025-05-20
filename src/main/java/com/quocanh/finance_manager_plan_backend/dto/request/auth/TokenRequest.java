package com.quocanh.finance_manager_plan_backend.dto.request.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    String token;
}
