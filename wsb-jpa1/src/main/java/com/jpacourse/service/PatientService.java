package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistance.entity.PatientEntity;

public interface PatientService {
    PatientTO findById(Long id);
    void deleteById(Long id);
    PatientEntity findByIdWithVisits(Long patientId);
}
