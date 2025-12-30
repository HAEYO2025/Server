package com.hy.haeyoback.repository;

import com.hy.haeyoback.entity.Post;
import com.hy.haeyoback.entity.PostReaction;
import com.hy.haeyoback.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    Optional<PostReaction> findByPostAndUser(Post post, User user);

    boolean existsByPostAndUser(Post post, User user);

    long countByPostAndReactionType(Post post, PostReaction.ReactionType reactionType);

    void deleteByPostAndUser(Post post, User user);
}