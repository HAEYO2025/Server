package com.hy.haeyoback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponse {
    private Long postId;
    private Long likeCount;
    private Long dislikeCount;
    private String userReaction; // "LIKE", "DISLIKE", 또는 null
}