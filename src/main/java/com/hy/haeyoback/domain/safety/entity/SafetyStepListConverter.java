package com.hy.haeyoback.domain.safety.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hy.haeyoback.global.config.JsonListConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class SafetyStepListConverter extends JsonListConverter<SafetyStep> {
    public SafetyStepListConverter() {
        super(new TypeReference<List<SafetyStep>>() {});
    }
}
