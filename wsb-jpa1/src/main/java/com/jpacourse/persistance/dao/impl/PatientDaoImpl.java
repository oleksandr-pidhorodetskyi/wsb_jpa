package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime time, String description) {
        PatientEntity patient = em.find(PatientEntity.class, patientId);
        DoctorEntity doctor = em.find(DoctorEntity.class, doctorId);

        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient or doctor not found");
        }

        VisitEntity visit = new VisitEntity();
        visit.setTime(time);
        visit.setDescription(description);
        visit.setDoctor(doctor);
        visit.setPatient(patient);

        if (patient.getVisits() == null) {
            patient.setVisits(new ArrayList<>());
        }

        patient.getVisits().add(visit);

        // Kaskadowy zapis wizyty
        em.merge(patient);
    }
}
