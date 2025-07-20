package com.yubaba.studify.repository;

import com.yubaba.studify.entity.TimeStampLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeStampLogRepository extends JpaRepository<TimeStampLog, Long> {
    List<TimeStampLog> findByStudyRecordId(Long studyRecordId);
}
