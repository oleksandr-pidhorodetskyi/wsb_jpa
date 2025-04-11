package com.jpacourse.mapper;

import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.VisitEntity;

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
        visitTO.setDescription(entity.getDescription()); // 🛠️ dodane mapowanie

        if (entity.getDoctor() != null) {
            String fullName = entity.getDoctor().getFirstName() + " " + entity.getDoctor().getLastName();
            visitTO.setDoctorFullName(fullName);
        }

        List<String> treatments = entity.getMedicalTreatments()
                .stream()
                .map(treatment -> treatment.getType().name())
                .collect(Collectors.toList());

        visitTO.setTreatmentTypes(treatments);

        return visitTO;
    }
}
