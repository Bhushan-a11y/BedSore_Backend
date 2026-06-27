package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TelemetryController {
    private final TelemetryService telemetryService;

    TelemetryController(TelemetryService telemetryService, PressureLogService pressureLogService) {
        this.telemetryService = telemetryService;
        this.pressureLogService = pressureLogService;
    }
    private final PressureLogService pressureLogService;

    @PostMapping("/api/v1/telemetry")
    public ResponseEntity<?> printData(@RequestBody BedTelemetryDTO bedTelemetryDTO) {
        bedTelemetryDTO.setTimestamp(LocalDateTime.now());
        System.out.println("Bed id : "+bedTelemetryDTO.getBedId());
        System.out.println("Back Pressure : "+bedTelemetryDTO.getBack());
        System.out.println("Timestamp : "+bedTelemetryDTO.getTimestamp() );
        System.out.println("posture : "+telemetryService.getPosture(bedTelemetryDTO));
        pressureLogService.saveData(bedTelemetryDTO);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

  

    

}
