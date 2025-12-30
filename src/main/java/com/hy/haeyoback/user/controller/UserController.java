package com.hy.haeyoback.user.controller;

import com.hy.haeyoback.user.dto.UserResponse;
import com.hy.haeyoback.user.service.UserService;
import com.hy.haeyoback.global.api.ApiResponse;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.global.security.JwtUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe(@AuthenticationPrincipal JwtUserDetails principal) {
        if (principal == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }
        UserResponse response = userService.getUserResponse(principal.getId());
        return ApiResponse.success(response);
    }
}
