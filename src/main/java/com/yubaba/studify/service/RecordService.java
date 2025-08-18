package com.yubaba.studify.service;

import com.yubaba.studify.dto.*;

import java.util.List;
import java.util.Map;

public interface RecordService {
    RecordResponse getFeedbackDetail(Long recordId);
    AnalysisResponse getAnalysisResult(Long recordId);
    void saveLogs(String email, SaveRecordRequest request);
    List<PieChartItem> getPieChart(Long recordId, String tab);
}
