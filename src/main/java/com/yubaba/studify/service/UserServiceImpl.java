package com.yubaba.studify.service;

import com.yubaba.studify.dto.ProfileResponse;
import com.yubaba.studify.entity.User;
import com.yubaba.studify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @Override
    public ProfileResponse getProfile(String email) {
        User user = findUserByEmail(email);
        return new ProfileResponse(user.getEmail(), user.getNickname());
    }

    @Override
    public void changePassword(String email, String originPassword, String newPassword) {
        User user = findUserByEmail(email);

        if (!passwordEncoder.matches(originPassword, user.getPassword())) {
            throw new IllegalArgumentException("INCORRECT_PASSWORD");
        }

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public ProfileResponse changeNickname(String email, String newNickname) {
        User user = findUserByEmail(email);
        user.updateNickname(newNickname);
        userRepository.save(user);

        return new ProfileResponse(user.getEmail(), user.getNickname());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }

    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        userRepository.delete(user);
        redisService.deleteRefreshToken(user.getEmail());
    }

}
