package com.hy.haeyoback.service;

import com.hy.haeyoback.dto.ReactionResponse;
import com.hy.haeyoback.entity.Post;
import com.hy.haeyoback.entity.PostReaction;
import com.hy.haeyoback.user.entity.User;
import com.hy.haeyoback.repository.PostReactionRepository;
import com.hy.haeyoback.repository.PostRepository;
import com.hy.haeyoback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostReactionService {

    private final PostReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReactionResponse toggleReaction(Long postId, String username, PostReaction.ReactionType reactionType) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Optional<PostReaction> existingReaction = reactionRepository.findByPostAndUser(post, user);

        if (existingReaction.isPresent()) {
            PostReaction reaction = existingReaction.get();

            // 같은 반응이면 제거 (토글)
            if (reaction.getReactionType() == reactionType) {
                reactionRepository.delete(reaction);
                return buildReactionResponse(post, user);
            }

            // 다른 반응이면 변경
            reaction.setReactionType(reactionType);
            reactionRepository.save(reaction);
        } else {
            // 새로운 반응 추가
            PostReaction newReaction = PostReaction.builder()
                    .post(post)
                    .user(user)
                    .reactionType(reactionType)
                    .build();
            reactionRepository.save(newReaction);
        }

        return buildReactionResponse(post, user);
    }

    @Transactional
    public ReactionResponse removeReaction(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        reactionRepository.deleteByPostAndUser(post, user);

        return buildReactionResponse(post, user);
    }

    public ReactionResponse getReactionStatus(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        return buildReactionResponse(post, user);
    }

    private ReactionResponse buildReactionResponse(Post post, User user) {
        long likeCount = reactionRepository.countByPostAndReactionType(post, PostReaction.ReactionType.LIKE);
        long dislikeCount = reactionRepository.countByPostAndReactionType(post, PostReaction.ReactionType.DISLIKE);

        Optional<PostReaction> userReaction = reactionRepository.findByPostAndUser(post, user);
        String userReactionType = userReaction.map(r -> r.getReactionType().name()).orElse(null);

        return ReactionResponse.builder()
                .postId(post.getId())
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .userReaction(userReactionType)
                .build();
    }
}