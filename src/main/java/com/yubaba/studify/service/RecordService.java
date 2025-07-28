package com.yubaba.studify.service;

import com.yubaba.studify.dto.AnalysisResponse;
import com.yubaba.studify.dto.RecordResponse;
import com.yubaba.studify.dto.RecordTimeLog;
import com.yubaba.studify.dto.SaveRecordRequest;

import java.util.List;
import java.util.Map;

public interface RecordService {
    RecordResponse getFeedbackDetail(Long recordId, String tab);
    AnalysisResponse getAnalysisResult(Long recordId, String tab);
    void saveLogs(String email, SaveRecordRequest request);
}
