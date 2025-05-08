package com.jpacourse.service.impl;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.mapper.PatientMapper;
import com.jpacourse.mapper.VisitMapper;
import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    @Autowired
    private EntityManager entityManager;

    private final PatientDao patientDao;

    @Autowired
    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public PatientTO findById(Long id) {
        PatientEntity entity = patientDao.findOne(id);

        // Map base patient data
        PatientTO to = PatientMapper.mapToTO(entity);

        // Filter and map only past visits
        List<VisitTO> pastVisits = entity.getVisits().stream()
                .filter(visit -> visit.getTime().isBefore(LocalDateTime.now()))
                .map(VisitMapper::mapToTO)
                .toList();

        to.setPastVisits(pastVisits);
        return to;
    }

    @Override
    public void deleteById(Long id) {
        patientDao.delete(id);
    }

    @Override
    public PatientEntity findByIdWithVisits(Long patientId) {
        String jpql = "SELECT p FROM PatientEntity p LEFT JOIN FETCH p.visits WHERE p.id = :patientId";
        TypedQuery<PatientEntity> query = entityManager.createQuery(jpql, PatientEntity.class);
        query.setParameter("patientId", patientId);
        return query.getSingleResult();
    }
}
