package com.example.demo;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int patientId;
    String pName;
    int age;
    String skinCondition;
    String diseaseDiagonosed;
    LocalDateTime admitTime;
    int assignedBedId;


}
