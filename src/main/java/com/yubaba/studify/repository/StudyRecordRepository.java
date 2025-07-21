package com.yubaba.studify.repository;

import com.yubaba.studify.entity.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {
    List<StudyRecord> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<StudyRecord> findByUserIdAndDate(Long userId, LocalDate date);
}
