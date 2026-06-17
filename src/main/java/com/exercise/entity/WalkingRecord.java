package com.exercise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "walking_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;                     // auto increment (자동 생성)
    private String exerciseType;        // 운동 타입(걷기 : 1001)
    private LocalDateTime startTime;    // 시작 시간
    private LocalDateTime endTime;      // 종료 시간
    private int count;                  // 걸음수
    private Double distance;            // 거리(단위, m)
    private Double calorie;             // 칼로리(kcal)

    // ------------ 가공 필드 ------------
    private long durationSeconds;       // 운동 시간(초 단위로 계산 저장)

    @Builder
    public WalkingRecord(String exerciseType, LocalDateTime startTime, LocalDateTime endTime, int count, Double distance, Double calorie) {
        this.exerciseType = exerciseType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
        this.distance = distance;
        this.calorie = calorie;
    }

    public void calculateDerivedFields() {
        if(this.startTime != null  && this.endTime != null && this.durationSeconds == 0) {
            this.durationSeconds = Duration.between(this.startTime, this.endTime).toSeconds();
        }
    }

    public void merge(WalkingRecord other) {
        this.count += other.getCount();
        this.distance += other.getDistance();
        this.calorie += other.getCalorie();

        other.calculateDerivedFields();
        this.calculateDerivedFields();

        this.durationSeconds += other.getDurationSeconds();

        // 해당 날짜 걷기 처음 시작 시간과 마지막 걷기 시간 저장
        if (other.getStartTime().isBefore(this.startTime)) this.startTime = other.getStartTime();
        if (other.getEndTime().isAfter(this.endTime)) this.endTime = other.getEndTime();
    }

    @PrePersist
    public void prePersist() {
        calculateDerivedFields();
    }

}
