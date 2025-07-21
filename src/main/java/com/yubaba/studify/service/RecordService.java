package com.yubaba.studify.service;

import com.yubaba.studify.dto.AnalysisResponse;
import com.yubaba.studify.dto.RecordResponse;

public interface RecordService {
    RecordResponse getFeedbackDetail(Long recordId, String tab);
    AnalysisResponse getAnalysisResult(Long recordId, String tab);
}
