package com.exercise.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter@Table(name = "running_data")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunningRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;                     // auto increment (자동 생성)
    private int meanHeartRate;          // 평균 심박수
    private String exerciseType;        // 운동 타입(달리기 : 1002)
    private LocalDateTime startTime;    // 시작 시간
    private Double distance;            // 거리(단위, m)
    private Double calorie;             // 칼로리(kcal)

    // DB에 저장되지 않고 계산을 위해 받는 값
    @Transient
    private Double meanSpeed;           // 평균 속도(m/s)

    // ------------ 가공 필드 ------------
    private Long durationSeconds;       // 운동 시간(초 단위로 계산 저장)
    private String pace;                // 평균 페이스(예 : 1km당 5'30")

    @Builder
    public RunningRecord(int meanHeartRate, String exerciseType, LocalDateTime startTime, Double meanSpeed, Double distance, Double calorie) {
        this.meanHeartRate = meanHeartRate;
        this.exerciseType = exerciseType;
        this.startTime = startTime;
        this.meanSpeed = meanSpeed;
        this.distance = distance;
        this.calorie = calorie;
    }

    // CSV 데이터 받아온 후 운동 시간과 페이스 계산
    public void calculateDerivedFields() {
        // 운동 시간 계산
        if (this.meanSpeed != null && this.meanSpeed > 0 && this.distance != null) {
            // 시간 = 거리 / 속도
            this.durationSeconds = Math.round(this.distance / this.meanSpeed);
        }

        // 페이스 계산(분/km)
        if(this.distance != null && this.distance > 0 && this.durationSeconds != null && this.durationSeconds > 0) {
            double km = this.distance / 1000.0;                             // m -> km로 변환
            double totalMinutes = (double) this.durationSeconds / 60.0;     // s -> m로 변환
            double paceDecimal = totalMinutes / km;                         // 1km당 걸린 시간(분)

            int minutes = (int) paceDecimal;                                // 앞자리(분)
            int seconds = (int) Math.round((paceDecimal - minutes) * 60);   // 뒷자리(초)

            // 분당 페이스로 변환
            this.pace = String.format("%d'%02d\"", minutes, seconds);
        } else {
            this.pace = "0'00\"";
        }
    }

    // DB 저장되기 전에 자동 계산
    @PrePersist
    public void prePersist() {
        calculateDerivedFields();
    }

}
