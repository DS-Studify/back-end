package com.yubaba.studify.service;

import com.yubaba.studify.dto.CalendarResponse;

import java.time.LocalDate;
import java.time.YearMonth;

public interface StatisticsService {
    CalendarResponse getCalendarMonthly(String email, YearMonth yearMonth);
}
