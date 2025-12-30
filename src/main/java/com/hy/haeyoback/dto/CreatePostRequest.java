package com.hy.haeyoback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {
    
    @NotNull(message = "위도는 필수입니다")
    private Double latitude;
    
    @NotNull(message = "경도는 필수입니다")
    private Double longitude;
    
    @NotBlank(message = "카테고리는 필수입니다")
    private String category; // TRASH, DAMAGE, WILDLIFE
    
    private String description;
    
    private String imageBase64; // Base64 인코딩된 이미지
    
    private String address; // 주소 정보 (선택)
}