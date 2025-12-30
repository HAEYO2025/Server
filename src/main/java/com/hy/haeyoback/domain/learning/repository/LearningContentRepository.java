package com.hy.haeyoback.domain.learning.repository;

import com.hy.haeyoback.domain.learning.entity.LearningContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningContentRepository extends JpaRepository<LearningContent, Long> {
}
