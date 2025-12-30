package com.hy.haeyoback.scenario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scenario_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TurnHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String situation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String choice;

    @Column(nullable = false)
    private Double survivalRate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    public TurnHistory(Scenario scenario, String situation, String choice, Double survivalRate, String comment) {
        this.scenario = scenario;
        this.situation = situation;
        this.choice = choice;
        this.survivalRate = survivalRate;
        this.comment = comment;
    }
}
