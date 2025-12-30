package com.hy.haeyoback.domain.scenario.service;

import com.hy.haeyoback.domain.scenario.dto.ScenarioRequestDto;
import com.hy.haeyoback.domain.scenario.dto.ScenarioResponseDto;
import com.hy.haeyoback.domain.scenario.entity.Scenario;
import com.hy.haeyoback.domain.scenario.repository.ScenarioRepository;
import com.hy.haeyoback.domain.user.entity.User;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;

    @Transactional
    public Long createScenario(ScenarioRequestDto requestDto, User user) {
        Scenario scenario = requestDto.toEntity(user);
        Scenario savedScenario = scenarioRepository.save(scenario);
        return savedScenario.getId();
    }

    public List<ScenarioResponseDto> getAllScenarios() {
        return scenarioRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(ScenarioResponseDto::new)
                .collect(Collectors.toList());
    }

    public ScenarioResponseDto getScenario(Long scenarioId) {
        Scenario scenario = findScenarioById(scenarioId);
        return new ScenarioResponseDto(scenario);
    }

    @Transactional
    public Long updateScenario(Long scenarioId, ScenarioRequestDto requestDto, User user) {
        Scenario scenario = findScenarioById(scenarioId);
        validateUserIsOwner(scenario, user);
        scenario.update(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getAddress(),
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getStartTime()
        );
        return scenario.getId();
    }

    @Transactional
    public void deleteScenario(Long scenarioId, User user) {
        Scenario scenario = findScenarioById(scenarioId);
        validateUserIsOwner(scenario, user);
        scenarioRepository.delete(scenario);
    }

    private Scenario findScenarioById(Long scenarioId) {
        return scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "시나리오를 찾을 수 없습니다."));
    }

    private void validateUserIsOwner(Scenario scenario, User user) {
        if (!scenario.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "시나리오에 대한 권한이 없습니다.");
        }
    }
}
