package com.exercise.controller;


import com.exercise.entity.WalkingRecord;
import com.exercise.service.WalkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/walking/walking-records")
@RequiredArgsConstructor
public class WalkingApiController {

    private final WalkingService walkingService;

    @PostMapping
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            walkingService.importCsv(file);
            List<WalkingRecord> updatedList = walkingService.getAllRecords();
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("오류 발생 : " + e.getMessage());
        }
    }

    // 기록 가져오기
    @GetMapping
    public ResponseEntity<List<WalkingRecord>> getRecords() {
        List<WalkingRecord> records = walkingService.getAllRecords();
        return ResponseEntity.ok(records);
    }

}
