package com.exercise.dto;

import com.exercise.entity.RunningRecord;

import java.time.LocalDateTime;

public record RunningRecordResponse(
        int id,
        LocalDateTime startTime,
        Double distance,
        Double calorie,
        Long durationSeconds,
        String pace
) {
    public static RunningRecordResponse from(RunningRecord entity) {
        return new RunningRecordResponse(
                entity.getId(),
                entity.getStartTime(),
                entity.getDistance(),
                entity.getCalorie(),
                entity.getDurationSeconds(),
                entity.getPace()
        );
    }
}
