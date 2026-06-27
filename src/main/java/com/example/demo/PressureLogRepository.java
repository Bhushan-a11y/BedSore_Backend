package com.example.demo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PressureLogRepository extends JpaRepository<PressureLog,Long> {
    List<PressureLog> findAllByPatientIdOrderByTimestampDesc(int patientId);
}
