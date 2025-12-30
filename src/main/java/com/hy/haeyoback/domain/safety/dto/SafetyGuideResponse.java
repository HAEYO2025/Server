package com.hy.haeyoback.domain.safety.dto;

import com.hy.haeyoback.domain.safety.entity.SafetyGuide;
import com.hy.haeyoback.domain.safety.entity.SafetySituation;

import java.time.LocalDateTime;

public class SafetyGuideResponse {
    private final Long id;
    private final String title;
    private final String summary;
    private final SafetySituation situation;
    private final String category;
    private final Integer priority;
    private final String thumbnailUrl;
    private final LocalDateTime createdAt;

    public SafetyGuideResponse(Long id, String title, String summary, SafetySituation situation,
                               String category, Integer priority, String thumbnailUrl, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.situation = situation;
        this.category = category;
        this.priority = priority;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

    public static SafetyGuideResponse from(SafetyGuide guide) {
        return new SafetyGuideResponse(
                guide.getId(),
                guide.getTitle(),
                guide.getSummary(),
                guide.getSituation(),
                guide.getCategory(),
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
