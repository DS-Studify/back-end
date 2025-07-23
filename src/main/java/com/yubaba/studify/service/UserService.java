package com.yubaba.studify.service;

import com.yubaba.studify.dto.ProfileResponse;
import com.yubaba.studify.entity.User;
import com.yubaba.studify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        return new ProfileResponse(user.getEmail(), user.getNickname());
    }

}
