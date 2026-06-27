package com.example.demo;

import java.time.LocalDateTime;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BedTelemetryDTO {
    @Id
    int bedId;
    int back;
    int leftTop;
    int leftBottom;
    int rightTop;
    int rightBottom;
    String currentPosture;
    LocalDateTime timestamp;


}
