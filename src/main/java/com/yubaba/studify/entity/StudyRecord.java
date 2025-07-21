package com.yubaba.studify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int recordTime;   // 녹화시간 (초)

    @Column(nullable = false)
    private int studyTime;    // 실제 공부시간 (초)

    @Column(nullable = false)
    private int focusTime;    // 집중시간 (초)

    @Column(nullable = false)
    private int nfocusTime;   // 비집중시간 (초)

    @Column(nullable = false)
    private int poseTime;     // 바른자세시간 (초)

    @Column(nullable = false)
    private int nposeTime;    // 나쁜자세시간 (초)

    @Column(nullable = false)
    private int sleepTime;    // 졸음시간 (초)

    @Column(nullable = false)
    private int awayTime;     // 자리비움시간 (초)

    @Column(nullable = false)
    private int etcTime;      // 기타시간 (초)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feedback;  // AI 피드백

    @OneToMany(mappedBy = "studyRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeStampLog> timeStampLogs;

    // Lombok @Getter, @Setter 사용 가능
}
