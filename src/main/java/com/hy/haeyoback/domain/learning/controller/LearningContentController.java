package com.hy.haeyoback.domain.learning.controller;

import com.hy.haeyoback.domain.learning.dto.LearningContentDetailResponse;
import com.hy.haeyoback.domain.learning.dto.LearningContentResponse;
import com.hy.haeyoback.domain.learning.service.LearningContentService;
import com.hy.haeyoback.global.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/learning/contents")
public class LearningContentController {

    private final LearningContentService learningContentService;

    public LearningContentController(LearningContentService learningContentService) {
        this.learningContentService = learningContentService;
    }

    @GetMapping
    public ApiResponse<List<LearningContentResponse>> getAllContents() {
        List<LearningContentResponse> contents = learningContentService.getAllContents();
        return ApiResponse.success(contents);
    }

    @GetMapping("/{id}")
    public ApiResponse<LearningContentDetailResponse> getContentById(@PathVariable Long id) {
        LearningContentDetailResponse content = learningContentService.getContentById(id);
        return ApiResponse.success(content);
    }
}
