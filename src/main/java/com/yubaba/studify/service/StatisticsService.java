package com.yubaba.studify.service;

import com.yubaba.studify.dto.CalendarResponse;

import java.time.LocalDate;

public interface StatisticsService {
    CalendarResponse getCalendarData(String email, LocalDate date);
}
