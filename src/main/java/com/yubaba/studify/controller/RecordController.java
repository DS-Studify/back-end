package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.AnalysisResponse;
import com.yubaba.studify.dto.RecordResponse;
import com.yubaba.studify.dto.RecordTimeLog;
import com.yubaba.studify.dto.SaveRecordRequest;
import com.yubaba.studify.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "피드백 상세 조회", description = "피드백 상세 정보를 recordId와 tab값으로 조회합니다.")
    @GetMapping("/{studyRecordId}/feedback")
    public ResponseEntity<ApiResponse<RecordResponse>> getFeedbackDetail(
            @PathVariable Long studyRecordId,
            @RequestParam String tab
    ) {
        RecordResponse dto = recordService.getFeedbackDetail(studyRecordId, tab);
        return ResponseEntity.ok(
                ApiResponse.<RecordResponse>builder()
                        .status(ResponseCode.SUCCESS_FEEDBACK_DETAIL.getStatus())
                        .code(ResponseCode.SUCCESS_FEEDBACK_DETAIL.getCode())
                        .message(ResponseCode.SUCCESS_FEEDBACK_DETAIL.getMessage())
                        .data(dto)
                        .build()
        );
    }

    @Operation(summary = "피드백 상세 조회", description = "피드백 상세 정보를 recordId와 tab값으로 조회합니다.")
    @GetMapping("/{studyRecordId}/result")
    public ResponseEntity<ApiResponse<AnalysisResponse>> getAnalysisResult(
            @PathVariable Long studyRecordId,
            @RequestParam String tab
    ) {
        AnalysisResponse dto = recordService.getAnalysisResult(studyRecordId, tab);
        return ResponseEntity.ok(
                ApiResponse.<AnalysisResponse>builder()
                        .status(ResponseCode.SUCCESS_FEEDBACK_DETAIL.getStatus())
                        .code(ResponseCode.SUCCESS_FEEDBACK_DETAIL.getCode())
                        .message(ResponseCode.SUCCESS_FEEDBACK_DETAIL.getMessage())
                        .data(dto)
                        .build()
        );
    }

    @Operation(summary = "공부 기록 저장", description = "공부 로그를 저장합니다.")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> saveRecord(@RequestBody SaveRecordRequest request) {
        Long userId = 2L;
        recordService.saveLogs(userId, request);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(ResponseCode.SUCCESS_ANALYSIS_RESULT.getStatus())
                        .code(ResponseCode.SUCCESS_ANALYSIS_RESULT.getCode())
                        .message(ResponseCode.SUCCESS_ANALYSIS_RESULT.getMessage())
                        .build()
        );
    }
}