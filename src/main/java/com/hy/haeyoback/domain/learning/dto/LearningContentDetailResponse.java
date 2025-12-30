package com.hy.haeyoback.domain.learning.dto;

import com.hy.haeyoback.domain.learning.entity.LearningContent;
import java.time.LocalDateTime;

public class LearningContentDetailResponse {

    private final Long id;
    private final String title;
    private final String summary;
    private final String category;
    private final String thumbnailUrl;
    private final String content;
    private final LocalDateTime createdAt;

    public LearningContentDetailResponse(
            Long id,
            String title,
            String summary,
            String category,
            String thumbnailUrl,
            String content,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static LearningContentDetailResponse from(LearningContent content) {
        return new LearningContentDetailResponse(
                content.getId(),
                content.getTitle(),
                content.getSummary(),
                content.getCategory(),
                content.getThumbnailUrl(),
                content.getContent(),
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

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
