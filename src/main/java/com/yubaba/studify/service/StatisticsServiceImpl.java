package com.yubaba.studify.service;

import com.yubaba.studify.dto.CalendarResponse;
import com.yubaba.studify.dto.CalendarSummary;
import com.yubaba.studify.dto.DailyDetail;
import com.yubaba.studify.dto.TimeRange;
import com.yubaba.studify.entity.StudyRecord;
import com.yubaba.studify.entity.StudyState;
import com.yubaba.studify.entity.User;
import com.yubaba.studify.repository.StudyRecordRepository;
import com.yubaba.studify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{
    private final StudyRecordRepository studyRecordRepository;
    private final UserRepository userRepository;

    @Override
    public CalendarResponse getCalendarMonthly(String email, YearMonth yearMonth) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
        Long userId = user.getId();

        // 월간 전체 StudyRecord
        List<StudyRecord> monthlyRecords = studyRecordRepository
                .findByUserIdAndDateBetween(userId, yearMonth.atDay(1), yearMonth.atEndOfMonth());

        // 같은 날짜는 합산 처리
        Map<String, Integer> summaryMap = monthlyRecords.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getDate().toString(),
                        Collectors.summingInt(StudyRecord::getStudyTime)
                ));

        List<CalendarSummary> calendar = summaryMap.entrySet().stream()
                .map(entry -> new CalendarSummary(entry.getKey(), entry.getValue()))
                .toList();

        return new CalendarResponse(
                yearMonth.getYear(),
                yearMonth.getMonthValue(),
                calendar
        );
    }
        // 해당 날짜에 대한 상세 정보
        List<StudyRecord> targetRecords = monthlyRecords.stream()
                .filter(r -> r.getDate().equals(date))
                .toList();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        int totalStudyTime = targetRecords.stream().mapToInt(StudyRecord::getStudyTime).sum();
        int totalFocusTime = targetRecords.stream().mapToInt(StudyRecord::getFocusTime).sum();

        List<TimeRange> timeRanges = targetRecords.stream()
                .map(record -> new TimeRange(
                        record.getId(),
                        record.getStartTime().format(timeFormatter),
                        record.getEndTime().format(timeFormatter)
                ))
                .collect(Collectors.toList());

        DailyDetail detail = new DailyDetail(
                formatKoreanShortDate(date),
                totalStudyTime,
                totalFocusTime,
                timeRanges
        );

        return new CalendarResponse(
                yearMonth.getYear(),
                yearMonth.getMonthValue(),
                calendar,
                detail
        );
    }
    private String formatKoreanShortDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN));
    }
}
