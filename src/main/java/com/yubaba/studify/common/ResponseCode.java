package com.yubaba.studify.common;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS_REGISTER(200, "SUCCESS_REGISTER", "회원가입을 성공했습니다."),
    SUCCESS_LOGIN(200, "SUCCESS_LOGIN", "로그인을 성공했습니다."),
    DUPLICATE_EMAIL(400, "DUPLICATE_EMAIL", "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(400, "DUPLICATE_NICKNAME", "이미 존재하는 닉네임입니다."),
    INVALID_CREDENTIALS(401, "INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다."),
    SUCCESS_HOME(200, "SUCCESS_HOME", "홈화면 조회에 성공했습니다."),
    SUCCESS_ANALYSIS_RESULT(200, "SUCCESS_ANALYSIS_RESULT", "공부 분석 결과 조회에 성공했습니다."),
    SUCCESS_FEEDBACK_DETAIL(200, "SUCCESS_FEEDBACK_DETAIL", "피드백 상세 조회에 성공했습니다."),
    SUCCESS_STATISTICS_CALENDAR(200, "SUCCESS_STATISTICS_CALENDAR", "통계 달력 조회에 성공했습니다.");

    private final int status;
    private final String code;
    private final String message;

    ResponseCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
