package com.epamtask.repository.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.TrainingTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Loggable
    @Override
    public List<TrainingTypeEntity> findAll() {
        return entityManager.createQuery("SELECT t FROM TrainingTypeEntity t", TrainingTypeEntity.class)
                .getResultList();
    }

    @Override
    @Loggable
    public void update(TrainingTypeEntity trainingTypeEntity) {
        entityManager.merge(trainingTypeEntity);
    }

    @Loggable
    @Override
    public Optional<TrainingTypeEntity> findByName(String name) {
        return entityManager.createQuery("SELECT t FROM TrainingTypeEntity t WHERE t.type = :name", TrainingTypeEntity.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    @Loggable
    @Override
    public Optional<TrainingTypeEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TrainingTypeEntity.class, id));
    }

    @Override
    @Loggable
    public TrainingTypeEntity save(TrainingTypeEntity trainingTypeEntity) {
        entityManager.persist(trainingTypeEntity);
        return trainingTypeEntity;
    }

    @Override
    @Loggable
    public void delete(TrainingTypeEntity trainingTypeEntity) {
        if (entityManager.contains(trainingTypeEntity)) {
            entityManager.remove(trainingTypeEntity);
        } else {
            TrainingTypeEntity merged = entityManager.merge(trainingTypeEntity);
            entityManager.remove(merged);
        }
    }

    @Override
    @Loggable
    public void deleteById(Long id) {
        TrainingTypeEntity trainingTypeEntity = entityManager.find(TrainingTypeEntity.class, id);
        if (trainingTypeEntity != null) {
            entityManager.remove(trainingTypeEntity);
        }
    }
}