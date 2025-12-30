package com.hy.haeyoback.scenario.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.scenario.entity.Scenario;
import com.hy.haeyoback.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScenarioCreateRequestDto {

    @NotNull(message = "시나리오는 필수입니다.")
    @Valid
    private ScenarioPayload scenario;

    @NotNull(message = "제보는 필수입니다.")
    @Valid
    private ReportPayload report;

    @Valid
    private List<TurnHistoryPayload> history = new ArrayList<>();

    public Scenario toEntity(User user) {
        return Scenario.builder()
                .title(scenario.getTitle())
                .description(scenario.getDescription())
                .latitude(report.getLatitude())
                .longitude(report.getLongitude())
                .startTime(parseStartTime(scenario.getStartDate()))
                .user(user)
                .build();
    }

    public java.time.LocalDate parseReportedDate() {
        String value = report.getReportedDate();
        if (value == null || value.isBlank()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "reported_date는 필수입니다.");
        }
        try {
            return java.time.LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "reported_date 형식이 올바르지 않습니다.");
        }
    }

    private LocalDateTime parseStartTime(String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "start_date는 필수입니다.");
        }
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
            try {
                return LocalDate.parse(value).atStartOfDay();
            } catch (DateTimeParseException ex) {
                throw new CustomException(ErrorCode.VALIDATION_ERROR, "start_date 형식이 올바르지 않습니다.");
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ScenarioPayload {

        @NotBlank(message = "시나리오 제목은 필수입니다.")
        private String title;

        @NotBlank(message = "시나리오 설명은 필수입니다.")
        @Size(min = 10, message = "시나리오 설명은 최소 10자 이상이어야 합니다.")
        private String description;

        @NotBlank(message = "start_date는 필수입니다.")
        @JsonProperty("start_date")
        @JsonAlias("startDate")
        private String startDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ReportPayload {

        @NotBlank(message = "제보 제목은 필수입니다.")
        private String title;

        @NotNull(message = "경도는 필수입니다.")
        private Double longitude;

        @NotNull(message = "위도는 필수입니다.")
        private Double latitude;

        @NotBlank(message = "제보 내용은 필수입니다.")
        private String description;

        @NotBlank(message = "reported_date는 필수입니다.")
        @JsonProperty("reported_date")
        @JsonAlias("reportedDate")
        private String reportedDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TurnHistoryPayload {

        @NotBlank(message = "history.situation은 필수입니다.")
        private String situation;

        @NotBlank(message = "history.choice는 필수입니다.")
        private String choice;

        @NotNull(message = "history.survival_rate는 필수입니다.")
        @JsonProperty("survival_rate")
        @JsonAlias("survivalRate")
        private Double survivalRate;

        @NotBlank(message = "history.comment는 필수입니다.")
        private String comment;
    }
}
