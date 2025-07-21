package com.yubaba.studify.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordTimeLog {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
