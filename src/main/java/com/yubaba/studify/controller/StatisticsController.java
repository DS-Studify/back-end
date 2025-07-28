package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.CalendarResponse;
import com.yubaba.studify.dto.RecordResponse;
import com.yubaba.studify.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "통계 달력 조회", description = "통계 달력 정보를 조회합니다.")
    @GetMapping("/calendar")
    public ResponseEntity<ApiResponse<CalendarResponse>> getCalendar(
            @AuthenticationPrincipal String email, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        CalendarResponse dto = statisticsService.getCalendarData(email, date);
        return ResponseEntity.ok(
                ApiResponse.<CalendarResponse>builder()
                        .status(ResponseCode.SUCCESS_STATISTICS_CALENDAR.getStatus())
                        .code(ResponseCode.SUCCESS_STATISTICS_CALENDAR.getCode())
                        .message(ResponseCode.SUCCESS_STATISTICS_CALENDAR.getMessage())
                        .data(dto)
                        .build()
        );
    }
}
