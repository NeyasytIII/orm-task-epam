package com.epamtask.repository.impl;
import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Training;
import com.epamtask.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Loggable
    public Training save(Training training) {
        entityManager.persist(training);
        return training;
    }

    @Override
    @Loggable
    public void update(Training training) {
        entityManager.merge(training);
    }
    @Loggable
    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Training.class, id));
    }
    @Loggable
    @Override
    public List<Training> findAll() {
        return entityManager.createQuery("SELECT t FROM Training t", Training.class).getResultList();
    }

    @Override
    @Loggable
    public void deleteById(Long id) {
        Training training = entityManager.find(Training.class, id);
        if (training != null) {
            entityManager.remove(training);
        }
    }

    @Override
    @Loggable
    public void delete(Training training) {
        if (entityManager.contains(training)) {
            entityManager.remove(training);
        } else {
            Training merged = entityManager.merge(training);
            entityManager.remove(merged);
        }
    }
    @Loggable
    @Override
    public List<Training> findByTraineeUsernameAndCriteria(
            String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {

        String query = "SELECT t FROM Training t " +
                "JOIN t.trainee tr " +
                "JOIN t.trainer tn " +
                "JOIN t.trainingType tt " +
                "WHERE tr.userName = :traineeUsername ";

        if (fromDate != null) query += "AND t.trainingDate >= :fromDate ";
        if (toDate != null) query += "AND t.trainingDate <= :toDate ";
        if (trainerName != null) query += "AND tn.userName = :trainerName ";
        if (trainingType != null) query += "AND tt.type = :trainingType ";

        var typedQuery = entityManager.createQuery(query, Training.class)
                .setParameter("traineeUsername", traineeUsername);

        if (fromDate != null) typedQuery.setParameter("fromDate", fromDate);
        if (toDate != null) typedQuery.setParameter("toDate", toDate);
        if (trainerName != null) typedQuery.setParameter("trainerName", trainerName);
        if (trainingType != null) typedQuery.setParameter("trainingType", trainingType);

        return typedQuery.getResultList();
    }
    @Loggable
    @Override
    public List<Training> findByTrainerUsernameAndCriteria(
            String trainerUsername, Date fromDate, Date toDate, String traineeName) {

        String query = "SELECT t FROM Training t " +
                "JOIN t.trainer tn " +
                "JOIN t.trainee tr " +
                "WHERE tn.userName = :trainerUsername ";

        if (fromDate != null) query += "AND t.trainingDate >= :fromDate ";
        if (toDate != null) query += "AND t.trainingDate <= :toDate ";
        if (traineeName != null) query += "AND tr.userName = :traineeName ";

        var typedQuery = entityManager.createQuery(query, Training.class)
                .setParameter("trainerUsername", trainerUsername);

        if (fromDate != null) typedQuery.setParameter("fromDate", fromDate);
        if (toDate != null) typedQuery.setParameter("toDate", toDate);
        if (traineeName != null) typedQuery.setParameter("traineeName", traineeName);

        return typedQuery.getResultList();
    }
}