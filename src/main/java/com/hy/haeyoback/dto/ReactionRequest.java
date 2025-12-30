package com.hy.haeyoback.dto;

import com.hy.haeyoback.entity.PostReaction;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionRequest {

    @NotNull(message = "반응 타입은 필수입니다")
    private PostReaction.ReactionType reactionType; // LIKE 또는 DISLIKE
}