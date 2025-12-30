package com.hy.haeyoback.domain.safety.repository;

import com.hy.haeyoback.domain.safety.entity.SafetyGuide;
import com.hy.haeyoback.domain.safety.entity.SafetySituation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SafetyGuideRepository extends JpaRepository<SafetyGuide, Long> {
    List<SafetyGuide> findBySituationOrderByPriorityAsc(SafetySituation situation);
    List<SafetyGuide> findAllByOrderByPriorityAsc();
    List<SafetyGuide> findByCategoryOrderByPriorityAsc(String category);
}
