package com.jpacourse.persistance.dao;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.service.PatientService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private VisitDao visitDao;

    @Autowired
    private DoctorDao doctorDao;

    @Test
    public void testFindById_shouldReturnCorrectPatientTO_withPastVisitsOnly() {
        // given
        Long patientId = 101L;

        // when
        PatientTO patient = patientService.findById(patientId);

        // then
        assertNotNull(patient);
        assertEquals("Jan", patient.getFirstName());
        assertTrue(patient.getIsInsured());

        assertNotNull(patient.getVisits());
        assertFalse(patient.getVisits().isEmpty());

        for (VisitTO visit : patient.getVisits()) {
            assertTrue(visit.getTime().isBefore(LocalDateTime.now()));
            assertNotNull(visit.getFirstNameOfDoctor());
            assertNotNull(visit.getLastNameOfDoctor());
            assertNotNull(visit.getTreatments());
            assertFalse(visit.getTreatments().isEmpty());
        }
    }

    @Test
    public void testDeletePatient_shouldCascadeDeleteVisitsButNotDoctors() {
        // given
        Long patientId = 101L;
        Long visitId = 301L;
        Long doctorId = 201L;

        assertTrue(patientDao.exists(patientId));
        assertNotNull(visitDao.findOne(visitId));
        assertNotNull(doctorDao.findOne(doctorId));

        // when
        patientService.delete(patientId);

        // then
        assertFalse(patientDao.exists(patientId));
        assertNull(visitDao.findOne(visitId));
        assertNotNull(doctorDao.findOne(doctorId)); // lekarz nie został usunięty
    }
}
