package com.jpacourse.mapper;

import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.VisitEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public final class VisitMapper {

    public static VisitTO mapToTO(VisitEntity entity) {
        if (entity == null) {
            return null;
        }

        VisitTO visitTO = new VisitTO();
        visitTO.setId(entity.getId());
        visitTO.setDateTime(entity.getTime());
        visitTO.setDescription(entity.getDescription());

        if (entity.getDoctor() != null) {
            visitTO.setDoctorFirstName(entity.getDoctor().getFirstName());
            visitTO.setDoctorLastName(entity.getDoctor().getLastName());
        }

        if (entity.getMedicalTreatments() != null) {
            List<String> treatments = entity.getMedicalTreatments()
                    .stream()
                    .map(treatment -> treatment.getType().name())
                    .collect(Collectors.toList());

            visitTO.setTreatmentTypes(treatments);
        }

        // Sprawdzenie, czy wizyta się już odbyła (czy jest w przeszłości)
        visitTO.setPast(entity.getTime().isBefore(LocalDateTime.now()));

        return visitTO;
    }
}
