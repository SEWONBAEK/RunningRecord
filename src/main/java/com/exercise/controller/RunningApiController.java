package com.exercise.controller;

import com.exercise.entity.RunningRecord;
import com.exercise.service.RunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/running/running-records")
@RequiredArgsConstructor
public class RunningApiController {

    private final RunningService runningService;

    // CSV 파일 업로드
    @PostMapping
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            runningService.importCsv(file);
            List<RunningRecord> updatedList = runningService.getAllRecords();
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedList);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("오류 발생 : " + e.getMessage());
        }
    }

    // 기록 가져오기
    @GetMapping
    public ResponseEntity<List<RunningRecord>> getRecords() {
        List<RunningRecord> records = runningService.getAllRecords();
        return ResponseEntity.ok(records);
    }
 }
