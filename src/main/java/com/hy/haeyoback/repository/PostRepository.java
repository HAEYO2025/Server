package com.hy.haeyoback.repository;

import com.hy.haeyoback.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByUserId(Long userId);

    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<Post> findByCategory(String category);
    
    @Query("SELECT p FROM Post p WHERE " +
           "p.latitude BETWEEN :minLat AND :maxLat AND " +
           "p.longitude BETWEEN :minLon AND :maxLon")
    List<Post> findPostsInBounds(
        @Param("minLat") Double minLat,
        @Param("maxLat") Double maxLat,
        @Param("minLon") Double minLon,
        @Param("maxLon") Double maxLon
    );
    
    List<Post> findByResolvedFalse();
}
