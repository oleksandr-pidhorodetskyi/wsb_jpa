package com.jpacourse.persistance.dao;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.enums.Specialization;
import com.jpacourse.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
        // given - dane z data.sql
        Long patientId = 101L; // Jan Kowalski
        Long doctorId = 201L;  // Piotr Lewandowski

        // Dodajemy przeszłą wizytę
        patientDao.addVisitToPatient(
                patientId,
                doctorId,
                LocalDateTime.now().minusDays(5),
                "Przegląd ogólny"
        );

        // Dodajemy przyszłą wizytę (nie powinna się pojawić w TO)
        patientDao.addVisitToPatient(
                patientId,
                doctorId,
                LocalDateTime.now().plusDays(5),
                "Planowana kontrola"
        );

        // when
        PatientTO result = patientService.findById(patientId);

        // then
        assertNotNull(result);
        assertEquals("Jan", result.getFirstName());
        assertEquals("Kowalski", result.getLastName());
        assertTrue(result.isActive());

        assertNotNull(result.getPastVisits());
        assertTrue(result.getPastVisits().size() >= 1, "Powinna być co najmniej jedna przeszła wizyta");

        boolean containsOurVisit = result.getPastVisits().stream()
                .anyMatch(v -> v.getDescription().equals("Przegląd ogólny"));

        assertTrue(containsOurVisit, "W TO powinna być nasza przeszła wizyta");
    }



    @Test
    public void shouldDeletePatientAndVisitsButNotDoctors() {
        // given - dane z data.sql
        Long patientId = 102L; // Anna Nowak
        Long doctorId = 202L;  // Maria Wiśniewska

        // Sprawdzamy czy wizyty są
        Long countBefore = entityManager.createQuery(
                        "SELECT COUNT(v) FROM VisitEntity v WHERE v.patient.id = :patientId", Long.class)
                .setParameter("patientId", patientId)
                .getSingleResult();

        assertTrue(countBefore > 0, "Pacjent powinien mieć wizyty przed usunięciem");

        // when
        patientService.deleteById(patientId);

        // then
        assertNull(entityManager.find(PatientEntity.class, patientId), "Pacjent powinien być usunięty");
        assertNotNull(entityManager.find(DoctorEntity.class, doctorId), "Doktor nie powinien być usunięty");

        Long countAfter = entityManager.createQuery(
                        "SELECT COUNT(v) FROM VisitEntity v WHERE v.patient.id = :patientId", Long.class)
                .setParameter("patientId", patientId)
                .getSingleResult();

        assertEquals(0L, countAfter, "Wizyty pacjenta powinny zostać usunięte");
    }
}
