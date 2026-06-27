package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class TelemetryService {
  

	public String getPosture(BedTelemetryDTO bedTelemetryDTO) {
        String posture;
        if(bedTelemetryDTO.getLeftTop()>50&&bedTelemetryDTO.getLeftBottom()>50){
            posture = "LEFT";
            bedTelemetryDTO.setCurrentPosture(posture);
        }
        
        else if(bedTelemetryDTO.getRightTop()>50&&bedTelemetryDTO.getRightBottom()>50){
            posture = "RIGHT";
            bedTelemetryDTO.setCurrentPosture(posture);}

        else{
            posture = "BACK";
            bedTelemetryDTO.setCurrentPosture(posture);}
        return posture;
    }


}
