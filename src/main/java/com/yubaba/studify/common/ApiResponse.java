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
}
