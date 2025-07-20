package com.yubaba.studify.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordRequest {
    private Long studyRecordId;
    private String studyDate;
    private String actualStudyTime;
    private String tab;
//    private FeedbackPieChartDto pieChartRatio;
    private Map<String, List<RecordTimeLog>> timeLog;
    private String aiFeedback;
}
