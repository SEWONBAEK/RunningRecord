package com.run.running.service;

import com.run.running.entity.RunningRecord;
import com.run.running.repository.RunningRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RunningService {

    private final RunningRepository runningRepository;

    // 여러 종류의 날짜 형식을 저장하기 위한 포맷터 리스트 (start_time용 대비)
    private final DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm:ss a", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    };

    @Transactional
    public void importCsv(MultipartFile file) throws IOException {

        // 기존 러닝 기록 삭제
        runningRepository.deleteAllInBatch();

        // CSV 파일 읽기 위한 BufferedReader
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
        );

        reader.readLine();

        // CSVFormat Builder 사용
        CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        try (CSVParser csvParser = csvFormat.parse(reader)) {
            List<RunningRecord> recordsToSave = new ArrayList<>();

            for(CSVRecord csvRecord : csvParser) {
                try {
                    String startTimeStr = csvRecord.get("com.samsung.health.exercise.start_time");
                    if (startTimeStr == null || !startTimeStr.contains("-")) continue;
                    // 달리기 데이터 필터링 (달리기 : 1002)
                    String exerciseType = csvRecord.get("com.samsung.health.exercise.exercise_type");
                    if (!"1002".equals(exerciseType)) continue;

                    // 워치 데이터만 필터링 (심박수 있는 것만)
                    int meanHeartRate = parseHeartRate(csvRecord.get("com.samsung.health.exercise.mean_heart_rate"));
                    if (meanHeartRate <= 0) continue;

                    // 속도 데이터 필터링
                    String speedStr = csvRecord.get("com.samsung.health.exercise.mean_speed");
                    double meanSpeed = (speedStr != null && !speedStr.isEmpty()) ? Double.parseDouble(speedStr) : 0.0;
                    if (meanSpeed <= 0) continue;

                    // 시작 시간 공백 정리 및 파싱
                    String startClean = startTimeStr.trim().replaceAll("\\s+", " ");
                    LocalDateTime startDT = parseFullDateTime(startClean);

                    // Entity 생성
                    RunningRecord record = RunningRecord.builder()
                            .exerciseType(exerciseType)
                            .startTime(startDT)
                            .meanSpeed(meanSpeed)
                            .distance(Double.parseDouble(csvRecord.get("com.samsung.health.exercise.distance")))
                            .calorie(Double.parseDouble(csvRecord.get("com.samsung.health.exercise.calorie")))
                            .meanHeartRate(meanHeartRate)
                            .build();

                    recordsToSave.add(record);
                }catch (Exception e) {
                    System.out.println("줄 파싱 중 에러: " + e.getMessage());
                    continue;
                }
            }
            // 데이터 저장
            runningRepository.saveAll(recordsToSave);
        }
    }

    // 다양한 날짜 형식을 시도하며 파싱하는 메서드
    private LocalDateTime parseFullDateTime(String dateTimeStr) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateTimeStr, formatter);
            } catch (Exception ignored) { }
        }
        throw new IllegalArgumentException("지원하지 않는 날짜 형식입니다: " + dateTimeStr);
    }

    public List<RunningRecord> getAllRecords() {
        return runningRepository.findAllByOrderByStartTimeDesc();
    }

    private int parseHeartRate(String heartRateStr) {
        try {
            if (heartRateStr == null || heartRateStr.isEmpty() || "0.0".equals(heartRateStr)) {
                return 0;
            }
            return (int) Double.parseDouble(heartRateStr);
        }catch (NumberFormatException e) {
            return 0;
        }
    }
}
