package com.hy.haeyoback.domain.safety.dto;

import com.hy.haeyoback.domain.safety.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public class SafetyGuideDetailResponse {
    private final Long id;
    private final String title;
    private final String summary;
    private final SafetySituation situation;
    private final String category;
    private final List<SafetyStep> steps;
    private final List<SafetyWarning> warnings;
    private final List<EmergencyContact> emergencyContacts;
    private final Integer priority;
    private final String thumbnailUrl;
    private final LocalDateTime createdAt;

    public SafetyGuideDetailResponse(Long id, String title, String summary, SafetySituation situation,
                                     String category, List<SafetyStep> steps, List<SafetyWarning> warnings,
                                     List<EmergencyContact> emergencyContacts, Integer priority,
                                     String thumbnailUrl, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.situation = situation;
        this.category = category;
        this.steps = steps;
        this.warnings = warnings;
        this.emergencyContacts = emergencyContacts;
        this.priority = priority;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

    public static SafetyGuideDetailResponse from(SafetyGuide guide) {
        return new SafetyGuideDetailResponse(
                guide.getId(),
                guide.getTitle(),
                guide.getSummary(),
                guide.getSituation(),
                guide.getCategory(),
                guide.getSteps(),
                guide.getWarnings(),
                guide.getEmergencyContacts(),
                guide.getPriority(),
                guide.getThumbnailUrl(),
                guide.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public SafetySituation getSituation() {
        return situation;
    }

    public String getCategory() {
        return category;
    }

    public List<SafetyStep> getSteps() {
        return steps;
    }

    public List<SafetyWarning> getWarnings() {
        return warnings;
    }

    public List<EmergencyContact> getEmergencyContacts() {
        return emergencyContacts;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
