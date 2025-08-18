package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.AnalysisResponse;
import com.yubaba.studify.dto.PieChartItem;
import com.yubaba.studify.dto.RecordResponse;
import com.yubaba.studify.dto.SaveRecordRequest;
import com.yubaba.studify.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "피드백 상세 조회", description = "피드백 상세 정보를 studyRecordId로 조회합니다.")
    @GetMapping("/{studyRecordId}/feedback")
    public ResponseEntity<ApiResponse<RecordResponse>> getFeedbackDetail(
            @PathVariable Long studyRecordId
    ) {
        RecordResponse dto = recordService.getFeedbackDetail(studyRecordId);
        return ResponseEntity.ok(
                ApiResponse.success(ResponseCode.SUCCESS_FEEDBACK_DETAIL, dto)
        );
    }

    @Operation(summary = "공부 분석 결과 조회", description = "공부 분석 결과를 studyRecordId로 조회합니다.")
    @GetMapping("/{studyRecordId}/result")
    public ResponseEntity<ApiResponse<AnalysisResponse>> getAnalysisResult(
            @PathVariable Long studyRecordId
    ) {
        AnalysisResponse dto = recordService.getAnalysisResult(studyRecordId);
        return ResponseEntity.ok(
                ApiResponse.success(ResponseCode.SUCCESS_ANALYSIS_RESULT, dto)
        );
    }

    @Operation(summary = "공부 기록 저장", description = "공부 로그를 저장합니다.")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> saveRecord(@AuthenticationPrincipal String email, @RequestBody SaveRecordRequest request) {
        recordService.saveLogs(email, request);
        return ResponseEntity.ok(
                ApiResponse.success(ResponseCode.SUCCESS_SAVE_RECORD, null)
        );
    }

    @Operation(summary = "원형 그래프 조회", description = "원형 그래프를 recordId와 tab값(study_time, pose, focus)으로 조회합니다.")
    @GetMapping("/{studyRecordId}/pie-chart")
    public ResponseEntity<ApiResponse<List<PieChartItem>>> getPieChart(
            @PathVariable Long studyRecordId,
            @RequestParam String tab
    ) {
        List<PieChartItem> dto = recordService.getPieChart(studyRecordId, tab);
        return ResponseEntity.ok(
                ApiResponse.success(ResponseCode.SUCCESS_PIE_CHART, dto)
        );
    }

}