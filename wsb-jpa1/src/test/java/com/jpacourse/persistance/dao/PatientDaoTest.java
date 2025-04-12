package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.PatientEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private VisitDao visitDao;

    @Autowired
    private DoctorDao doctorDao;

    @Test
    public void testAddVisitToPatient_shouldAddNewVisitCascaded() {
        // given
        Long patientId = 102L;
        Long doctorId = 202L;
        LocalDateTime visitTime = LocalDateTime.of(2024, 4, 15, 12, 0);
        String description = "Nowa wizyta testowa";

        // when
        patientDao.addVisitToPatient(patientId, doctorId, visitTime, description);

        // then
        PatientEntity updatedPatient = patientDao.findOne(patientId);
        assertNotNull(updatedPatient);
        assertTrue(updatedPatient.getVisits().stream()
                .anyMatch(v -> description.equals(v.getDescription()) && visitTime.equals(v.getTime())));

        boolean found = visitDao.findAll().stream()
                .anyMatch(v -> v.getPatient().getId().equals(patientId) &&
                        v.getDoctor().getId().equals(doctorId) &&
                        description.equals(v.getDescription()));
        assertTrue(found);
    }
}
