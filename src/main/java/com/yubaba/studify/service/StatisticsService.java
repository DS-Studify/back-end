package com.yubaba.studify.service;

import com.yubaba.studify.dto.CalendarResponse;
import com.yubaba.studify.dto.DailyDetail;

import java.time.LocalDate;
import java.time.YearMonth;

public interface StatisticsService {
    CalendarResponse getCalendarMonthly(String email, YearMonth yearMonth);
    DailyDetail getCalendarDaily(String email, LocalDate date);
}
