package com.exercise.dto;

import com.exercise.entity.WalkingRecord;

import java.time.LocalDateTime;

public record WalkingRecordResponse(
        int id,
        LocalDateTime startTime,
        Double distance,
        Double calorie,
        Long durationSeconds,
        int count
) {
    public static WalkingRecordResponse from(WalkingRecord entity) {
        return new WalkingRecordResponse(
                entity.getId(),
                entity.getStartTime(),
                entity.getDistance(),
                entity.getCalorie(),
                entity.getDurationSeconds(),
                entity.getCount()
        );
    }
}
