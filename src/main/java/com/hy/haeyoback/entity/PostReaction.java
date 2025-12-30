package com.hy.haeyoback.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import com.hy.haeyoback.user.entity.User;

@Entity
@Table(name = "post_reactions", uniqueConstraints = @UniqueConstraint(columnNames = { "post_id", "user_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType; // LIKE 또는 DISLIKE

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum ReactionType {
        LIKE,
        DISLIKE
    }
}