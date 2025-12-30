package com.hy.haeyoback.domain.scenario.repository;

import com.hy.haeyoback.domain.scenario.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    List<Scenario> findAllByOrderByCreatedAtDesc();
}
