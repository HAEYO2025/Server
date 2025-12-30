package com.hy.haeyoback.controller;

import com.hy.haeyoback.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/geocoding")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GeocodingController {
    
    private final GeocodingService geocodingService;
    
    /**
     * 주소 -> 좌표 변환
     * GET /api/geocoding/address?query=서울특별시 강남구 테헤란로 152
     */
    @GetMapping("/address")
    public ResponseEntity<?> addressToCoordinates(@RequestParam String query) {
        try {
            Map<String, Object> result = geocodingService.addressToCoordinates(query);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * 좌표 -> 주소 변환
     * GET /api/geocoding/coordinates?lat=37.5665&lon=126.9780
     */
    @GetMapping("/coordinates")
    public ResponseEntity<?> coordinatesToAddress(
            @RequestParam Double lat,
            @RequestParam Double lon) {
        try {
            Map<String, Object> result = geocodingService.coordinatesToAddress(lat, lon);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * 장소 검색
     * GET /api/geocoding/search?query=부산역
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchPlace(@RequestParam String query) {
        try {
            Map<String, Object> result = geocodingService.searchPlace(query);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}