package com.hy.haeyoback.user.controller;

import com.hy.haeyoback.user.dto.UserResponse;
import com.hy.haeyoback.user.service.UserService;
import com.hy.haeyoback.global.api.ApiResponse;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.global.security.JwtUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "내 정보 조회", description = "인증된 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe(@AuthenticationPrincipal JwtUserDetails principal) {
        if (principal == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Unauthorized");
        }
        UserResponse response = userService.getUserResponse(principal.getId());
        return ApiResponse.success(response);
    }
}
