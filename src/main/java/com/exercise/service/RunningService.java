package com.exercise.service;

import com.exercise.dto.RunningRecordRequest;
import com.exercise.entity.RunningRecord;
import com.exercise.repository.RunningRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RunningService {

    private final RunningRepository runningRepository;
    private final Validator validator;

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

                    // 달리기 데이터 아닐 시 스킵
                    String exerciseType = csvRecord.get("com.samsung.health.exercise.exercise_type");
                    if (!"1002".equals(exerciseType)) continue;

                    //RUNNING DTO에 요청
                    RunningRecordRequest dto = RunningRecordRequest.from(csvRecord);

                    // 어노테이션 검증기 실행
                    Set<ConstraintViolation<RunningRecordRequest>> violations = validator.validate(dto);

                    // 검증 결과 스킵된 사유 출력
                    if(!violations.isEmpty()) {
                        String errorMessages = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        System.out.println("행 스킵 사유 : " + errorMessages);
                        continue;
                    }

                    recordsToSave.add(dto.toEntity());

                }catch (Exception e) {
                    System.out.println("에러 발생 : " + e.getMessage());
                }
            }
            // 데이터 저장
            runningRepository.saveAll(recordsToSave);
        }
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

    // 달리기 전체 데이터 불러오기
    public List<RunningRecord> getAllRecords() {
        return runningRepository.findAllByOrderByStartTimeDesc();
    }
}
