package com.hy.haeyoback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/kakao")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class KakaoProxyController {
    
    private final RestTemplate restTemplate;
    
    @Value("${kakao.api.key}")
    private String apiKey;
    
    /**
     * Kakao 키워드 검색
     * GET /api/kakao/search?query=부산역
     */
    @GetMapping("/search")
    public ResponseEntity<String> searchPlace(@RequestParam String query) {
        try {
            String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + 
                        URLEncoder.encode(query, StandardCharsets.UTF_8);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + apiKey);
            
            HttpEntity<?> entity = new HttpEntity<>(headers);
            
            log.info("Kakao API 요청: {}", url);
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );
            
            log.info("Kakao API 응답: {}", response.getBody());
            return ResponseEntity.ok(response.getBody());
            
        } catch (Exception e) {
            log.error("Kakao API 오류: ", e);
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Kakao 주소 검색
     * GET /api/kakao/address?query=서울특별시 강남구
     */
    @GetMapping("/address")
    public ResponseEntity<String> searchAddress(@RequestParam String query) {
        try {
            String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + 
                        URLEncoder.encode(query, StandardCharsets.UTF_8);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + apiKey);
            
            HttpEntity<?> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );
            
            return ResponseEntity.ok(response.getBody());
            
        } catch (Exception e) {
            log.error("Kakao Address API 오류: ", e);
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}