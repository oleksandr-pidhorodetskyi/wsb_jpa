package com.jpacourse.mapper;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class PatientMapper {
    public static PatientTO mapToTO(final PatientEntity patientEntity) {
        if(patientEntity == null) {
            return null;
        }

        final PatientTO patientTO = new PatientTO();
        patientTO.setId(patientEntity.getId());
        patientTO.setFirstName(patientEntity.getFirstName());
        patientTO.setLastName(patientEntity.getLastName());
        patientTO.setTelephoneNumber(patientEntity.getTelephoneNumber());
        patientTO.setEmail(patientEntity.getEmail());
        patientTO.setPatientNumber(patientEntity.getPatientNumber());
        patientTO.setDateOfBirth(patientEntity.getDateOfBirth());
        patientTO.setIsInsured(patientEntity.getIsInsured());

        List<VisitTO> visits = patientEntity.getVisits()
                .stream()
                .map(VisitMapper::mapToTO)
                .collect(Collectors.toList());
        patientTO.setVisits(visits);

        return  patientTO;
    }

    public static PatientEntity mapToEntity(final PatientTO patientTO) {
        if (patientTO == null) {
            return null;
        }

        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(patientTO.getId());
        patientEntity.setFirstName(patientTO.getFirstName());
        patientEntity.setLastName(patientTO.getLastName());
        patientEntity.setTelephoneNumber(patientTO.getTelephoneNumber());
        patientEntity.setEmail(patientTO.getEmail());
        patientEntity.setPatientNumber(patientTO.getPatientNumber());
        patientEntity.setDateOfBirth(patientTO.getDateOfBirth());
        patientEntity.setIsInsured(patientTO.getIsInsured());

        List<VisitEntity> visits = patientTO.getVisits()
                .stream()
                .map(VisitMapper::mapToEntity)
                .collect(Collectors.toList());

        // ustaw relację dwukierunkową (jeśli potrzebna):
        visits.forEach(v -> v.setPatient(patientEntity));
        patientEntity.setVisits(visits);

        return patientEntity;
    }
}
