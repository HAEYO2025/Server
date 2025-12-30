package com.hy.haeyoback.domain.learning.service;

import com.hy.haeyoback.domain.learning.dto.LearningContentDetailResponse;
import com.hy.haeyoback.domain.learning.dto.LearningContentResponse;
import com.hy.haeyoback.domain.learning.entity.LearningContent;
import com.hy.haeyoback.domain.learning.repository.LearningContentRepository;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LearningContentService {

    private final LearningContentRepository learningContentRepository;

    public LearningContentService(LearningContentRepository learningContentRepository) {
        this.learningContentRepository = learningContentRepository;
    }

    public List<LearningContentResponse> getAllContents() {
        return learningContentRepository.findAll().stream()
                .map(LearningContentResponse::from)
                .collect(Collectors.toList());
    }

    public LearningContentDetailResponse getContentById(Long id) {
        LearningContent content = learningContentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "Learning content not found"));
        return LearningContentDetailResponse.from(content);
    }
}
