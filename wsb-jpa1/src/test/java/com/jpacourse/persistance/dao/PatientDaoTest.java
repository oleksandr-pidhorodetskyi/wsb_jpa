package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
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
    private DoctorDao doctorDao;

    @Test
    public void shouldAddVisitToPatientAndCascadeUpdate() {
        // given
        // Tworzymy pacjenta
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Kasia");
        patient.setLastName("Testowa");
        patient.setTelephoneNumber("888999000");
        patient.setPatientNumber("PAT-01");
        patient.setDateOfBirth(LocalDateTime.now().toLocalDate());
        patient.setActive(true);
        patient = patientDao.save(patient);  // Pacjent zapisany do bazy

        // Tworzymy doktora
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Dr");
        doctor.setLastName("Strange");
        doctor.setDoctorNumber("DOC-123");
        doctor.setTelephoneNumber("123456789");
        doctor.setSpecialization(Specialization.GP);
        doctor = doctorDao.save(doctor);  // Doktor zapisany do bazy

        // when
        // Dodajemy wizytę dla pacjenta
        patientDao.addVisitToPatient(patient.getId(), doctor.getId(), LocalDateTime.now(), "Opis testowej wizyty");

        // then
        // Ładujemy pacjenta z bazy i sprawdzamy wizyty
        PatientEntity loadedPatient = patientDao.findOne(patient.getId());
        assertNotNull(loadedPatient.getVisits(), "Lista wizyt pacjenta nie powinna być null");
        assertEquals(1, loadedPatient.getVisits().size(), "Pacjent powinien mieć dokładnie jedną wizytę");

        // Pobieramy pierwszą wizytę i sprawdzamy szczegóły
        VisitEntity visit = loadedPatient.getVisits().get(0);
        assertNotNull(visit, "Wizyta powinna być przypisana do pacjenta");
        assertEquals("Opis testowej wizyty", visit.getDescription(), "Opis wizyty powinien być zgodny");
        assertEquals(doctor.getId(), visit.getDoctor().getId(), "ID doktora przypisanego do wizyty powinno być zgodne");

        // Sprawdzamy, czy pacjent został poprawnie zapisany (merge powinno nastąpić)
        assertTrue(loadedPatient.getVisits().contains(visit), "Pacjent powinien zawierać tę wizytę");
    }
}
