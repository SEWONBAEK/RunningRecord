package com.exercise.service;

import com.exercise.dto.WalkingRecordRequest;
import com.exercise.entity.WalkingRecord;
import com.exercise.repository.WalkingRepository;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalkingService {

    private final WalkingRepository walkingRepository;
    private final Validator validator;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm[:ss][.SSS]");

    @Transactional
    public void importCsv(MultipartFile file) throws IOException {

        // 기존 걷기 데이터 삭제
        walkingRepository.deleteAllInBatch();

        // CSV 파일 읽기 위한 BufferdReader
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

            Map<LocalDate, WalkingRecord> totalMap = new HashMap<>();

            for(CSVRecord csvRecord : csvParser) {
                try {

                    // 걷기 데이터 아닐 시 스킵
                    String exerciseType = csvRecord.get("com.samsung.health.exercise.exercise_type");
                    if (!"1001".equals(exerciseType)) continue;

                    // WALKING DTO에 요청
                    WalkingRecordRequest dto = WalkingRecordRequest.from(csvRecord);

                    // 어노테이션 검증기 실행
                    Set<ConstraintViolation<WalkingRecordRequest>> violations = validator.validate(dto);

                    // 검증 결과 스킵된 사유 출력
                    if(!violations.isEmpty()) {
                        String errorMessages = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        System.out.println("행 스킵 사유 : " + errorMessages);
                        continue;
                    }

                    WalkingRecord currentRecord = dto.toEntity();
                    LocalDate dateKey = currentRecord.getStartTime().toLocalDate();

                    totalMap.merge(dateKey, currentRecord, (existing, replacement) -> {
                        existing.merge(replacement);
                        return existing;
                    });

                }catch (Exception e) {
                    System.out.println("에러 발생 : " + e.getMessage());
                }
            }

            List<WalkingRecord> recordsToSave = new ArrayList<>(totalMap.values());
            // 데이터 저장
            walkingRepository.saveAll(recordsToSave);
        }

    }

    // 걷기 전체 리스트 불러오기
    public List<WalkingRecord> getAllRecords() {
        return walkingRepository.findAllByOrderByStartTimeDesc();
    }
}
