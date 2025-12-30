package com.hy.haeyoback.safety.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "safety_guides")
public class SafetyGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SafetySituation situation;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = SafetyStepListConverter.class)
    private List<SafetyStep> steps = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    @Convert(converter = SafetyWarningListConverter.class)
    private List<SafetyWarning> warnings = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    @Convert(converter = EmergencyContactListConverter.class)
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();

    @Column(nullable = false)
    private Integer priority;

    private String thumbnailUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Integer shareCount = 0;

    protected SafetyGuide() {
    }

    public SafetyGuide(String title, String summary, SafetySituation situation, String category,
                       List<SafetyStep> steps, List<SafetyWarning> warnings,
                       List<EmergencyContact> emergencyContacts, Integer priority, String thumbnailUrl) {
        this.title = title;
        this.summary = summary;
        this.situation = situation;
        this.category = category;
        this.steps = steps != null ? steps : new ArrayList<>();
        this.warnings = warnings != null ? warnings : new ArrayList<>();
        this.emergencyContacts = emergencyContacts != null ? emergencyContacts : new ArrayList<>();
        this.priority = priority;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = LocalDateTime.now();
    }

    public void incrementShareCount() {
        this.shareCount++;
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

    public Integer getShareCount() {
        return shareCount;
    }
}
