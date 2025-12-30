package com.hy.haeyoback.service;

import com.hy.haeyoback.dto.CreatePostRequest;
import com.hy.haeyoback.dto.PostResponse;
import com.hy.haeyoback.entity.Post;
import com.hy.haeyoback.user.entity.User;
import com.hy.haeyoback.repository.PostRepository;
import com.hy.haeyoback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/images/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 게시물 생성
     */
    @Transactional
    public PostResponse createPost(String username, CreatePostRequest request) {
        // 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 이미지 저장
        String imageUrl = null;
        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            imageUrl = saveImage(request.getImageBase64());
        }

        // 게시물 생성
        Post post = Post.builder()
                .user(user)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .category(request.getCategory())
                .description(request.getDescription())
                .imageUrl(imageUrl)
                .address(request.getAddress())
                .resolved(false)
                .build();

        Post savedPost = postRepository.save(post);

        return convertToResponse(savedPost);
    }

    /**
     * 전체 게시물 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 게시물 상세 조회
     */
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다"));

        return convertToResponse(post);
    }

    /**
     * 영역 내 게시물 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsInBounds(Double minLat, Double maxLat, Double minLon, Double maxLon) {
        return postRepository.findPostsInBounds(minLat, maxLat, minLon, maxLon).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 게시물 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByCategory(String category) {
        return postRepository.findByCategory(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 미해결 게시물 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getUnresolvedPosts() {
        return postRepository.findByResolvedFalse().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 해결 상태 토글
     */
    @Transactional
    public PostResponse toggleResolved(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다"));

        post.setResolved(!post.isResolved());
        Post updatedPost = postRepository.save(post);

        return convertToResponse(updatedPost);
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public void deletePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다"));

        // 작성자 확인
        if (!post.getUser().getUsername().equals(username)) {
            throw new RuntimeException("게시물 삭제 권한이 없습니다");
        }

        // 이미지 파일 삭제
        if (post.getImageUrl() != null) {
            deleteImage(post.getImageUrl());
        }

        postRepository.delete(post);
    }

    /**
     * Base64 이미지를 파일로 저장
     */
    private String saveImage(String base64Image) {
        try {
            // Base64 디코딩
            String[] parts = base64Image.split(",");
            String imageData = parts.length > 1 ? parts[1] : parts[0];
            byte[] imageBytes = Base64.getDecoder().decode(imageData);

            // 파일명 생성
            String fileName = UUID.randomUUID().toString() + ".jpg";
            String filePath = UPLOAD_DIR + fileName;

            // 디렉토리 생성
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일 저장
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(imageBytes);
            }

            return "/uploads/images/" + fileName;

        } catch (Exception e) {
            log.error("이미지 저장 실패: ", e);
            throw new RuntimeException("이미지 저장에 실패했습니다");
        }
    }

    /**
     * 이미지 파일 삭제
     */
    private void deleteImage(String imageUrl) {
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            File file = new File(UPLOAD_DIR + fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            log.error("이미지 삭제 실패: ", e);
        }
    }

    /**
     * Post 엔티티를 PostResponse로 변환
     */
    private PostResponse convertToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .category(post.getCategory())
                .description(post.getDescription())
                .imageUrl(post.getImageUrl())
                .address(post.getAddress())
                .createdAt(post.getCreatedAt().format(DATE_FORMATTER))
                .resolved(post.isResolved())
                .build();
    }
}