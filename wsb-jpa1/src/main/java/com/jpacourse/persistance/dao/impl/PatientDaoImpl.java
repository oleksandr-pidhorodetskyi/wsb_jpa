package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PatientDaoImpl implements PatientDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public PatientEntity findOne(Long id) {
        return em.find(PatientEntity.class, id);
    }

    @Override
    public List<PatientEntity> findAll() {
        return List.of();
    }

    @Override
    public PatientEntity update(PatientEntity entity) {
        return null;
    }

    @Override
    public void delete(PatientEntity entity) {

    }

    @Override
    public PatientEntity save(PatientEntity entity) {
        if (entity.getId() == null) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    public PatientEntity getOne(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {
        PatientEntity entity = em.find(PatientEntity.class, id);
        if (entity != null) {
            // 👇 To JEST KLUCZOWE — usuwamy wszystkie wizyty z listy pacjenta
            entity.getVisits().clear();

            // Teraz Hibernate rozpozna orphany i je usunie
            em.remove(entity);
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime time, String description) {
        PatientEntity patient = em.find(PatientEntity.class, patientId);
        DoctorEntity doctor = em.find(DoctorEntity.class, doctorId);

        if (patient != null && doctor != null) {
            VisitEntity visit = new VisitEntity();
            visit.setDescription(description);
            visit.setTime(time);
            visit.setDoctor(doctor);

            patient.addVisit(visit); // dwustronne powiązanie
            em.persist(visit);
        }
    }

    @Override
    public boolean exists(Long id) {
        return em.find(PatientEntity.class, id) != null;
    }
}
