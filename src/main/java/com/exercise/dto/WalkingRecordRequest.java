package com.exercise.dto;

import com.exercise.entity.WalkingRecord;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.apache.commons.csv.CSVRecord;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record WalkingRecordRequest(
    @Positive(message = "걸음수는 0보다 커야 합니다.")
    int count,

    String exerciseType,

    @NotNull(message = "시작 시간 형식이 올바르지 않거나 누락되었습니다.")
    @DateTimeFormat
    LocalDateTime startTime,

    @NotNull(message = "종료 시간 형식이 올바르지 않거나 누락되었습니다.")
    @DateTimeFormat
    LocalDateTime endTime,

    @Positive(message = "거리는 0보다 커야 합니다.")
    Double distance,

    @Positive(message = "칼로리는 0보다 커야 합니다.")
    Double calorie
) {

    // 날짜 파싱을 위한 포맷터 선언
    private static final DateTimeFormatter CSV_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s.SSS");

    public static WalkingRecordRequest from(CSVRecord csvRecord) {

        return new WalkingRecordRequest(
                parseInternalInt(csvRecord.get("com.samsung.health.exercise.count")),
                csvRecord.get("com.samsung.health.exercise.exercise_type"),
                parseInternalDateTime(csvRecord.get("com.samsung.health.exercise.start_time")),
                parseInternalDateTime(csvRecord.get("com.samsung.health.exercise.end_time")),
                parseInternalDouble(csvRecord.get("com.samsung.health.exercise.distance")),
                parseInternalDouble(csvRecord.get("com.samsung.health.exercise.calorie"))
        );

    }

    // 걸음수 쓰레기 데이터 방아올 시 0으로 변환
    private static int parseInternalInt(String str) {
        return (str == null || str.isEmpty() || "0.0".equals(str)) ? 0 : (int) Double.parseDouble(str);
    }

    // 거리, 칼로리 쓰레기 데이터 받아올 시 0.0으로 변환
    private static double parseInternalDouble(String str) {
        return (str == null || str.trim().isEmpty()) ? 0.0 : Double.parseDouble(str.trim());
    }

    // 시작 시간, 종료 시간 데이터 가공
    private static LocalDateTime parseInternalDateTime(String str) {
        if (str == null || !str.contains("-")) return null;
        String cleanTime = str.trim().replaceAll("\\s+", " ");
        return LocalDateTime.parse(cleanTime, CSV_FORMATTER);
    }

    public WalkingRecord toEntity() {
        return WalkingRecord.builder()
                .count(count)
                .exerciseType(exerciseType)
                .startTime(startTime)
                .endTime(endTime)
                .distance(distance)
                .calorie(calorie)
                .build();
    }
}
