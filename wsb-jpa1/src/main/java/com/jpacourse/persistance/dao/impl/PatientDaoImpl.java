package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.DoctorDao;
import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {

    @Autowired
    private DoctorDao doctorDao;

    @Override
    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime time, String description) {
        // Use the findOne method from AbstractDao
        PatientEntity patient = findOne(patientId);  // Assuming findOne() is provided by AbstractDao
        DoctorEntity doctor = doctorDao.findOne(doctorId); // Assuming doctorDao.findOne() works

        // Null check for patient and doctor
        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient or doctor not found");
        }

        // Create a new visit
        VisitEntity visit = new VisitEntity();
        visit.setTime(time);
        visit.setDescription(description);
        visit.setDoctor(doctor);
        visit.setPatient(patient);

        // Ensure the patient's visits list is initialized
        if (patient.getVisits() == null) {
            patient.setVisits(new ArrayList<>());
        }

        // Add the visit to the patient's visit list
        patient.getVisits().add(visit);

        // Call the update method from AbstractDao (assuming cascading saves are handled)
        update(patient);
    }

    @Override
    public List<PatientEntity> findByLastName(String lastName) {
        String jpql = "SELECT p FROM PatientEntity p WHERE p.lastName = :lastName";

        TypedQuery<PatientEntity> query = entityManager.createQuery(jpql, PatientEntity.class);
        query.setParameter("lastName", lastName);

        return query.getResultList();
    }

    @Override
    public List<PatientEntity> findPatientsWithMoreThanXVisits(int x) {
        String jpql = "SELECT p FROM PatientEntity p WHERE SIZE(p.visits) > :x";  // SIZE(p.visits) liczy liczbę wizyt pacjenta
        TypedQuery<PatientEntity> query = entityManager.createQuery(jpql, PatientEntity.class);
        query.setParameter("x", x);
        return query.getResultList();
    }

    @Override
    public List<PatientEntity> findInactivePatientsUsingNegation() {
        String jpql = "SELECT p FROM PatientEntity p WHERE p.active <> true";
        TypedQuery<PatientEntity> query = entityManager.createQuery(jpql, PatientEntity.class);
        return query.getResultList();
    }

}
