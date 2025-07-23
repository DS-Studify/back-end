package com.yubaba.studify.service;

import com.yubaba.studify.dto.LoginRequest;
import com.yubaba.studify.dto.LoginResponse;
import com.yubaba.studify.dto.SignupRequest;
import com.yubaba.studify.entity.User;
import com.yubaba.studify.repository.UserRepository;
import com.yubaba.studify.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public void register(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("DUPLICATE_EMAIL");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("INVALID_CREDENTIALS");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponse(token);
    }

    @Override
    public void verifyEmail(String email, String code) {
        String saved = redisService.getEmailAuthCode(email);
        if (saved == null || !saved.equals(code)) {
            throw new IllegalArgumentException("INVALID_EMAIL_CODE");
        }
        redisService.deleteEmailAuthCode(email);
    }

}
