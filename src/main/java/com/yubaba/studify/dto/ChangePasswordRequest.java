package com.yubaba.studify.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String originPassword;
    private String newPassword;
}
