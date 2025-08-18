package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.CalendarResponse;
import com.yubaba.studify.dto.DailyDetail;
import com.yubaba.studify.dto.RecordResponse;
import com.yubaba.studify.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "통계 달력 월간 조회", description = "통계 달력 정보를 조회합니다.")
    @GetMapping("/calendar/monthly")
    public ResponseEntity<ApiResponse<CalendarResponse>> getCalendarMonthly(
            @AuthenticationPrincipal String email, @RequestParam YearMonth month
    ) {
        CalendarResponse dto = statisticsService.getCalendarMonthly(email, month);
        return ResponseEntity.ok(
                ApiResponse.success(ResponseCode.SUCCESS_STATISTICS_CALENDAR_MONTHLY, dto)
        );
    }

    @Operation(summary = "통계 달력 특정 일자 상세 조회", description = "통계 달력 정보를 조회합니다.")
    @GetMapping("/calendar/daily")
    public ResponseEntity<ApiResponse<DailyDetail>> getCalendarDaily(
            @AuthenticationPrincipal String email, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        DailyDetail dto = statisticsService.getCalendarDaily(email, date);
        return ResponseEntity.ok(
                ApiResponse.success(ResponseCode.SUCCESS_STATISTICS_CALENDAR_DAILY, dto)
        );
    }
}
