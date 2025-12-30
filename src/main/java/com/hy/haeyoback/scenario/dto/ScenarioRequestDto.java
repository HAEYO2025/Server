package com.hy.haeyoback.scenario.dto;

import com.hy.haeyoback.scenario.entity.Scenario;
import com.hy.haeyoback.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ScenarioRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "상황 설명은 필수입니다.")
    @Size(min = 10, message = "상황 설명은 최소 10자 이상이어야 합니다.")
    private String description;
    
    private String address;
    
    private Double latitude;

    private Double longitude;

    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalDateTime startTime;

    public Scenario toEntity(User user) {
        return Scenario.builder()
                .title(this.title)
                .description(this.description)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .startTime(this.startTime)
                .user(user)
                .build();
    }
}
