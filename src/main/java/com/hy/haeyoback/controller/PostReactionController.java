package com.hy.haeyoback.controller;

import com.hy.haeyoback.dto.ReactionRequest;
import com.hy.haeyoback.dto.ReactionResponse;
import com.hy.haeyoback.service.PostReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/reactions")
@RequiredArgsConstructor
@Tag(name = "게시물 반응", description = "게시물 좋아요/싫어요 API")
public class PostReactionController {

    private final PostReactionService reactionService;

    @PostMapping
    @Operation(summary = "반응 추가/변경", description = "게시물에 좋아요 또는 싫어요 추가/변경 (토글)")
    public ResponseEntity<ReactionResponse> toggleReaction(
            @PathVariable Long postId,
            @Valid @RequestBody ReactionRequest request,
            @AuthenticationPrincipal UserDetails user) {

        ReactionResponse response = reactionService.toggleReaction(
                postId,
                user.getUsername(),
                request.getReactionType());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "반응 제거", description = "게시물의 좋아요/싫어요 제거")
    public ResponseEntity<ReactionResponse> removeReaction(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails user) {

        ReactionResponse response = reactionService.removeReaction(postId, user.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "반응 상태 조회", description = "게시물의 좋아요/싫어요 개수 및 현재 사용자의 반응 조회")
    public ResponseEntity<ReactionResponse> getReactionStatus(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails user) {

        ReactionResponse response = reactionService.getReactionStatus(postId, user.getUsername());
        return ResponseEntity.ok(response);
    }
}