package com.yubaba.studify.service;

import com.yubaba.studify.dto.LoginRequest;
import com.yubaba.studify.dto.LoginResponse;
import com.yubaba.studify.dto.SignupRequest;

public interface AuthService {
    void register(SignupRequest request);
    void verifyEmail(String email, String code);
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
}
