package com.yubaba.studify.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final ChatGptService chatGptService;

//    @PostMapping
//    public String chat(@RequestBody String message) throws Exception {
//        return chatGptService.getChatResponse(message);
//    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<String>> generateFeedback(@RequestBody JsonNode studyResult) {
        try {
            String feedback = chatGptService.getFeedback(studyResult);
            return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS_FEEDBACK_GENERATED, feedback));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(ResponseCode.FAIL_FEEDBACK_GENERATED.getStatus())
                    .body(ApiResponse.error(ResponseCode.FAIL_FEEDBACK_GENERATED, e.getMessage()));
        }
    }

}
