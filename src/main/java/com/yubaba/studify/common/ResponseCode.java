package com.yubaba.studify.common;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS_REGISTER(200, "SUCCESS_REGISTER", "회원가입을 성공했습니다."),
    SUCCESS_EMAIL_SEND(200, "SUCCESS_EMAIL_SEND", "인증 코드가 이메일로 전송되었습니다."),
    SUCCESS_EMAIL_RESEND(200, "SUCCESS_EMAIL_RESEND", "인증 코드를 재전송했습니다."),
    SUCCESS_EMAIL_VERIFY(200, "SUCCESS_EMAIL_VERIFY", "이메일 인증에 성공했습니다."),
    INVALID_EMAIL_FORMAT(400, "INVALID_EMAIL_FORMAT", "이메일 형식이 유효하지 않습니다."),
    INVALID_EMAIL_CODE(400, "INVALID_EMAIL_CODE", "인증 코드가 일치하지 않거나 만료되었습니다."),
    EMAIL_NOT_VERIFIED(400, "EMAIL_NOT_VERIFIED", "이메일 인증이 완료되지 않았습니다."),
    SUCCESS_LOGIN(200, "SUCCESS_LOGIN", "로그인을 성공했습니다."),
    DUPLICATE_EMAIL(400, "DUPLICATE_EMAIL", "이미 존재하는 이메일입니다."),
//    DUPLICATE_NICKNAME(400, "DUPLICATE_NICKNAME", "이미 존재하는 닉네임입니다."),
    INVALID_CREDENTIALS(401, "INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(401, "INVALID_REFRESH_TOKEN", "Refresh Token이 유효하지 않거나 만료되었습니다."),
    SUCCESS_TOKEN_REFRESH(200, "SUCCESS_TOKEN_REFRESH", "Access Token이 재발급되었습니다."),
    SUCCESS_LOGOUT(200, "SUCCESS_LOGOUT", "로그아웃에 성공했습니다."),

    SUCCESS_PROFILE(200, "SUCCESS_PROFILE", "프로필 조회에 성공했습니다."),
    SUCCESS_CHANGE_PASSWORD(200, "SUCCESS_CHANGE_PASSWORD", "비밀번호를 변경했습니다."),
    INCORRECT_PASSWORD(403, "INCORRECT_PASSWORD", "현재 비밀번호가 일치하지 않습니다."),
    SUCCESS_CHANGE_NICKNAME(200, "SUCCESS_CHANGE_NICKNAME", "닉네임을 변경했습니다."),

    SUCCESS_HOME(200, "SUCCESS_HOME", "홈화면 조회에 성공했습니다."),
    SUCCESS_SAVE_RECORD(200, "SUCCES_SAVE_RECORD", "공부 기록 저장에 성공했습니다."),
    SUCCESS_ANALYSIS_RESULT(200, "SUCCESS_ANALYSIS_RESULT", "공부 분석 결과 조회에 성공했습니다."),
    SUCCESS_PIE_CHART(200, "SUCCESS_PIE_CHART", "원형 그래프 조회에 성공했습니다."),
    SUCCESS_FEEDBACK_DETAIL(200, "SUCCESS_FEEDBACK_DETAIL", "피드백 상세 조회에 성공했습니다."),
    SUCCESS_STATISTICS_CALENDAR_MONTHLY(200, "SUCCESS_STATISTICS_CALENDAR_MONTHLY", "통계 달력 월간 조회에 성공했습니다."),

    SUCCESS_FEEDBACK_GENERATED(200, "FEEDBACK_GENERATION_SUCCESS", "AI 피드백 생성에 성공했습니다."),
    FAIL_FEEDBACK_GENERATED(500, "FEEDBACK_GENERATION_FAILED", "AI 피드백 생성 중 오류가 발생했습니다.");

    private final int status;
    private final String code;
    private final String message;

    ResponseCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
