package com.hy.haeyoback.global.constants;

public final class JwtConstants {

    private JwtConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // JWT Claim Names
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_TOKEN_TYPE = "typ";

    // JWT Token Types
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    // Roles
    public static final String ROLE_USER = "ROLE_USER";

    // Error Messages
    public static final String ERROR_INVALID_SUBJECT = "Invalid subject";
    public static final String ERROR_INVALID_TOKEN_TYPE = "Invalid token type";
}
