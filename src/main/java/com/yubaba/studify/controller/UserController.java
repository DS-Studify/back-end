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
import org.springframework.http.HttpStatus;
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

        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_PROFILE, response));
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@AuthenticationPrincipal String email, @RequestBody ChangePasswordRequest request) {

        try {
            userService.changePassword(email, request.getOriginPassword(), request.getNewPassword());

            return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_CHANGE_PASSWORD, email));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(ResponseCode.INCORRECT_PASSWORD));
        }
    }

    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    @PatchMapping("/change-nickname")
    public ResponseEntity<ApiResponse<ProfileResponse>> changeNickname(@AuthenticationPrincipal String email, @RequestBody ChangeNicknameRequest request) {

        ProfileResponse response = userService.changeNickname(email, request.getNewNickname());

        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_CHANGE_NICKNAME, response));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteUser(@AuthenticationPrincipal String email) {
        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_DELETE_USER, email));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(ResponseCode.USER_NOT_FOUND, email));
        }
    }

}
