package com.yubaba.studify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordResponse {
    private Long studyRecordId;
    private String studyDate;
    private String actualStudyTime;
    private String tab;
    private PieChart pieChartRatio;
    private Map<String, List<RecordTimeLog>> timeLog;
    private String aiFeedback;
}
