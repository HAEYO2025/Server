package com.hy.haeyoback.domain.user.service;

import com.hy.haeyoback.domain.user.dto.UserResponse;
import com.hy.haeyoback.domain.user.entity.User;
import com.hy.haeyoback.domain.user.repository.UserRepository;
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

    public UserResponse signup(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "Email already exists");
        }

        User user = new User(email, passwordEncoder.encode(password));
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getEmail());
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        return user;
    }

    public UserResponse getUserResponse(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }
}
