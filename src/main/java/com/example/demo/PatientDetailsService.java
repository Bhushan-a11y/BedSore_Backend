package com.example.demo;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class PatientDetailsService {
    private final PatientDetailsRepository patientDetailsRepository;

    PatientDetailsService(PatientDetailsRepository patientDetailsRepository) {
        this.patientDetailsRepository = patientDetailsRepository;
    }

    public PatientDetails createPatient(PatientDetails patientDetails) {
        PatientDetails pd = new PatientDetails();
        pd.setPName(patientDetails.getPName());
        pd.setAge(patientDetails.getAge());
        pd.setSkinCondition(patientDetails.getSkinCondition());
        pd.setDiseaseDiagonosed(patientDetails.getDiseaseDiagonosed());
        pd.setAssignedBedId(patientDetails.getAssignedBedId());
        pd.setAdmitTime(LocalDateTime.now());
        patientDetailsRepository.save(pd);
        return pd;

    }
   

}
