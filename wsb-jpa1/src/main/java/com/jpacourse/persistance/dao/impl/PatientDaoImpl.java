package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.DoctorDao;
import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {
    @Autowired
    private DoctorDao doctorDao;

    @Override
    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime time, String description) {
        PatientEntity patientEntity = findOne(patientId);
        DoctorEntity doctorEntity = doctorDao.findOne(doctorId);

        if (patientEntity == null || doctorEntity == null) {
            throw new IllegalArgumentException("Patient or Doctor not found");
        }

        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setDescription(description);
        visitEntity.setTime(time);
        visitEntity.setPatient(patientEntity);
        visitEntity.setDoctor(doctorEntity);

        patientEntity.getVisits().add(visitEntity);
        entityManager.merge(patientEntity);
    }
}
