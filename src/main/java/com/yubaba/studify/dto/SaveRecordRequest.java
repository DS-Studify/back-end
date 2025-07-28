package com.yubaba.studify.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveRecordRequest {
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Map<Integer, List<RecordTimeLog>> timeLog;
}
