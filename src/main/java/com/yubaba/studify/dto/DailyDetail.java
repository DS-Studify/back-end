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
public class DailyDetail {
    private String date;
    private int totalStudyTime;
    private int focusTime;
    private List<TimeRange> timeRanges;
}
