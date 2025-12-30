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

    public UserResponse signup(String email, String username, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "Email already exists");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "Username already exists");
        }

        User user = new User(email, username, passwordEncoder.encode(password));
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getEmail(), saved.getUsername());
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        return user;
    }

    public User authenticateByUsername(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        return user;
    }

    public UserResponse getUserResponse(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "User not found"));
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }

    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "User not found"));
    }
}
