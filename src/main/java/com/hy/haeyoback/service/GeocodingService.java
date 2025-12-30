package com.hy.haeyoback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeocodingService {
    
    private final RestTemplate restTemplate;
    
    @Value("${naver.client-id:YOUR_CLIENT_ID}")
    private String clientId;
    
    @Value("${naver.client-secret:YOUR_CLIENT_SECRET}")
    private String clientSecret;
    
    private static final String GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    private static final String REVERSE_GEOCODING_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
    
    /**
     * 주소 -> 좌표 변환
     */
    public Map<String, Object> addressToCoordinates(String address) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_URL)
                    .queryParam("query", address)
                    .toUriString();
            
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
            headers.set("X-NCP-APIGW-API-KEY", clientSecret);
            
            org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(headers);
            
            org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Map.class
            );
            
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> body = response.getBody();
            
            if (body != null && body.get("addresses") != null) {
                List<Map<String, Object>> addresses = (List<Map<String, Object>>) body.get("addresses");
                if (!addresses.isEmpty()) {
                    Map<String, Object> firstAddress = addresses.get(0);
                    result.put("latitude", Double.parseDouble((String) firstAddress.get("y")));
                    result.put("longitude", Double.parseDouble((String) firstAddress.get("x")));
                    result.put("address", firstAddress.get("roadAddress"));
                    result.put("jibunAddress", firstAddress.get("jibunAddress"));
                    return result;
                }
            }
            
            throw new RuntimeException("주소를 찾을 수 없습니다");
            
        } catch (Exception e) {
            log.error("Geocoding error: ", e);
            throw new RuntimeException("주소 변환 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 좌표 -> 주소 변환 (Reverse Geocoding)
     */
    public Map<String, Object> coordinatesToAddress(Double lat, Double lon) {
        try {
            String coords = lon + "," + lat;
            String url = UriComponentsBuilder.fromHttpUrl(REVERSE_GEOCODING_URL)
                    .queryParam("coords", coords)
                    .queryParam("output", "json")
                    .queryParam("orders", "roadaddr,addr")
                    .toUriString();
            
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
            headers.set("X-NCP-APIGW-API-KEY", clientSecret);
            
            org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(headers);
            
            org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Map.class
            );
            
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> body = response.getBody();
            
            if (body != null && body.get("results") != null) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
                if (!results.isEmpty()) {
                    Map<String, Object> firstResult = results.get(0);
                    Map<String, Object> region = (Map<String, Object>) firstResult.get("region");
                    Map<String, Object> land = (Map<String, Object>) firstResult.get("land");
                    
                    // 도로명 주소
                    if (land != null && land.get("addition0") != null) {
                        Map<String, Object> addition = (Map<String, Object>) land.get("addition0");
                        result.put("roadAddress", addition.get("value"));
                    }
                    
                    // 지번 주소
                    if (region != null) {
                        StringBuilder jibunAddress = new StringBuilder();
                        Map<String, Object> area1 = (Map<String, Object>) region.get("area1");
                        Map<String, Object> area2 = (Map<String, Object>) region.get("area2");
                        Map<String, Object> area3 = (Map<String, Object>) region.get("area3");
                        
                        if (area1 != null) jibunAddress.append(area1.get("name")).append(" ");
                        if (area2 != null) jibunAddress.append(area2.get("name")).append(" ");
                        if (area3 != null) jibunAddress.append(area3.get("name"));
                        
                        result.put("jibunAddress", jibunAddress.toString().trim());
                    }
                    
                    result.put("latitude", lat);
                    result.put("longitude", lon);
                    return result;
                }
            }
            
            throw new RuntimeException("주소를 찾을 수 없습니다");
            
        } catch (Exception e) {
            log.error("Reverse geocoding error: ", e);
            throw new RuntimeException("좌표 변환 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 장소 검색
     */
    public Map<String, Object> searchPlace(String query) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_URL)
                    .queryParam("query", query)
                    .toUriString();
            
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
            headers.set("X-NCP-APIGW-API-KEY", clientSecret);
            
            org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(headers);
            
            org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Map.class
            );
            
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> body = response.getBody();
            
            if (body != null && body.get("addresses") != null) {
                List<Map<String, Object>> addresses = (List<Map<String, Object>>) body.get("addresses");
                result.put("places", addresses);
                result.put("count", addresses.size());
                return result;
            }
            
            result.put("places", List.of());
            result.put("count", 0);
            return result;
            
        } catch (Exception e) {
            log.error("Place search error: ", e);
            throw new RuntimeException("장소 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}