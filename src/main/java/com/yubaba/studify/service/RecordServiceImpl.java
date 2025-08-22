package com.yubaba.studify.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yubaba.studify.dto.*;
import com.yubaba.studify.entity.StudyRecord;
import com.yubaba.studify.entity.StudyState;
import com.yubaba.studify.entity.TimeStampLog;
import com.yubaba.studify.entity.User;
import com.yubaba.studify.repository.StudyRecordRepository;
import com.yubaba.studify.repository.TimeStampLogRepository;
import com.yubaba.studify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final StudyRecordRepository studyRecordRepository;
    private final TimeStampLogRepository timeStampLogRepository;
    private final UserRepository userRepository;
    private final ChatGptService chatGptService;
    private final ObjectMapper mapper;

    @Override
    public RecordResponse getFeedbackDetail(Long recordId) {
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

        return RecordResponse.builder()
                .studyRecordId(record.getId())
                .studyDate(formatKoreanDate(record.getDate()))
                .startTime(String.valueOf(record.getStartTime()))
                .endTime(String.valueOf(record.getEndTime()))
                .actualStudyTime(String.valueOf(record.getStudyTime()))
                .timeLog(timeLogMap)
                .aiFeedback(record.getFeedback())
                .build();
    }

    @Override
    public AnalysisResponse getAnalysisResult(Long recordId) {
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

        int recordRatio = (int) (((double) record.getStudyTime() / record.getRecordTime()) * 100);

        return AnalysisResponse.builder()
                .studyRecordId(record.getId())
                .studyDate(formatKoreanDate(record.getDate()))
                .startTime(String.valueOf(record.getStartTime()))
                .endTime(String.valueOf(record.getEndTime()))
                .recordTime(String.valueOf(record.getRecordTime()))
                .recordRatio(recordRatio)
                .actualStudyTime(String.valueOf(record.getStudyTime()))
                .timeLog(timeLogMap)
                .aiFeedback(record.getFeedback())
                .build();
    }

    @Override
    public Long saveLogs(String email, SaveRecordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        // 시간 계산
        int recordTime = (int) Duration.between(request.getStartTime(), request.getEndTime()).getSeconds();
        int studyTime = 0, focusTime = 0, nfocusTime = 0, poseTime = 0,
                nposeTime = 0, sleepTime = 0, awayTime = 0, etcTime = 0;

        List<TimeStampLog> logs = new ArrayList<>();

        for (Map.Entry<Integer, List<RecordTimeLog>> entry : request.getTimeLog().entrySet()) {
            int stateCode = entry.getKey();
            StudyState state = StudyState.values()[stateCode - 1];
            for (RecordTimeLog period : entry.getValue()) {
                int duration = (int) Duration.between(period.getStartTime(), period.getEndTime()).getSeconds();

                switch (state) {
                    case STUDYING -> studyTime += duration;
                    case GOOD_POSE -> poseTime += duration;
                    case NFOCUS_POSE -> nfocusTime += duration;
                    case SLEEP_POSE -> sleepTime += duration;
                    case AWAY -> awayTime += duration;
                    case ETC -> etcTime += duration;
                }

                logs.add(TimeStampLog.builder()
                        .state(state)
                        .startTime(period.getStartTime())
                        .endTime(period.getEndTime())
                        .build());
            }
        }

        nposeTime = nfocusTime + sleepTime;


        // AI 피드백 요청 데이터 구성: timeLog 형태
        Map<String, List<RecordTimeLog>> timeLogMap = logs.stream()
                .collect(Collectors.groupingBy(
                        log -> String.valueOf(getStateCode(log.getState())),
                        Collectors.mapping(
                                log -> new RecordTimeLog(log.getStartTime(), log.getEndTime()),
                                Collectors.toList()
                        )
                ));

        String aiFeedback;
        try {
            // timeLogMap을 JSON으로 변환해서 전달
            JsonNode timeLogJson = mapper.valueToTree(timeLogMap);
            aiFeedback = chatGptService.getFeedback(timeLogJson);
        } catch (Exception e) {
            e.printStackTrace();
            aiFeedback = "AI 피드백 생성 중 오류가 발생했습니다.";
        }

        StudyRecord record = StudyRecord.builder()
                .userId(user.getId())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .recordTime(recordTime)
                .studyTime(studyTime)
                .focusTime(poseTime)
                .nfocusTime(nfocusTime)
                .poseTime(poseTime)
                .nposeTime(nposeTime)
                .sleepTime(sleepTime)
                .awayTime(awayTime)
                .etcTime(etcTime)
                .feedback(aiFeedback)
                .timeStampLogs(new ArrayList<>())
                .build();

        logs.forEach(log -> log.setStudyRecord(record));
        record.getTimeStampLogs().addAll(logs);
        StudyRecord saved = studyRecordRepository.save(record);

        return saved.getId();
    }

    @Override
    public List<PieChartItem> getPieChart(Long recordId, String tab) {
        StudyRecord record = studyRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("StudyRecord not found"));

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
                return List.of(
                        new PieChartItem("공부", ratioStudy, study),
                        new PieChartItem("졸음", ratioSleep, sleep),
                        new PieChartItem("자리비움", ratioAway, away),
                        new PieChartItem("공부 중지", ratioEtc, etc)
                );
            }
            case "focus" -> {
                int focus = record.getFocusTime();
                int nfocus = record.getNfocusTime();
                int total = focus + nfocus;
                int ratioFocus = getRatio(focus, total);
                return List.of(
                        new PieChartItem("집중", ratioFocus, focus),
                        new PieChartItem("비집중", 100 - ratioFocus, nfocus)
                );
            }
            case "pose" -> {
                int pose = record.getPoseTime();
                int npose = record.getNposeTime();
                int total = pose + npose;
                int ratioPose = getRatio(pose, total);
                return List.of(
                        new PieChartItem("바른 자세", ratioPose, pose),
                        new PieChartItem("나쁜 자세", 100 - ratioPose, npose)
                );
            }
            default -> {
                return List.of();
            }
        }
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