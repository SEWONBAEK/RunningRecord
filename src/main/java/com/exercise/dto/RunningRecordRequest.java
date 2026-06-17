package com.exercise.dto;


import com.exercise.entity.RunningRecord;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.apache.commons.csv.CSVRecord;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record RunningRecordRequest(
        @Positive(message = "워치 데이터(심박수)가 있는 기록만 저장합니다.")
        int meanHeartRate,

        String exerciseType,

        @NotNull(message = "시작 시간 형식이 올바르지 않거나 누락되었습니다.")
        @DateTimeFormat
        LocalDateTime startTime,

        @Positive(message = "속도는 0보다 커야 합니다.")
        Double meanSpeed,

        @Positive(message = "거리는 0보다 커야 합니다.")
        Double distance,

        @Positive(message = "칼로리는 0보다 커야 합니다.")
        Double calorie
) {
        // 날짜 파싱을 위한 포맷터 선언
        private static final DateTimeFormatter CSV_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s.SSS");

        public static RunningRecordRequest from(CSVRecord csvRecord) {

                return new RunningRecordRequest(
                        // 워치 데이터 (심박수 있는 것만)
                        parseInternalInt(csvRecord.get("com.samsung.health.exercise.mean_heart_rate")),
                        // 달리기 데이터 (달리기 : 1002)
                        csvRecord.get("com.samsung.health.exercise.exercise_type"),
                        // 운동 시작 시간(날짜 데이터와 운동한 시간 계산을 위한 데이터)
                        parseInternalDateTime(csvRecord.get("com.samsung.health.exercise.start_time")),
                        // 속도 데이터 필터링
                        parseInternalDouble(csvRecord.get("com.samsung.health.exercise.mean_speed")),
                        // 운동한 거리
                        parseInternalDouble(csvRecord.get("com.samsung.health.exercise.distance")),
                        // 칼로리
                        parseInternalDouble(csvRecord.get("com.samsung.health.exercise.calorie"))
                );
        }

        // 심박수 쓰레기 데이터 받아올 시 0으로 변환
        private static int parseInternalInt(String str) {
                return (str == null || str.isEmpty() || "0.0".equals(str)) ? 0 : (int) Double.parseDouble(str);
        }

        // 평균 속도, 거리, 칼로리 쓰레기 데이터 받아올 시 0.0으로 변환
        private static double parseInternalDouble(String str) {
                return (str == null || str.trim().isEmpty()) ? 0.0 : Double.parseDouble(str.trim());
        }

        // 시작 시간 데이터 가공
        private static LocalDateTime parseInternalDateTime(String str) {
                if (str == null || !str.contains("-")) return null;
                String cleanTime = str.trim().replaceAll("\\s+", " ");
                return LocalDateTime.parse(cleanTime, CSV_FORMATTER);
        }

        public RunningRecord toEntity() {
        return RunningRecord.builder()
                .meanHeartRate(meanHeartRate)
                .exerciseType(exerciseType)
                .startTime(startTime)
                .meanSpeed(meanSpeed)
                .distance(distance)
                .calorie(calorie)
                .build();
        }
}
