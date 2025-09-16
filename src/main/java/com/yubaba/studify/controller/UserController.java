package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.ChangeNicknameRequest;
import com.yubaba.studify.dto.ChangePasswordRequest;
import com.yubaba.studify.dto.ProfileResponse;
import com.yubaba.studify.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "프로필 조회", description = "사용자 프로필을 조회합니다.")
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

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@AuthenticationPrincipal String email, @RequestBody ChangePasswordRequest request) {

        try {
            userService.changePassword(email, request.getOriginPassword(), request.getNewPassword());

            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(ResponseCode.SUCCESS_CHANGE_PASSWORD.getStatus())
                    .code(ResponseCode.SUCCESS_CHANGE_PASSWORD.getCode())
                    .message(ResponseCode.SUCCESS_CHANGE_PASSWORD.getMessage())
                    .data(null)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(ResponseCode.INCORRECT_PASSWORD.getStatus())
                    .body(ApiResponse.<Void>builder()
                            .status(ResponseCode.INCORRECT_PASSWORD.getStatus())
                            .code(ResponseCode.INCORRECT_PASSWORD.getCode())
                            .message(ResponseCode.INVALID_EMAIL_FORMAT.getMessage())
                            .data(null)
                            .build());
        }
    }

    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    @PatchMapping("/change-nickname")
    public ResponseEntity<ApiResponse<ProfileResponse>> changeNickname(@AuthenticationPrincipal String email, @RequestBody ChangeNicknameRequest request) {

        ProfileResponse response = userService.changeNickname(email, request.getNewNickname());

        return ResponseEntity.ok(ApiResponse.<ProfileResponse>builder()
                .status(ResponseCode.SUCCESS_CHANGE_NICKNAME.getStatus())
                .code(ResponseCode.SUCCESS_CHANGE_NICKNAME.getCode())
                .message(ResponseCode.SUCCESS_CHANGE_NICKNAME.getMessage())
                .data(response)
                .build());
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal String email) {
        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.ok(ApiResponse.<Void>builder()
                    .status(ResponseCode.SUCCESS_DELETE_USER.getStatus())
                    .code(ResponseCode.SUCCESS_DELETE_USER.getCode())
                    .message(ResponseCode.SUCCESS_DELETE_USER.getMessage())
                    .data(null)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(ResponseCode.USER_NOT_FOUND.getStatus())
                    .body(ApiResponse.<Void>builder()
                            .status(ResponseCode.USER_NOT_FOUND.getStatus())
                            .code(ResponseCode.USER_NOT_FOUND.getCode())
                            .message(ResponseCode.USER_NOT_FOUND.getMessage())
                            .data(null)
                            .build());
        }
    }

}
