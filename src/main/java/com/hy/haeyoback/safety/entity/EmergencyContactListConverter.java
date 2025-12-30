package com.hy.haeyoback.safety.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hy.haeyoback.global.config.JsonListConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class EmergencyContactListConverter extends JsonListConverter<EmergencyContact> {
    public EmergencyContactListConverter() {
        super(new TypeReference<List<EmergencyContact>>() {});
    }
}
