package com.hy.haeyoback.global.api;

import java.time.Instant;

public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String errorCode;
    private final String message;
    private final String timestamp;

    private ApiResponse(boolean success, T data, String errorCode, String message, String timestamp) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null, Instant.now().toString());
    }

    public static <T> ApiResponse<T> successMessage(String message) {
        return new ApiResponse<>(true, null, null, message, Instant.now().toString());
    }

    public static <T> ApiResponse<T> failure(String errorCode, String message) {
        return new ApiResponse<>(false, null, errorCode, message, Instant.now().toString());
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
