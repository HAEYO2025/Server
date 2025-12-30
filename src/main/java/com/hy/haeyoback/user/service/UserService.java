package com.hy.haeyoback.user.service;

import com.hy.haeyoback.user.dto.UserResponse;
import com.hy.haeyoback.user.entity.User;
import com.hy.haeyoback.user.repository.UserRepository;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse signup(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "Username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "Email already exists");
        }

        User user = new User(username, email, passwordEncoder.encode(password));
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getEmail());
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        return user;
    }

    public User authenticateById(Long userId, String password) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        return user;
    }

    public UserResponse getUserResponse(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }

    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "User not found"));
    }
}
