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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Scenario", description = "시나리오 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scenarios")
public class ScenarioController {

    private final ScenarioService scenarioService;
    private final UserRepository userRepository;

    @Operation(summary = "시나리오 생성", description = "새로운 시나리오를 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping
    public ResponseEntity<Void> createScenario(
            @Valid @RequestBody ScenarioRequestDto requestDto,
            @AuthenticationPrincipal JwtUserDetails userDetails
    ) {
        User user = getUserFromUserDetails(userDetails);
        Long scenarioId = scenarioService.createScenario(requestDto, user);
        return ResponseEntity.created(URI.create("/api/scenarios/" + scenarioId)).build();
    }

    @Operation(summary = "시나리오 목록 조회", description = "모든 시나리오 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ApiResponse<List<ScenarioResponseDto>> getAllScenarios() {
        return ApiResponse.success(scenarioService.getAllScenarios());
    }

    @Operation(summary = "시나리오 상세 조회", description = "ID로 특정 시나리오를 상세 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ApiResponse<ScenarioResponseDto> getScenario(
            @Parameter(description = "시나리오 ID") @PathVariable Long id
    ) {
        return ApiResponse.success(scenarioService.getScenario(id));
    }

    @Operation(summary = "시나리오 수정", description = "기존 시나리오를 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ApiResponse<Long> updateScenario(
            @Parameter(description = "시나리오 ID") @PathVariable Long id,
            @Valid @RequestBody ScenarioRequestDto requestDto,
            @AuthenticationPrincipal JwtUserDetails userDetails
    ) {
        User user = getUserFromUserDetails(userDetails);
        return ApiResponse.success(scenarioService.updateScenario(id, requestDto, user));
    }

    @Operation(summary = "시나리오 삭제", description = "시나리오를 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScenario(
            @Parameter(description = "시나리오 ID") @PathVariable Long id,
            @AuthenticationPrincipal JwtUserDetails userDetails
    ) {
        User user = getUserFromUserDetails(userDetails);
        scenarioService.deleteScenario(id, user);
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
