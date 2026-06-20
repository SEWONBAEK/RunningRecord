package com.exercise.controller;

import com.exercise.dto.WeatherResponse;
import com.exercise.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherApiController {

    private final WeatherService weatherService;

    public WeatherApiController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<?> getWeatherAndDust(@RequestParam double lat, @RequestParam double lon) {

        WeatherResponse response = weatherService.getWeatherData(lat, lon);

        return ResponseEntity.ok(response);

    }
}
