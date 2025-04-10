package com.jpacourse.persistance.dao;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void shouldReturnFullPatientTOWithVisits() {
        // given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Ala");
        patient.setLastName("Makota");
        patient.setTelephoneNumber("11223344");
        patient.setPatientNumber("P02");
        patient.setDateOfBirth(LocalDateTime.now().toLocalDate());
        patient.setActive(true);
        patient = patientDao.save(patient);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Gregory");
        doctor.setLastName("House");
        entityManager.persist(doctor); // zamiast DoctorDao

        patientDao.addVisitToPatient(
                patient.getId(),
                doctor.getId(),
                LocalDateTime.of(2025, 4, 10, 10, 30),
                "Kontrolna wizyta"
        );

        // when
        PatientTO result = patientService.findById(patient.getId());

        // then
        assertNotNull(result);
        assertEquals("Ala", result.getFirstName());
        assertEquals("Makota", result.getLastName());
        assertTrue(result.isActive());
        assertNotNull(result.getVisits());
        assertEquals(1, result.getVisits().size());

        assertTrue(result.getVisits().get(0).getDoctorFullName().contains("Gregory"));
        assertEquals("Kontrolna wizyta", result.getVisits().get(0).getDescription());
    }
}
