package com.it.Mujakjung_be.global.gemini.controller;

import com.it.Mujakjung_be.global.gemini.service.GeminiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GeminiController {
    private final GeminiService service;

    public GeminiController(GeminiService service) {
        this.service = service;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message){
        return service.generateTravelPlan(message);
    }
}
