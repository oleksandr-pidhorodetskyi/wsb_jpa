package com.jpacourse.mapper;

import com.jpacourse.dto.MedicalTreatmentTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.MedicalTreatmentEntity;
import com.jpacourse.persistance.entity.VisitEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class VisitMapper {

    public static VisitTO mapToTO(final VisitEntity visitEntity) {

        if(visitEntity == null) {
            return null;
        }

        final VisitTO visitTO = new VisitTO();
        visitTO.setId(visitEntity.getId());
        visitTO.setTime(visitEntity.getTime());
        visitTO.setFirstNameOfDoctor(visitEntity.getDoctor().getFirstName());
        visitTO.setLastNameOfDoctor(visitEntity.getDoctor().getLastName());
        List<MedicalTreatmentTO> treatmentTOs = visitEntity.getTreatments()
                .stream()
                .map(MedicalTreatmentMapper::mapToTO)
                .collect(Collectors.toList());
        visitTO.setTreatments((treatmentTOs));

        return visitTO;
    }

    public static VisitEntity mapToEntity(final VisitTO visitTO) {
        if (visitTO == null) {
            return null;
        }

        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setId(visitTO.getId());
        visitEntity.setTime(visitTO.getTime());

        //DoctorEntity trzeba przypisać w serwisie

        if (visitTO.getTreatments() != null) {
            List<MedicalTreatmentEntity> treatmentEntities = visitTO.getTreatments()
                    .stream()
                    .map(MedicalTreatmentMapper::mapToEntity)
                    .collect(Collectors.toList());
            visitEntity.setTreatments(treatmentEntities);
        }

        return visitEntity;
    }
}
