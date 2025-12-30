package com.hy.haeyoback.domain.scenario.dto;

import com.hy.haeyoback.domain.scenario.entity.Scenario;
import com.hy.haeyoback.domain.user.dto.UserResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScenarioResponseDto {

    private final Long id;
    private final String title;
    private final String description;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final LocalDateTime startTime;
    private final UserResponse user;
    private final Integer shareCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ScenarioResponseDto(Scenario scenario) {
        this.id = scenario.getId();
        this.title = scenario.getTitle();
        this.description = scenario.getDescription();
        this.address = scenario.getAddress();
        this.latitude = scenario.getLatitude();
        this.longitude = scenario.getLongitude();
        this.startTime = scenario.getStartTime();
        this.user = new UserResponse(scenario.getUser().getId(), scenario.getUser().getEmail());
        this.shareCount = scenario.getShareCount();
        this.createdAt = scenario.getCreatedAt();
        this.updatedAt = scenario.getUpdatedAt();
    }
}
