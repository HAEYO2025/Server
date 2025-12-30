package com.hy.haeyoback.safety.repository;

import com.hy.haeyoback.safety.entity.SafetyGuide;
import com.hy.haeyoback.safety.entity.SafetySituation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SafetyGuideRepository extends JpaRepository<SafetyGuide, Long> {
    List<SafetyGuide> findBySituationAndCategoryOrderByPriorityAsc(SafetySituation situation, String category);
    List<SafetyGuide> findBySituationOrderByPriorityAsc(SafetySituation situation);
    List<SafetyGuide> findAllByOrderByPriorityAsc();
    List<SafetyGuide> findByCategoryOrderByPriorityAsc(String category);
}
