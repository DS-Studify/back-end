package com.yubaba.studify.service;

import com.yubaba.studify.dto.ProfileResponse;
import com.yubaba.studify.entity.User;

public interface UserService {

    ProfileResponse getProfile(String email);

    void changePassword(String email, String originPassword, String newPassword);

    ProfileResponse changeNickname(String email, String newNickname);

}
