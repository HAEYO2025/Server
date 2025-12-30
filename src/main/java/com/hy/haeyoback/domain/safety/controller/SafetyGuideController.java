package com.hy.haeyoback.domain.safety.controller;

import com.hy.haeyoback.domain.safety.dto.SafetyGuideDetailResponse;
import com.hy.haeyoback.domain.safety.dto.SafetyGuideResponse;
import com.hy.haeyoback.domain.safety.entity.SafetySituation;
import com.hy.haeyoback.domain.safety.service.SafetyGuideService;
import com.hy.haeyoback.global.api.ApiResponse;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/safety-guides")
@Validated
public class SafetyGuideController {

    private final SafetyGuideService safetyGuideService;

    public SafetyGuideController(SafetyGuideService safetyGuideService) {
        this.safetyGuideService = safetyGuideService;
    }

    @GetMapping
    public ApiResponse<List<SafetyGuideResponse>> getAllGuides(
            @RequestParam(required = false) SafetySituation situation,
            @RequestParam(required = false) String category
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

    @GetMapping("/{id}")
    public ApiResponse<SafetyGuideDetailResponse> getGuideById(@PathVariable @Positive Long id) {
        SafetyGuideDetailResponse guide = safetyGuideService.getGuideById(id);
        return ApiResponse.success(guide);
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Void> shareGuide(@PathVariable Long id) {
        safetyGuideService.incrementShareCount(id);
        return ResponseEntity.noContent().build();
    }
}
