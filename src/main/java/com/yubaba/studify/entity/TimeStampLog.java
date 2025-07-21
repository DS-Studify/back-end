package com.yubaba.studify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeStampLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_record_id", nullable = false)
    private StudyRecord studyRecord;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private StudyState state;

    @Column(nullable = true)
    private LocalDateTime startTime;  // 상태 시작 시간

    @Column(nullable = true)
    private LocalDateTime endTime;    // 상태 끝 시간
}
