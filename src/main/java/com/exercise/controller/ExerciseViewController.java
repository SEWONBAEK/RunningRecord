package com.exercise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExerciseViewController {

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "exercise";
    }
}
