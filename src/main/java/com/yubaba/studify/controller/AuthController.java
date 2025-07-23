package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.*;
import com.yubaba.studify.entity.User;
import com.yubaba.studify.repository.UserRepository;
import com.yubaba.studify.security.JwtUtil;
import com.yubaba.studify.service.MailService;
import com.yubaba.studify.service.RedisService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final MailService mailService;
    private final RedisService redisService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody SignupRequest singupReq) {
        if (userRepository.existsByEmail(singupReq.getEmail())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Void>builder()
                            .status(ResponseCode.DUPLICATE_EMAIL.getStatus())
                            .code(ResponseCode.DUPLICATE_EMAIL.getCode())
                            .message(ResponseCode.DUPLICATE_EMAIL.getMessage())
                            .data(null)
                            .build()
            );
        }

        if (userRepository.existsByNickname(singupReq.getNickname())) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Void>builder()
                            .status(ResponseCode.DUPLICATE_NICKNAME.getStatus())
                            .code(ResponseCode.DUPLICATE_NICKNAME.getCode())
                            .message(ResponseCode.DUPLICATE_NICKNAME.getMessage())
                            .data(null)
                            .build()
            );
        }

        User user = User.builder()
                .email(singupReq.getEmail())
                .password(passwordEncoder.encode(singupReq.getPassword()))
                .nickname(singupReq.getNickname())
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ResponseCode.SUCCESS_REGISTER.getStatus())
                        .code(ResponseCode.SUCCESS_REGISTER.getCode())
                        .message(ResponseCode.SUCCESS_REGISTER.getMessage())
                        .data(null)
                        .build()
        );
    }

    @PostMapping("/send-verification")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestBody @Valid EmailRequest email) {
        mailService.sendAuthCode(email.getEmail());

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(ResponseCode.SUCCESS_EMAIL_SEND.getStatus())
                        .code(ResponseCode.SUCCESS_EMAIL_SEND.getCode())
                        .message(ResponseCode.SUCCESS_EMAIL_SEND.getMessage())
                        .data(email.getEmail())
                        .build()
        );
    }

    @PostMapping("/reverify")
    public ResponseEntity<ApiResponse<String>> resendEmail(@RequestBody @Valid EmailRequest email) {
        mailService.resendAuthCode(email.getEmail());

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(ResponseCode.SUCCESS_EMAIL_RESEND.getStatus())
                        .code(ResponseCode.SUCCESS_EMAIL_RESEND.getCode())
                        .message(ResponseCode.SUCCESS_EMAIL_RESEND.getMessage())
                        .data(email.getEmail())
                        .build()
        );
    }

    @PostMapping("/check-verification")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String email, @RequestParam String code) {

        String savedCode = redisService.getEmailAuthCode(email);

        if (savedCode != null && savedCode.equals(code)) {
            redisService.deleteEmailAuthCode(email);

            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .status(ResponseCode.SUCCESS_EMAIL_VERIFY.getStatus())
                            .code(ResponseCode.SUCCESS_EMAIL_VERIFY.getCode())
                            .message(ResponseCode.SUCCESS_EMAIL_VERIFY.getMessage())
                            .data(email)
                            .build()
            );
        }

        return ResponseEntity.badRequest().body(
                ApiResponse.<String>builder()
                        .status(ResponseCode.INVALID_EMAIL_CODE.getStatus())
                        .code(ResponseCode.INVALID_EMAIL_CODE.getCode())
                        .message(ResponseCode.INVALID_EMAIL_CODE.getMessage())
                        .data(email)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginReq) {
        User user = userRepository.findByEmail(loginReq.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.<LoginResponse>builder()
                            .status(ResponseCode.INVALID_CREDENTIALS.getStatus())
                            .code(ResponseCode.INVALID_CREDENTIALS.getCode())
                            .message(ResponseCode.INVALID_CREDENTIALS.getMessage())
                            .data(null)
                            .build()
            );
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .status(ResponseCode.SUCCESS_LOGIN.getStatus())
                        .code(ResponseCode.SUCCESS_LOGIN.getCode())
                        .message(ResponseCode.SUCCESS_LOGIN.getMessage())
                        .data(new LoginResponse(token))
                        .build()
        );
    }

}
