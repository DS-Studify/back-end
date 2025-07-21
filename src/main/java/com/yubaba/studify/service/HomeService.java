package com.yubaba.studify.service;

import com.yubaba.studify.dto.HomeResponse;

import java.time.LocalDate;

public interface HomeService {
    HomeResponse getHomeData(Long userId, LocalDate today);
}
