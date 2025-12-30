package com.hy.haeyoback.global.exception;

import com.hy.haeyoback.global.api.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.sql.SQLException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleExpiredJwtException(ExpiredJwtException ex) {
        logger.warn("JWT token expired: {}", ex.getMessage());
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.UNAUTHORIZED.name(), "Token has expired");
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatus()).body(body);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleMalformedJwtException(MalformedJwtException ex) {
        logger.warn("Malformed JWT token: {}", ex.getMessage());
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.UNAUTHORIZED.name(), "Invalid token format");
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatus()).body(body);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<Void>> handleSignatureException(SignatureException ex) {
        logger.warn("JWT signature validation failed: {}", ex.getMessage());
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.UNAUTHORIZED.name(), "Invalid token signature");
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatus()).body(body);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        logger.warn("Unsupported JWT token: {}", ex.getMessage());
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.UNAUTHORIZED.name(), "Unsupported token type");
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatus()).body(body);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleJwtException(JwtException ex) {
        logger.warn("JWT error: {}", ex.getMessage());
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.UNAUTHORIZED.name(), "Invalid token");
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatus()).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.warn("Data integrity violation: {}", ex.getMessage());
        String message = "Data integrity violation";
        if (isDuplicateKey(ex.getRootCause())) {
            message = "Duplicate entry detected";
        }
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.VALIDATION_ERROR.name(), message);
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus()).body(body);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataAccessException(DataAccessException ex) {
        logger.error("Database error: {}", ex.getMessage(), ex);
        ApiResponse<Void> body = ApiResponse.failure(ErrorCode.INTERNAL_ERROR.name(), "Database operation failed");
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        logger.error("Unhandled exception", ex);
        ApiResponse<Void> body = ApiResponse.failure(
                ErrorCode.INTERNAL_ERROR.name(),
                ErrorCode.INTERNAL_ERROR.getDefaultMessage()
        );
        return ResponseEntity.status(ErrorCode.INTERNAL_ERROR.getStatus()).body(body);
    }

    private boolean isDuplicateKey(Throwable cause) {
        if (!(cause instanceof SQLException sqlException)) {
            return false;
        }
        String sqlState = sqlException.getSQLState();
        return "23505".equals(sqlState);
    }
}
