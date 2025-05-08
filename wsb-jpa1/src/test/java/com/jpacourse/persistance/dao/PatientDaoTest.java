package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

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

    @Test
    public void shouldFindPatientsByLastName() {
        // given
        String lastName = "Kowalski"; // Przykładowe nazwisko

        // when
        List<PatientEntity> foundPatients = patientDao.findByLastName(lastName);

        // then
        assertNotNull(foundPatients, "Lista pacjentów nie powinna być null");
        assertFalse(foundPatients.isEmpty(), "Lista pacjentów nie powinna być pusta");

        // Sprawdzamy, czy pacjenci mają odpowiednie nazwisko
        for (PatientEntity patient : foundPatients) {
            assertEquals(lastName, patient.getLastName(), "Nazwisko pacjenta powinno być równe: " + lastName);
        }
    }

    @Test
    public void shouldFindPatientsWithMoreThanXVisits() {
        // given
        int x = 2;  // patients with more than 2 visits

        // when
        List<PatientEntity> patients = patientDao.findPatientsWithMoreThanXVisits(x);

        // then
        assertNotNull(patients, "Lista pacjentów nie powinna być null");
        assertFalse(patients.isEmpty(), "Lista pacjentów nie powinna być pusta");

        // Dodatkowe sprawdzenie, czy pacjenci mają więcej niż X wizyt
        for (PatientEntity patient : patients) {
            assertTrue(patient.getVisits().size() > x, "Pacjent powinien mieć więcej niż " + x + " wizyt");
        }
    }

    @Test
    public void shouldFindInactivePatientsUsingNegation() {
        // when
        List<PatientEntity> inactivePatients = patientDao.findInactivePatientsUsingNegation();

        // then
        assertNotNull(inactivePatients, "Lista pacjentów nie powinna być nullem");
        assertFalse(inactivePatients.isEmpty(), "Lista pacjentów nie powinna być pusta");

        for (PatientEntity patient : inactivePatients) {
            assertFalse(patient.isActive(), "Pacjent powinien być nieaktywny");
        }
    }

    @Test
    public void shouldThrowOptimisticLockException() {
        // Session 1: odczyt i aktualizacja
        EntityManager em1 = entityManagerFactory.createEntityManager();
        em1.getTransaction().begin();
        PatientEntity p1 = em1.find(PatientEntity.class, 101L);
        em1.getTransaction().commit();
        em1.close();

        // Session 2: równoległy odczyt
        EntityManager em2 = entityManagerFactory.createEntityManager();
        em2.getTransaction().begin();
        PatientEntity p2 = em2.find(PatientEntity.class, 101L);
        em2.getTransaction().commit();
        em2.close();

        // Zmiana i zapis p1 - inkrementacja version
        em1 = entityManagerFactory.createEntityManager();
        em1.getTransaction().begin();
        p1.setFirstName("Janek");
        p1 = em1.merge(p1);
        em1.getTransaction().commit();
        em1.close();

        // Próba zapisu p2 z przestarzałym version → wyjątek
        em2 = entityManagerFactory.createEntityManager();
        em2.getTransaction().begin();
        p2.setFirstName("Janusz");

        EntityManager finalEm = em2;
        assertThrows(OptimisticLockException.class, () -> {
            finalEm.merge(p2); // to powinno rzucić wyjątek
            finalEm.getTransaction().commit(); // wyjątek rzuci się tu
        });

        em2.close();
    }
}
