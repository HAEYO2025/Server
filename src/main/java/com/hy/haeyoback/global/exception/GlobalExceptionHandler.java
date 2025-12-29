package com.hy.haeyoback.global.exception;

import com.hy.haeyoback.global.api.ApiResponse;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse<Void> body = ApiResponse.failure(errorCode.name(), ex.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.VALIDATION_ERROR.name(), message);
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.UNAUTHORIZED.name(), ex.getMessage());
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatus()).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.FORBIDDEN.name(), ex.getMessage());
        return ResponseEntity.status(ErrorCode.FORBIDDEN.getStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.INTERNAL_ERROR.name(), ex.getMessage());
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(body);
    }
}
