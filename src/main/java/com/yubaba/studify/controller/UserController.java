package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.ProfileResponse;
import com.yubaba.studify.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@AuthenticationPrincipal String email) {
        ProfileResponse response = userService.getProfile(email);

        return ResponseEntity.ok(ApiResponse.<ProfileResponse>builder()
                .status(ResponseCode.SUCCESS_PROFILE.getStatus())
                .code(ResponseCode.SUCCESS_PROFILE.getCode())
                .message(ResponseCode.SUCCESS_PROFILE.getMessage())
                .data(response)
                .build());
    }

}
