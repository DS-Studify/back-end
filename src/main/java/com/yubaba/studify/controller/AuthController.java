package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.*;
import com.yubaba.studify.service.AuthService;
import com.yubaba.studify.service.MailService;
import com.yubaba.studify.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;
    private final RedisService redisService;

    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody SignupRequest singupReq) {
        try {
            authService.register(singupReq);
            return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_REGISTER, null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ResponseCode.DUPLICATE_EMAIL));
        }

    }

    @Operation(summary = "이메일 인증", description = "이메일 인증 메일을 보냅니다.")
    @PostMapping("/send-verification")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestBody @Valid EmailRequest email) {
        mailService.sendAuthCode(email.getEmail());

        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_EMAIL_SEND, email.getEmail()));
    }

    @Operation(summary = "이매일 재인증", description = "이메일 인증 메일을 재전송합니다.")
    @PostMapping("/reverify")
    public ResponseEntity<ApiResponse<String>> resendEmail(@RequestBody @Valid EmailRequest email) {
        mailService.resendAuthCode(email.getEmail());

        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_EMAIL_RESEND, email.getEmail()));
    }

    @Operation(summary = "이메일 인증번호 확인", description = "이메일 인증번호를 확인합니다.")
    @PostMapping("/check-verification")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String email, @RequestParam String code) {

        String savedCode = redisService.getEmailAuthCode(email);

        if (savedCode != null && savedCode.equals(code)) {
            redisService.deleteEmailAuthCode(email);

            return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_EMAIL_VERIFY, email));
        }

        return ResponseEntity.badRequest().body(ApiResponse.error(ResponseCode.INVALID_EMAIL_CODE, email));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginReq) {
        try {
            LoginResponse token = authService.login(loginReq);
            return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_LOGIN, token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(ResponseCode.INVALID_CREDENTIALS));
        }
    }

}
