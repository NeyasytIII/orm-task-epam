package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.TrainingTypeRepository;
import com.epamtask.storege.datamodes.TrainingTypeStorage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("databaseTrainingTypeStorage")
public class DatabaseTrainingTypeStorage implements TrainingTypeStorage {

    private final TrainingTypeRepository trainingTypeRepository;

    public DatabaseTrainingTypeStorage(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingTypeEntity> findAll() {
        return trainingTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingTypeEntity> findById(Long id) {
        return trainingTypeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingTypeEntity> findByName(String name) {
        return trainingTypeRepository.findByName(name);
    }

    @Override
    @Transactional
    public void save(TrainingTypeEntity trainingTypeEntity) {
        trainingTypeRepository.save(trainingTypeEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        trainingTypeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(TrainingTypeEntity trainingTypeEntity) {
        trainingTypeRepository.delete(trainingTypeEntity);
    }
}