package com.hy.haeyoback.safety.entity;

public class SafetyWarning {
    private String type;
    private String message;

    public SafetyWarning() {
    }

    public SafetyWarning(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
