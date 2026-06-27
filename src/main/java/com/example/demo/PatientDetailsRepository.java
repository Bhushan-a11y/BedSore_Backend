package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientDetailsRepository extends JpaRepository<PatientDetails,Integer>{
    public PatientDetails findByAssignedBedId(int assignedBedId);
}
