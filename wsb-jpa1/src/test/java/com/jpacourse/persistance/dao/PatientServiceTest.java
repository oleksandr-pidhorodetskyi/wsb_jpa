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
        // given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Ala");
        patient.setLastName("Makota");
        patient.setTelephoneNumber("11223344");
        patient.setPatientNumber("P02");
        patient.setDateOfBirth(LocalDate.now());
        patient.setActive(true);
        patient = patientDao.save(patient);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Gregory");
        doctor.setLastName("House");
        doctor.setDoctorNumber("DOC-987");
        doctor.setTelephoneNumber("555-123-456");
        doctor.setSpecialization(Specialization.GP);
        entityManager.persist(doctor);

        // Przeszła wizyta - powinna być w TO
        patientDao.addVisitToPatient(
                patient.getId(),
                doctor.getId(),
                LocalDateTime.now().minusDays(3),
                "Kontrolna wizyta"
        );

        // Przyszła wizyta - nie powinna być w TO
        patientDao.addVisitToPatient(
                patient.getId(),
                doctor.getId(),
                LocalDateTime.now().plusDays(5),
                "Planowana wizyta"
        );

        entityManager.flush();
        entityManager.clear();

        // when
        PatientTO result = patientService.findById(patient.getId());

        // then
        assertNotNull(result);
        assertEquals("Ala", result.getFirstName());
        assertEquals("Makota", result.getLastName());
        assertTrue(result.isActive());

        assertNotNull(result.getPastVisits());
        assertEquals(1, result.getPastVisits().size(), "Tylko jedna wizyta powinna być przeszła");

        VisitTO visit = result.getPastVisits().get(0);
        assertEquals("Gregory", visit.getDoctorFirstName());
        assertEquals("House", visit.getDoctorLastName());
        assertEquals("Kontrolna wizyta", visit.getDescription());
    }



    @Test
    public void shouldDeletePatientAndVisitsButNotDoctors() {
        // given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Anna");
        patient.setLastName("Nowak");
        patient.setTelephoneNumber("999888777");
        patient.setPatientNumber("P100");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 20));
        patient.setActive(true);
        patient = patientDao.save(patient);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("John");
        doctor.setLastName("Smith");
        doctor.setDoctorNumber("DOC-123");
        doctor.setTelephoneNumber("111-222-333");
        doctor.setSpecialization(Specialization.DERMATOLOGIST);
        entityManager.persist(doctor);

        patientDao.addVisitToPatient(
                patient.getId(),
                doctor.getId(),
                LocalDateTime.of(2025, 4, 11, 14, 0),
                "Dermatologiczna konsultacja"
        );

        Long patientId = patient.getId();
        Long doctorId = doctor.getId();

        entityManager.flush();
        entityManager.clear();

        // when
        patientService.deleteById(patientId);
        entityManager.flush();
        entityManager.clear();

        // then
        PatientEntity deletedPatient = entityManager.find(PatientEntity.class, patientId);
        DoctorEntity remainingDoctor = entityManager.find(DoctorEntity.class, doctorId);

        assertNull(deletedPatient, "Pacjent powinien być usunięty");
        assertNotNull(remainingDoctor, "Doktor nie powinien być usunięty");

        // dodatkowa kontrola – brak wizyt pacjenta (można to też osobno wyciągać z bazy jeśli masz encję wizyt)
        // lub sprawdzić przez JPQL/HQL:
        Long countVisits = entityManager.createQuery(
                        "SELECT COUNT(v) FROM VisitEntity v WHERE v.patient.id = :patientId", Long.class)
                .setParameter("patientId", patientId)
                .getSingleResult();

        assertEquals(0L, countVisits, "Wizyty pacjenta powinny zostać usunięte");
    }
}
