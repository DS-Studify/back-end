package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.dto.RecordResponse;
import com.yubaba.studify.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                        .status(200)
                        .code("SUCCESS_FEEDBACK_DETAIL_FETCHED")
                        .message("피드백 상세 조회에 성공했습니다.")
                        .data(dto)
                        .build()
        );
    }
}