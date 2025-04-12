package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;

import java.time.LocalDateTime;

public interface PatientService {

    void deleteById(Long id);
    void deletePatient(Long id);
    void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime time, String description);
    PatientTO findById(Long id);
}
