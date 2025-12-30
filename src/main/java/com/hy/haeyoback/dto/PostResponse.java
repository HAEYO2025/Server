package com.hy.haeyoback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String username;
    private Double latitude;
    private Double longitude;
    private String category;
    private String description;
    private String imageUrl;
    private String address;
    private String createdAt;
    private boolean resolved;
    private int likes;
    private int dislikes;
}