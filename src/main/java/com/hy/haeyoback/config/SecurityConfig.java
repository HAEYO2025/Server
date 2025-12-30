package com.hy.haeyoback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // 인증 관련 API는 모두 허용
                .requestMatchers("/api/auth/**").permitAll()
                
                // GET 요청 (마커 조회)는 모두 허용
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/posts/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/posts").permitAll()
                
                // POST 요청 (마커 생성)은 permitAll로 설정하고 컨트롤러에서 헤더 체크
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/posts").permitAll()
                
                // PATCH, DELETE 등 나머지 요청도 모두 허용 (컨트롤러에서 체크)
                .requestMatchers("/api/posts/**").permitAll()
                
                // VWorld 프록시 API
                .requestMatchers("/api/vworld/**").permitAll()
                
                // Geocoding API
                .requestMatchers("/api/geocoding/**").permitAll()
                
                // 정적 리소스 허용 (HTML 페이지 모두 접근 가능)
                .requestMatchers("/", "/login.html", "/signup.html", "/index.html", "/map.html").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                
                .anyRequest().permitAll()
            );
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}