package com.yubaba.studify.repository;

import com.yubaba.studify.entity.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {
}
