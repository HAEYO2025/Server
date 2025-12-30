package com.hy.haeyoback.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final int MIN_LENGTH = 8;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        List<String> violations = new ArrayList<>();

        if (value.length() < MIN_LENGTH) {
            violations.add("Password must be at least 8 characters");
        }
        if (!value.matches(".*[A-Za-z].*")) {
            violations.add("Password must include at least one letter");
        }
        if (!value.matches(".*\\d.*")) {
            violations.add("Password must include at least one number");
        }
        if (!value.matches(".*[^A-Za-z0-9].*")) {
            violations.add("Password must include at least one special character");
        }
        if (value.matches(".*\\s.*")) {
            violations.add("Password must not contain whitespace");
        }

        if (violations.isEmpty()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.join("; ", violations))
                .addConstraintViolation();
        return false;
    }
}
