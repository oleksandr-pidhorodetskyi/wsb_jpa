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
        // Zapisany pacjent
        Long patientId = 101L; // Jan Kowalski
        Long doctorId = 201L;  // Piotr Lewandowski

        // when
        patientDao.addVisitToPatient(patientId, doctorId, LocalDateTime.now(), "Nowa wizyta testowa");

        // then
        PatientEntity loadedPatient = patientDao.findOne(patientId);
        assertNotNull(loadedPatient, "Pacjent powinien istnieć");

        // Sprawdzamy czy pacjent ma co najmniej jedną wizytę (w tym nową)
        assertNotNull(loadedPatient.getVisits(), "Lista wizyt pacjenta nie powinna być null");
        assertTrue(loadedPatient.getVisits().size() > 0, "Pacjent powinien mieć co najmniej jedną wizytę");

        VisitEntity addedVisit = loadedPatient.getVisits().stream()
                .filter(v -> "Nowa wizyta testowa".equals(v.getDescription()))
                .findFirst()
                .orElse(null);

        assertNotNull(addedVisit, "Nowa wizyta powinna zostać dodana do pacjenta");
        assertEquals(doctorId, addedVisit.getDoctor().getId(), "Wizyta powinna być przypisana do właściwego doktora");
    }

}
