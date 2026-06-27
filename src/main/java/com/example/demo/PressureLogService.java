package com.example.demo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PressureLogService {
    private final AIService aiService;
    private final PatientDetailsRepository patientDetailsRepository;
    private final ConcurrentHashMap<Integer, Long> lastAlertedMinute = new ConcurrentHashMap<>();

    PressureLogService(PatientDetailsRepository patientDetailsRepository, PressureLogRepository pressureLogRepository, AIService aiService) {
        this.patientDetailsRepository = patientDetailsRepository;
        this.pressureLogRepository = pressureLogRepository;
      this.aiService = aiService;
    }
    private final PressureLogRepository pressureLogRepository;
    @Async("hospitalExecutor")
public void saveData(BedTelemetryDTO bedTelemetryDTO) {
    System.out.println("DEBUG: Service saveData started!");

    String currentPosture = bedTelemetryDTO.getCurrentPosture();
    System.out.println("DEBUG: Posture received in Service is: " + currentPosture);

    PatientDetails patientDetails = patientDetailsRepository.findByAssignedBedId(bedTelemetryDTO.getBedId());
    
    if (patientDetails == null) {
        System.out.println("DEBUG: FAILED! Patient not found for bed " + bedTelemetryDTO.getBedId());
        return; 
    }

    // 1. Fetch the GUARANTEED safe List instead of the Optional box
    List<PressureLog> logs = pressureLogRepository.findAllByPatientIdOrderByTimestampDesc(patientDetails.getPatientId());
    
    // 2. Check if the list has items (!isEmpty), and if so, look at index 0 (the newest log)
    if (!logs.isEmpty() && currentPosture != null && currentPosture.equals(logs.get(0).getPosture())) {
        System.out.println("DEBUG: Posture unchanged, ignoring to protect database.");
        return;
    }

    PressureLog pressureLog = new PressureLog();
    pressureLog.setPatientId(patientDetails.getPatientId());
    pressureLog.setPosture(currentPosture);
    
    // Spring Boot acts as the clock!
    pressureLog.setTimestamp(LocalDateTime.now()); 
    
    pressureLogRepository.save(pressureLog);
    System.out.println("DEBUG: SUCCESS! Saved new posture to TiDB.");
}


    public PatientDetailsDTO getDuration(int patientId) {
    Optional<PatientDetails> patientOpt = patientDetailsRepository.findById(patientId);
    
    if (patientOpt.isEmpty()) {
        return null; 
    }
    PatientDetails patientDetails = patientOpt.get();


    List<PressureLog> logOpt = pressureLogRepository.findAllByPatientIdOrderByTimestampDesc(patientDetails.getPatientId());
 
    if (logOpt.isEmpty()) {
        return new PatientDetailsDTO(patientId, patientDetails.getPName(), "NO_DATA", null, 0);
    }
    
    PressureLog lastLog = logOpt.getFirst();

   
    LocalDateTime logTime = lastLog.getTimestamp();
    LocalDateTime currentTime = LocalDateTime.now();
    long timeElapsed = Duration.between(logTime, currentTime).toMinutes();


    return new PatientDetailsDTO(
            patientId,
            patientDetails.getPName(),
            lastLog.getPosture(),
            logTime,
            timeElapsed
    );
}
@Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void checkPatientPosture() {
        List<PatientDetails> patients = patientDetailsRepository.findAll();
        
        for (PatientDetails p : patients) {
            PatientDetailsDTO dto = getDuration(p.getPatientId());
            if (dto == null || dto.getMinutesElapsed() == 0) continue;
            
            long elapsed = dto.getMinutesElapsed();
            
            // Logic: Every 25 minutes (25, 50, 75...)
            if (elapsed > 0 && elapsed % 25 == 0) {
                // The Bouncer: check if we already alerted for this specific milestone
                if (lastAlertedMinute.getOrDefault(p.getPatientId(), -1L) != elapsed) {
                    
                    List<PressureLog> logs = pressureLogRepository.findAllByPatientIdOrderByTimestampDesc(p.getPatientId());
                    
                    if (!logs.isEmpty()) {
                        String prompt = buildPrompt(p, logs);
                        String recommendation = aiService.generateRecommendation(prompt);
                        
                        // Update the Bouncer so we don't spam
                        lastAlertedMinute.put(p.getPatientId(), elapsed);
                        
                        System.out.println("AI ALERT FOR PATIENT " + p.getPatientId() + ": " + recommendation);
                    }
                }
            }
        }
    }
private String buildPrompt(PatientDetails patient, List<PressureLog> logs) {
    // 1. Start with a persona (Tell the AI who it is)
    String persona = "You are a senior hospital head nurse. ";

    // 2. Give it the Context (The raw data from your database)
    String context = "The patient is " + patient.getPName() + 
                     ", aged " + patient.getAge() + 
                     ", with a skin condition of: " + patient.getSkinCondition() + ". " +
                     "The patient has been in the '" + logs.get(0).getPosture() + 
                     "' position for " + getDuration(patient.getPatientId()) + " minutes.";

    // 3. Give it the Task (What you want it to do)
    String task = "Based on this, tell me in exactly one sentence if this is dangerous " +
                  "and what the nurse should do next.";

    // Combine it all into one giant string
    return persona + context + task;
}

}
    


