package com.epamtask.facade.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.facade.TrainingTypeFacade;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeFacadeImpl implements TrainingTypeFacade {
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingTypeFacadeImpl(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @Loggable
    @Override
    public List<TrainingTypeEntity> getAllTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }

    @Loggable
    @Override
    public Optional<TrainingTypeEntity> getTrainingTypeById(Long id) {
        return trainingTypeService.getTrainingTypeById(id);
    }

    @Loggable
    @Override
    public Optional<TrainingTypeEntity> getTrainingTypeByName(String name) {
        return trainingTypeService.getTrainingTypeByName(name);
    }

    @Loggable
    @Override
    public TrainingTypeEntity createTrainingType(String name) {
        return trainingTypeService.createTrainingType(name);
    }

    @Loggable
    @Override
    public void deleteTrainingTypeById(Long id) {
        trainingTypeService.deleteTrainingTypeById(id);
    }

    @Loggable
    @Override
    public void deleteTrainingType(TrainingTypeEntity trainingTypeEntity) {
        trainingTypeService.deleteTrainingType(trainingTypeEntity);
    }
}