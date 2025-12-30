package com.hy.haeyoback.safety.entity;

public class SafetyStep {
    private int order;
    private String title;
    private String description;

    public SafetyStep() {
    }

    public SafetyStep(int order, String title, String description) {
        this.order = order;
        this.title = title;
        this.description = description;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
