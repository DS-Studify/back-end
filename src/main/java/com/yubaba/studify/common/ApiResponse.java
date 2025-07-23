package com.yubaba.studify.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(ResponseCode code, T data) {
        return ApiResponse.<T>builder()
                .status(code.getStatus())
                .code(code.getCode())
                .message(code.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(ResponseCode code) {
        return success(code, null);
    }

    public static <T> ApiResponse<T> error(ResponseCode code, T data) {
        return success(code, data);
    }
}
