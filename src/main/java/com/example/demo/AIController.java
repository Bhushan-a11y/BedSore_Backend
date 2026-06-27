package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController

public class AIController {
    private final PressureLogService pressureLogService;
    private final AIService aiService;

    AIController(AIService aiService, PressureLogService pressureLogService) {
        this.aiService = aiService;
        this.pressureLogService = pressureLogService;
    }

    @GetMapping("/ai")
    public String test()
    {
        return aiService.generateRecommendation("How to prevent Bed Sores ?");
    }
    @GetMapping("/trigger-test")
public String triggerManualCheck() {
    // This calls your scheduled method manually just for testing!
    pressureLogService.checkPatientPosture();
    return "Manual check triggered! Check your server console for logs.";
}

}
