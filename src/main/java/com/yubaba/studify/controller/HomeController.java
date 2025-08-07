package com.yubaba.studify.controller;

import com.yubaba.studify.common.ApiResponse;
import com.yubaba.studify.common.ResponseCode;
import com.yubaba.studify.dto.HomeResponse;
import com.yubaba.studify.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "홈화면 조회", description = "홈화면 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<HomeResponse>> getHome(@AuthenticationPrincipal String email) {
        LocalDate today = LocalDate.now();
        HomeResponse dto = homeService.getHomeData(email, today);
        return ResponseEntity.ok(
                ApiResponse.<HomeResponse>builder()
                        .status(ResponseCode.SUCCESS_HOME.getStatus())
                        .code(ResponseCode.SUCCESS_HOME.getCode())
                        .message(ResponseCode.SUCCESS_HOME.getMessage())
                        .data(dto)
                        .build()
        );
    }
}
