package com.quocanh.finance_manager_plan_backend.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {
    EMAIL_SEND_FAILED(1010, "Email send failed", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_NOT_FOUND(1005, "Email not found", HttpStatus.BAD_REQUEST),
    Email_EXISTED(1001, "Email existed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1002, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_KEY(1003, "Invalid key", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1004, "Unauthorized", HttpStatus.FORBIDDEN),
    AUTHENTICATION_FAILED(1006, "Authentication failed", HttpStatus.UNAUTHORIZED),
    TOKEN_PARSING_FAILED(1007, "Token parsing failed", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1008, "Token invalid", HttpStatus.UNAUTHORIZED),
    TOKEN_GENERATION_FAILED(1009, "Token generation failed", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1011, "Invalid token", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1012, "Email already exists", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1013, "Token expired", HttpStatus.UNAUTHORIZED),
    WEAK_PASSWORD(1014, "Weak password", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(1015, "Invalid email format", HttpStatus.BAD_REQUEST),;
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
    int code;
    String message;
    HttpStatusCode httpStatusCode;

}

