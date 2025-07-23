package com.yubaba.studify.service;

import com.yubaba.studify.dto.LoginRequest;
import com.yubaba.studify.dto.LoginResponse;
import com.yubaba.studify.dto.SignupRequest;

public interface AuthService {
    void register(SignupRequest request);
    LoginResponse login(LoginRequest request);
    void verifyEmail(String email, String code);
}
