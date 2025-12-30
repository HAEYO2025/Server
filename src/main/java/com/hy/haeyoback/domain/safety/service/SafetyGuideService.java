package com.hy.haeyoback.domain.safety.service;

import com.hy.haeyoback.domain.safety.dto.SafetyGuideDetailResponse;
import com.hy.haeyoback.domain.safety.dto.SafetyGuideResponse;
import com.hy.haeyoback.domain.safety.entity.SafetyGuide;
import com.hy.haeyoback.domain.safety.entity.SafetySituation;
import com.hy.haeyoback.domain.safety.repository.SafetyGuideRepository;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SafetyGuideService {

    private final SafetyGuideRepository safetyGuideRepository;

    public SafetyGuideService(SafetyGuideRepository safetyGuideRepository) {
        this.safetyGuideRepository = safetyGuideRepository;
    }

    public List<SafetyGuideResponse> getAllGuides() {
        return safetyGuideRepository.findAllByOrderByPriorityAsc().stream()
                .map(SafetyGuideResponse::from)
                .collect(Collectors.toList());
    }

    public List<SafetyGuideResponse> getGuidesBySituation(SafetySituation situation) {
        return safetyGuideRepository.findBySituationOrderByPriorityAsc(situation).stream()
                .map(SafetyGuideResponse::from)
                .collect(Collectors.toList());
    }

    public List<SafetyGuideResponse> getGuidesByCategory(String category) {
        return safetyGuideRepository.findByCategoryOrderByPriorityAsc(category).stream()
                .map(SafetyGuideResponse::from)
                .collect(Collectors.toList());
    }

    public SafetyGuideDetailResponse getGuideById(Long id) {
        SafetyGuide guide = safetyGuideRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "Safety guide not found"));
        return SafetyGuideDetailResponse.from(guide);
    }
}
