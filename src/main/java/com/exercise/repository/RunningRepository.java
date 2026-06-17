package com.exercise.repository;

import com.exercise.entity.RunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningRepository extends JpaRepository<RunningRecord, Long> {
    List<RunningRecord> findAllByOrderByStartTimeDesc();
}
