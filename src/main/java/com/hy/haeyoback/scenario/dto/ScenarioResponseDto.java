package com.hy.haeyoback.scenario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hy.haeyoback.scenario.entity.Scenario;
import com.hy.haeyoback.scenario.entity.TurnHistory;
import com.hy.haeyoback.user.dto.UserResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScenarioResponseDto {

    private final Long id;
    private final String title;
    private final String description;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final LocalDateTime startTime;
    private final ReportResponse report;
    private final List<TurnHistoryResponse> history;
    private final UserResponse user;
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
        this.report = (scenario.getReport() != null) ? new ReportResponse(scenario.getReport()) : null;
        this.history = scenario.getHistory().stream()
                .map(TurnHistoryResponse::new)
                .toList();
        this.user = (scenario.getUser() != null)
                ? new UserResponse(scenario.getUser().getId(), scenario.getUser().getEmail(), scenario.getUser().getUsername())
                : null;
        this.createdAt = scenario.getCreatedAt();
        this.updatedAt = scenario.getUpdatedAt();
    }

    @Getter
    public static class ReportResponse {
        private final String title;
        private final String description;
        private final Double longitude;
        private final Double latitude;
        @JsonProperty("reported_date")
        private final java.time.LocalDate reportedDate;

        public ReportResponse(com.hy.haeyoback.scenario.entity.Report report) {
            this.title = report.getTitle();
            this.description = report.getDescription();
            this.longitude = report.getLongitude();
            this.latitude = report.getLatitude();
            this.reportedDate = report.getReportedDate();
        }
    }

    @Getter
    public static class TurnHistoryResponse {
        private final String situation;
        private final String choice;
        @JsonProperty("survival_rate")
        private final Double survivalRate;
        private final String comment;

        public TurnHistoryResponse(TurnHistory history) {
            this.situation = history.getSituation();
            this.choice = history.getChoice();
            this.survivalRate = history.getSurvivalRate();
            this.comment = history.getComment();
        }
    }

    @Getter
    public static class ScenarioSummaryResponse {
        private final Long id;
        private final String title;
        private final String description;
        private final String address;
        private final Double latitude;
        private final Double longitude;
        private final LocalDateTime startTime;
        private final ReportResponse report;
        private final UserResponse user;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public ScenarioSummaryResponse(Scenario scenario) {
            this.id = scenario.getId();
            this.title = scenario.getTitle();
            this.description = scenario.getDescription();
            this.address = scenario.getAddress();
            this.latitude = scenario.getLatitude();
            this.longitude = scenario.getLongitude();
            this.startTime = scenario.getStartTime();
            this.report = (scenario.getReport() != null) ? new ReportResponse(scenario.getReport()) : null;
            this.user = (scenario.getUser() != null)
                    ? new UserResponse(scenario.getUser().getId(), scenario.getUser().getEmail(), scenario.getUser().getUsername())
                    : null;
            this.createdAt = scenario.getCreatedAt();
            this.updatedAt = scenario.getUpdatedAt();
        }
    }
}
