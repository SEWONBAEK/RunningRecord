package com.exercise.repository;

import com.exercise.entity.WalkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalkingRepository extends JpaRepository<WalkingRecord, Long> {
    List<WalkingRecord> findAllByOrderByStartTimeDesc();
}
