package com.jpacourse.service;

import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientDao patientDao;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void shouldCreateAndReturnPatientWithVisit() {
        // given
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Gregory");
        doctor.setLastName("House");
        doctor.setDoctorNumber("DOC-987");
        doctor.setTelephoneNumber("555-123-456");
        doctor.setSpecialization(Specialization.GP);
        em.persist(doctor);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Ala");
        patient.setLastName("Makota");
        patient.setPatientNumber("P02");
        patient.setTelephoneNumber("11223344");
        patient.setDateOfBirth(LocalDate.now());
        patient.setActive(true);
        patient = patientDao.save(patient);

        patientService.addVisitToPatient(
                patient.getId(),
                doctor.getId(),
                LocalDateTime.of(2025, 4, 10, 10, 30),
                "Kontrolna wizyta"
        );

        // when
        var result = patientService.findById(patient.getId());

        // then
        assertNotNull(result);
        assertEquals("Ala", result.getFirstName());
        assertEquals("Makota", result.getLastName());
        assertEquals(1, result.getVisits().size());
        assertEquals("Gregory", result.getVisits().get(0).getDoctorFirstName());
        assertEquals("Kontrolna wizyta", result.getVisits().get(0).getDescription());
    }

    @Test
    public void shouldDeletePatientAndCascadeVisits_butKeepDoctors() {
        // given
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Lisa");
        doctor.setLastName("Cuddy");
        doctor.setDoctorNumber("DOC-222");
        doctor.setTelephoneNumber("999-888-777");
        doctor.setSpecialization(Specialization.DERMATOLOGIST);
        em.persist(doctor);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Marek");
        patient.setLastName("Nowak");
        patient.setPatientNumber("P03");
        patient.setTelephoneNumber("987654321");
        patient.setDateOfBirth(LocalDate.now().minusYears(30));
        patient.setActive(true);

        VisitEntity visit = new VisitEntity();
        visit.setDoctor(doctor);
        visit.setTime(LocalDateTime.now());
        visit.setDescription("Wizyta kontrolna");

        // ⬇️ Prawidłowe powiązanie wizyty z pacjentem
        patient.addVisit(visit);

        // ⬇️ Zapis pacjenta wraz z wizytą (kaskadowo)
        patient = patientDao.save(patient);

        em.flush();
        em.clear();

        // when
        patientService.deletePatient(patient.getId());

        // then
        PatientEntity deleted = em.find(PatientEntity.class, patient.getId());
        List<VisitEntity> remainingVisits = em.createQuery("SELECT v FROM VisitEntity v", VisitEntity.class).getResultList();
        List<DoctorEntity> remainingDoctors = em.createQuery("SELECT d FROM DoctorEntity d", DoctorEntity.class).getResultList();

        assertNull(deleted, "Pacjent powinien zostać usunięty");
        assertTrue(remainingVisits.isEmpty(), "Wszystkie wizyty pacjenta powinny zostać usunięte");
        assertFalse(remainingDoctors.isEmpty(), "Doktorzy nie powinni zostać usunięci");
    }


}
