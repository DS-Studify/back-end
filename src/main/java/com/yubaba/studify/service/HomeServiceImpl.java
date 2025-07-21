package com.yubaba.studify.service;

import com.yubaba.studify.dto.HomeResponse;
import com.yubaba.studify.entity.StudyRecord;
import com.yubaba.studify.entity.User;
import com.yubaba.studify.repository.StudyRecordRepository;
import com.yubaba.studify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService{
    private final UserRepository userRepository;
    private final StudyRecordRepository studyRecordRepository;

    @Override
    public HomeResponse getHomeData(Long userId, LocalDate today) {
        // 닉네임 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 오늘 날짜의 studyTime 합산
        List<StudyRecord> todayRecords = studyRecordRepository.findByUserIdAndDate(userId, today);
        int todayStudyTime = todayRecords.stream()
                .mapToInt(StudyRecord::getStudyTime)
                .sum();

        return HomeResponse.builder()
                .nickName(user.getNickname())
                .todayStudyTime(todayStudyTime)
                .build();
    }

}
