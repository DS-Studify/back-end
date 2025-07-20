package com.yubaba.studify.service;

import com.yubaba.studify.dto.PieChart;
import com.yubaba.studify.dto.PieChartItem;
import com.yubaba.studify.dto.RecordResponse;
import com.yubaba.studify.dto.RecordTimeLog;
import com.yubaba.studify.entity.StudyRecord;
import com.yubaba.studify.entity.StudyState;
import com.yubaba.studify.entity.TimeStampLog;
import com.yubaba.studify.repository.StudyRecordRepository;
import com.yubaba.studify.repository.TimeStampLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final StudyRecordRepository studyRecordRepository;
    private final TimeStampLogRepository timeStampLogRepository;

    public RecordResponse getFeedbackDetail(Long recordId, String tab) {
        StudyRecord record = studyRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("StudyRecord not found"));

        List<TimeStampLog> timeStampLogs = timeStampLogRepository.findByStudyRecordId(recordId);

        // 상태별 로그 그룹핑 → 그래프용 시간 로그 유지
        Map<String, List<RecordTimeLog>> timeLogMap = timeStampLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> String.valueOf(getStateCode(log.getState())),
                        Collectors.mapping(
                                log -> new RecordTimeLog(log.getStartTime(), log.getEndTime()),
                                Collectors.toList()
                        )
                ));

        PieChart pieChart;

        switch (tab) {
            case "study_time" -> {
                int study = record.getStudyTime();
                int sleep = record.getSleepTime();
                int away = record.getAwayTime();
                int etc = record.getEtcTime();
                int total = study + sleep + away + etc;
                int ratioStudy = getRatio(study, total);
                int ratioSleep = getRatio(sleep, total);
                int ratioAway = getRatio(away, total);
                int ratioEtc = getRatio(etc, total);
                pieChart = new PieChart(List.of(
                        new PieChartItem("공부", ratioStudy, study),
                        new PieChartItem("졸음", ratioSleep, sleep),
                        new PieChartItem("자리비움", ratioAway, away),
                        new PieChartItem("기타", ratioEtc, etc)
                ));
            }
            case "focus" -> {
                int focus = record.getFocusTime();
                int nfocus = record.getNfocusTime();
                int total = focus + nfocus;
                int ratioFocus = getRatio(focus, total);
                pieChart = new PieChart(List.of(
                        new PieChartItem("집중", ratioFocus, focus),
                        new PieChartItem("비집중", 100 - ratioFocus, nfocus)
                ));
            }
            case "pose" -> {
                int pose = record.getPoseTime();
                int npose = record.getNposeTime();
                int total = pose + npose;
                int ratioPose = getRatio(pose, total);
                pieChart = new PieChart(List.of(
                        new PieChartItem("바른 자세", ratioPose, pose),
                        new PieChartItem("나쁜 자세", 100 - ratioPose, npose)
                ));
            }
            default -> {
                pieChart = new PieChart(List.of());
            }
        }

        return RecordResponse.builder()
                .studyRecordId(record.getId())
                .studyDate(formatKoreanDate(record.getDate()))
                .actualStudyTime(String.valueOf(record.getStudyTime()))
                .tab(tab)
                .pieChartRatio(pieChart)
                .timeLog(timeLogMap)
                .aiFeedback(record.getFeedback())
                .build();
    }

    private int getStateCode(StudyState state) {
        return switch (state) {
            case STUDYING -> 1;
            case GOOD_POSE -> 2;
            case NFOCUS_POSE -> 3;
            case SLEEP_POSE -> 4;
            case AWAY -> 5;
            case ETC -> 6;
        };
    }

    private int getRatio(int part, int total) {
        return total == 0 ? 0 : Math.round((float) part / total * 100);
    }

    private String formatKoreanDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E)", Locale.KOREAN));
    }
}