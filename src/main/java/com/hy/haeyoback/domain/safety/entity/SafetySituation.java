package com.hy.haeyoback.domain.safety.entity;

public enum SafetySituation {
    SHIP_ACCIDENT("선박 사고"),
    DROWNING("익수 사고"),
    WEATHER_WARNING("기상 악화"),
    FIRE_ONBOARD("선박 화재"),
    MAN_OVERBOARD("낙수 사고"),
    MEDICAL_EMERGENCY("의료 응급상황"),
    COLLISION("충돌 사고"),
    ENGINE_FAILURE("엔진 고장"),
    GENERAL_SAFETY("일반 안전수칙");

    private final String description;

    SafetySituation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
