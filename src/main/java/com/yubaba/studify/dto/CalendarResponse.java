package com.yubaba.studify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarResponse {
    private int year;
    private int month;
    private List<CalendarSummary> calendar;
    private DailyDetail detail;
}
