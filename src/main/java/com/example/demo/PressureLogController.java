package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PressureLogController {
    @Autowired
    private PressureLogService pressureLogService;
    private final PressureLogRepository pressureLogRepository;

    PressureLogController(PressureLogRepository pressureLogRepository) {
        this.pressureLogRepository = pressureLogRepository;
    }
    @GetMapping("/patientHistory/{patientId}")
    public ResponseEntity<?> getHistory(@PathVariable int patientId)
    {
        List<PressureLog> log = pressureLogRepository.findAllByPatientIdOrderByTimestampDesc(patientId);
        if(!log.isEmpty())
        {
            return new ResponseEntity<>(log,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/duration/{patientID}")
    public ResponseEntity<?> getDur(@PathVariable int patientID)
    {
        PatientDetailsDTO patientDetailsDTO = pressureLogService.getDuration(patientID);
        if(patientDetailsDTO!=null)
        {
            return new ResponseEntity<>(patientDetailsDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
