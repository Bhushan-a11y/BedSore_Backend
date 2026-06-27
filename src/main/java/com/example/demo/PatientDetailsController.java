package com.example.demo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientDetailsController {
    private final PatientDetailsService patientDetailsService;

    PatientDetailsController(PatientDetailsService patientDetailsService) {
        this.patientDetailsService = patientDetailsService;
    }

    @PostMapping("/patient")
    public ResponseEntity<?> createPatient(@RequestBody PatientDetails patientDetails)
    {
        PatientDetails pd = patientDetailsService.createPatient(patientDetails);
        if(pd!=null)
        {
            return new ResponseEntity<>(pd,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
