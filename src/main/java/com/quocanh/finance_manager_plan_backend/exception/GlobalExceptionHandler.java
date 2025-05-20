package com.quocanh.finance_manager_plan_backend.exception;

import com.quocanh.finance_manager_plan_backend.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import java.net.SocketTimeoutException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        ApiResponse response = new ApiResponse();
        response.setCode(1001);
        response.setMessage(ex.getMessage());
        response.setTimestamp(new Date());
        response.getError();
        return ResponseEntity.
                badRequest().
                body(response);
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public ResponseEntity<String> handleAsyncRequestNotUsableException(AsyncRequestNotUsableException ex) {
        // Log the exception
        System.err.println("AsyncRequestNotUsableException: " + ex.getMessage());
        // Return a response entity with an appropriate message and FriendRequestStatusEnum code
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<String> handleSocketTimeoutException(SocketTimeoutException ex) {
        // Log the exception
        System.err.println("SocketTimeoutException: " + ex.getMessage());
        // Return a response entity with an appropriate message and FriendRequestStatusEnum code
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timed out. Please try again later.");
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse response = new ApiResponse();

        response.setCode(errorCode.getCode());
        response.setMessage(ex.getMessage());
        response.setTimestamp(new Date());

        return ResponseEntity.
                status(errorCode.getHttpStatusCode()).
                body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())

                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .timestamp(new Date())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> hanldeValidation (MethodArgumentNotValidException ex) {
        String enumKey = ex.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try{
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        ApiResponse response = new ApiResponse();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        response.setTimestamp(new Date());
        return ResponseEntity.badRequest().body(response);
    }
}

