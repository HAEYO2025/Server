package com.hy.haeyoback.scenario.controller;

import com.hy.haeyoback.scenario.dto.ScenarioRequestDto;
import com.hy.haeyoback.scenario.dto.ScenarioResponseDto;
import com.hy.haeyoback.scenario.service.ScenarioService;
import com.hy.haeyoback.user.entity.User;
import com.hy.haeyoback.user.repository.UserRepository;
import com.hy.haeyoback.global.api.ApiResponse;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.global.security.JwtUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scenarios")
public class ScenarioController {

    private final ScenarioService scenarioService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Void> createScenario(
            @Valid @RequestBody ScenarioRequestDto requestDto,
            @AuthenticationPrincipal JwtUserDetails userDetails
    ) {
        User user = getUserFromUserDetails(userDetails);
        Long scenarioId = scenarioService.createScenario(requestDto, user);
        return ResponseEntity.created(URI.create("/api/scenarios/" + scenarioId)).build();
    }
    
    @GetMapping
    public ApiResponse<List<ScenarioResponseDto>> getAllScenarios() {
        return ApiResponse.success(scenarioService.getAllScenarios());
    }

    @GetMapping("/{id}")
    public ApiResponse<ScenarioResponseDto> getScenario(@PathVariable Long id) {
        return ApiResponse.success(scenarioService.getScenario(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> updateScenario(
            @PathVariable Long id,
            @Valid @RequestBody ScenarioRequestDto requestDto,
            @AuthenticationPrincipal JwtUserDetails userDetails
    ) {
        User user = getUserFromUserDetails(userDetails);
        return ApiResponse.success(scenarioService.updateScenario(id, requestDto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScenario(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserDetails userDetails
    ) {
        User user = getUserFromUserDetails(userDetails);
        scenarioService.deleteScenario(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Void> shareScenario(@PathVariable Long id) {
        scenarioService.incrementShareCount(id);
        return ResponseEntity.noContent().build();
    }

    private User getUserFromUserDetails(JwtUserDetails userDetails) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "인증이 필요합니다.");
        }
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
