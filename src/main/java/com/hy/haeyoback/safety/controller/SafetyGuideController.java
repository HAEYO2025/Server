package com.hy.haeyoback.safety.controller;

import com.hy.haeyoback.safety.dto.SafetyGuideDetailResponse;
import com.hy.haeyoback.safety.dto.SafetyGuideResponse;
import com.hy.haeyoback.safety.entity.SafetySituation;
import com.hy.haeyoback.safety.service.SafetyGuideService;
import com.hy.haeyoback.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Safety Guide", description = "안전 가이드 관련 API")
@RestController
@RequestMapping("/api/safety-guides")
@Validated
public class SafetyGuideController {

    private final SafetyGuideService safetyGuideService;

    public SafetyGuideController(SafetyGuideService safetyGuideService) {
        this.safetyGuideService = safetyGuideService;
    }

    @Operation(summary = "안전 가이드 목록 조회", description = "조건에 따라 안전 가이드 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ApiResponse<List<SafetyGuideResponse>> getAllGuides(
            @Parameter(description = "상황 (예: SHIP_ACCIDENT, DROWNING)") @RequestParam(required = false) SafetySituation situation,
            @Parameter(description = "카테고리") @RequestParam(required = false) String category
    ) {
        List<SafetyGuideResponse> guides;

        if (situation != null && category != null) {
            guides = safetyGuideService.getGuidesBySituationAndCategory(situation, category);
        } else if (situation != null) {
            guides = safetyGuideService.getGuidesBySituation(situation);
        } else if (category != null) {
            guides = safetyGuideService.getGuidesByCategory(category);
        } else {
            guides = safetyGuideService.getAllGuides();
        }

        return ApiResponse.success(guides);
    }

    @Operation(summary = "안전 가이드 상세 조회", description = "ID로 특정 안전 가이드를 상세 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ApiResponse<SafetyGuideDetailResponse> getGuideById(
            @Parameter(description = "안전 가이드 ID") @PathVariable @Positive Long id
    ) {
        SafetyGuideDetailResponse guide = safetyGuideService.getGuideById(id);
        return ApiResponse.success(guide);
    }
}
