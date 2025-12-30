//package com.hy.haeyoback.service;
//
//import com.hy.haeyoback.security.JwtTokenProvider;
//
//import com.hy.haeyoback.dto.LoginRequest;
//import com.hy.haeyoback.dto.SignupRequest;
//import com.hy.haeyoback.entity.User;
//import com.hy.haeyoback.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    /**
//     * 회원가입
//     */
//    @Transactional
//    public Map<String, Object> signup(SignupRequest request) {
//        // 중복 체크
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("이미 사용 중인 사용자명입니다");
//        }
//
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("이미 사용 중인 이메일입니다");
//        }
//
//        // 사용자 생성
//        User user = User.builder()
//                .username(request.getUsername())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .build();
//
//        User savedUser = userRepository.save(user);
//
//        // 응답 생성
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "회원가입이 완료되었습니다");
//        response.put("username", savedUser.getUsername());
//        response.put("email", savedUser.getEmail());
//
//        return response;
//    }
//
//    /**
//     * 로그인
//     */
//    @Transactional(readOnly = true)
//    public Map<String, Object> login(LoginRequest request) {
//        // 사용자 조회
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
//
//        // 비밀번호 확인
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("비밀번호가 일치하지 않습니다");
//        }
//
//        // 응답 생성
//        Map<String, Object> response = new HashMap<>();
//        String token = jwtTokenProvider.createToken(user.getUsername());
//
//        response.put("token", token);
//        response.put("username", user.getUsername());
//        response.put("email", user.getEmail());
//        response.put("userId", user.getId());
//
//        return response;
//    }
//
//    /**
//     * 사용자명으로 사용자 조회
//     */
//    @Transactional(readOnly = true)
//    public User getUserByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
//    }
//
//    /**
//     * 사용자 ID로 사용자 조회
//     */
//    @Transactional(readOnly = true)
//    public User getUserById(Long id) {
//        return userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
//    }
//
//    /**
//     * 사용자명 중복 확인
//     */
//    @Transactional(readOnly = true)
//    public boolean isUsernameAvailable(String username) {
//        return !userRepository.existsByUsername(username);
//    }
//
//    /**
//     * 이메일 중복 확인
//     */
//    @Transactional(readOnly = true)
//    public boolean isEmailAvailable(String email) {
//        return !userRepository.existsByEmail(email);
//    }
//}