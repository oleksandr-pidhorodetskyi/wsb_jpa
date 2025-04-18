package com.jpacourse.mapper;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.PatientEntity;

import java.time.LocalDateTime;
import java.util.List;

public final class PatientMapper {

    public static PatientTO mapToTO(PatientEntity entity) {
        if (entity == null) {
            return null;
        }

        PatientTO to = new PatientTO();
        to.setId(entity.getId());
        to.setFirstName(entity.getFirstName());
        to.setLastName(entity.getLastName());
        to.setTelephoneNumber(entity.getTelephoneNumber());
        to.setEmail(entity.getEmail());
        to.setPatientNumber(entity.getPatientNumber());
        to.setDateOfBirth(entity.getDateOfBirth());
        to.setActive(entity.isActive());

        List<VisitTO> pastVisits = entity.getVisits().stream()
                .filter(visit -> visit.getTime().isBefore(LocalDateTime.now()))
                .map(VisitMapper::mapToTO)
                .toList();

        to.setPastVisits(pastVisits);

        return to;
    }
}
