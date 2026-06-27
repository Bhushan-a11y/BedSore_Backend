package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDetailsDTO {
    private int patientId;
    private String patientName;
    private String currentPosture;
    private LocalDateTime lastMovedAt;
    private long minutesElapsed; 
}