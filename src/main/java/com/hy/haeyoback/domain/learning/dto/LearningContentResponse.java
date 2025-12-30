package com.hy.haeyoback.domain.learning.dto;

import com.hy.haeyoback.domain.learning.entity.LearningContent;
import java.time.LocalDateTime;

public class LearningContentResponse {

    private final Long id;
    private final String title;
    private final String summary;
    private final String category;
    private final String thumbnailUrl;
    private final LocalDateTime createdAt;

    public LearningContentResponse(Long id, String title, String summary, String category, String thumbnailUrl, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

    public static LearningContentResponse from(LearningContent content) {
        return new LearningContentResponse(
                content.getId(),
                content.getTitle(),
                content.getSummary(),
                content.getCategory(),
                content.getThumbnailUrl(),
                content.getCreatedAt()
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

    public String getCategory() {
        return category;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
