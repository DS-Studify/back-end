package com.yubaba.studify.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordTimeLog {
    private String startTime;
    private String endTime;
}
