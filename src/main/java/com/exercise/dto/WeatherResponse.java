package com.exercise.dto;

public record WeatherResponse(
        // 오늘 데이터
        String todaySky,
        String todayREH,
        String todayTMN,
        String todayTMX,

        // 내일 데이터
        String tomorrowSky,
        String tomorrowREH,
        String tomorrowTMN,
        String tomorrowTMX,

        // 모레 데이터
        String dayAfterSky,
        String dayAfterREH,
        String dayAfterTMN,
        String dayAfterTMX,

        // 미세먼지 데이터
        String pm10Grade, String pm10Value,
        String pm25Grade, String pm25Value

) {

}
