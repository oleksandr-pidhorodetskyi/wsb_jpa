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
    public void shouldAddVisitToPatient() {
        // given
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Kasia");
        patient.setLastName("Testowa");
        patient.setTelephoneNumber("888999000");
        patient.setPatientNumber("PAT-01");
        patient.setDateOfBirth(LocalDateTime.now().toLocalDate());
        patient.setActive(true);
        patientDao.save(patient);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Dr");
        doctor.setLastName("Strange");
        doctor.setDoctorNumber("DOC-123"); // wymagane pole
        doctor.setTelephoneNumber("123456789"); // wymagane pole
        doctor.setSpecialization(Specialization.GP); // wymagane pole typu enum
        doctorDao.save(doctor);

        // when
        patientDao.addVisitToPatient(patient.getId(), doctor.getId(), LocalDateTime.now(), "Opis testowej wizyty");

        // then
        PatientEntity loaded = patientDao.findOne(patient.getId());
        assertNotNull(loaded.getVisits());
        assertEquals(1, loaded.getVisits().size());

        VisitEntity visit = loaded.getVisits().get(0);
        assertEquals("Opis testowej wizyty", visit.getDescription());
        assertEquals(doctor.getId(), visit.getDoctor().getId());
    }
}
