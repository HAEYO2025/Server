package com.hy.haeyoback.controller;

import com.hy.haeyoback.dto.CreatePostRequest;
import com.hy.haeyoback.dto.PostResponse;
import com.hy.haeyoback.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor

public class PostController {
    
    private final PostService postService;
    
    /**
     * 게시물 생성 (로그인 필요)
     * POST /api/posts
     * 
     * HTTP 메서드가 POST이므로 Spring Security에서 자동으로 인증 체크
     * 하지만 X-Username 헤더로도 추가 검증
     */
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestHeader(value = "X-Username", required = false) String username,
            @Valid @RequestBody CreatePostRequest request) {
        
        // 로그인 확인 (헤더 체크)
        if (username == null || username.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "로그인이 필요한 기능입니다");
            error.put("requireLogin", true);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        try {
            PostResponse post = postService.createPost(username, request);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * 전체 게시물 조회 (로그인 불필요)
     * GET /api/posts
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    
    /**
     * 게시물 상세 조회 (로그인 불필요)
     * GET /api/posts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostResponse post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * 영역 내 게시물 조회 (로그인 불필요)
     * GET /api/posts/bounds?minLat=35.1&maxLat=35.2&minLon=129.0&maxLon=129.1
     */
    @GetMapping("/bounds")
    public ResponseEntity<List<PostResponse>> getPostsInBounds(
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLon,
            @RequestParam Double maxLon) {
        List<PostResponse> posts = postService.getPostsInBounds(minLat, maxLat, minLon, maxLon);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * 카테고리별 게시물 조회 (로그인 불필요)
     * GET /api/posts/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<PostResponse>> getPostsByCategory(@PathVariable String category) {
        List<PostResponse> posts = postService.getPostsByCategory(category);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * 미해결 게시물 조회 (로그인 불필요)
     * GET /api/posts/unresolved
     */
    @GetMapping("/unresolved")
    public ResponseEntity<List<PostResponse>> getUnresolvedPosts() {
        List<PostResponse> posts = postService.getUnresolvedPosts();
        return ResponseEntity.ok(posts);
    }
    
    /**
     * 해결 상태 토글 (로그인 필요)
     * PATCH /api/posts/{id}/resolve
     */
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<?> toggleResolved(
            @PathVariable Long id,
            @RequestHeader(value = "X-Username", required = false) String username) {
        
        // 로그인 확인
        if (username == null || username.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "로그인이 필요한 기능입니다");
            error.put("requireLogin", true);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        try {
            PostResponse post = postService.toggleResolved(id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * 게시물 삭제 (로그인 필요, 작성자만 가능)
     * DELETE /api/posts/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @RequestHeader(value = "X-Username", required = false) String username) {
        
        // 로그인 확인
        if (username == null || username.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "로그인이 필요한 기능입니다");
            error.put("requireLogin", true);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        try {
            postService.deletePost(id, username);
            Map<String, String> response = new HashMap<>();
            response.put("message", "게시물이 삭제되었습니다");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * 로그인 상태 확인
     * GET /api/posts/check-auth
     */
    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth(
            @RequestHeader(value = "X-Username", required = false) String username) {
        Map<String, Object> response = new HashMap<>();
        
        if (username != null && !username.isEmpty()) {
            response.put("authenticated", true);
            response.put("username", username);
        } else {
            response.put("authenticated", false);
        }
        
        return ResponseEntity.ok(response);
    }
}