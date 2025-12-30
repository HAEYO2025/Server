package com.hy.haeyoback.safety.dto;

import com.hy.haeyoback.safety.entity.SafetyGuide;
import com.hy.haeyoback.safety.entity.SafetySituation;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SafetyGuideResponse {
    private final Long id;
    private final String title;
    private final String summary;
    private final SafetySituation situation;
    private final String category;
    private final Integer priority;
    private final String thumbnailUrl;
    private final Integer shareCount;
    private final LocalDateTime createdAt;

    private SafetyGuideResponse(Long id, String title, String summary, SafetySituation situation,
                               String category, Integer priority, String thumbnailUrl, Integer shareCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.situation = situation;
        this.category = category;
        this.priority = priority;
        this.thumbnailUrl = thumbnailUrl;
        this.shareCount = shareCount;
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
                guide.getShareCount(),
                guide.getCreatedAt()
        );
    }
}
