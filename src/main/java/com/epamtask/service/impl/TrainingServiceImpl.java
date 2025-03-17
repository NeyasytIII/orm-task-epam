package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import com.epamtask.service.TrainingService;
import com.epamtask.storege.datamodes.TrainingStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingStorage trainingStorage;

    public TrainingServiceImpl(
            @Value("${data.source}") String dataSource,
            @Qualifier("databaseTrainingStorage") TrainingStorage databaseStorage,
            @Qualifier("fileTrainingStorage") TrainingStorage fileStorage
    ) {
        this.trainingStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseStorage : fileStorage;
    }

    @Override
    @Loggable
    public void createTraining(Long trainingId, Long trainerId, Long traineeId, String trainingName,
                               TrainingType type, Date trainingDate, String trainingDuration) {
        if (trainingId == null || trainingId <= 0) {
            throw new IllegalArgumentException("Training ID must be positive");
        }
        if (trainerId == null || trainerId <= 0) {
            throw new IllegalArgumentException("Trainer ID must be positive");
        }
        if (traineeId == null || traineeId <= 0) {
            throw new IllegalArgumentException("Trainee ID must be positive");
        }
        if (trainingName == null || trainingName.isBlank()) {
            throw new IllegalArgumentException("Training name is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("Training type is required");
        }
        if (trainingDate == null) {
            throw new IllegalArgumentException("Training date is required");
        }
        if (trainingDuration == null || trainingDuration.isBlank()) {
            throw new IllegalArgumentException("Training duration is required");
        }
        if (trainingStorage.findById(trainingId).isPresent()) {
            throw new IllegalArgumentException("Training with ID " + trainingId + " already exists");
        }

        Training training = new Training(trainingId, traineeId, trainerId, trainingName, type, trainingDate, trainingDuration);
        trainingStorage.save(training);
    }

    @Override
    @Loggable
    public Optional<Training> getTrainingById(Long trainingId) {
        if (trainingId == null) {
            throw new IllegalArgumentException("Training ID cannot be null");
        }
        return trainingStorage.findById(trainingId);
    }

    @Override
    @Loggable
    public Map<Long, Training> getTrainingsByTrainerId(Long trainerId) {
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null");
        }
        return trainingStorage.findByTrainerId(trainerId);
    }

    @Override
    @Loggable
    public Map<Long, Training> getTrainingsByTraineeId(Long traineeId) {
        if (traineeId == null) {
            throw new IllegalArgumentException("Trainee ID cannot be null");
        }
        return trainingStorage.findByTraineeId(traineeId);
    }

    @Override
    @Loggable
    public List<Training> getAllTrainings() {
        List<Training> result = trainingStorage.findAll();
        if (result == null || result.isEmpty()) {
            throw new IllegalStateException("No trainings found");
        }
        return result;
    }

    @Override
    @Loggable
    public List<Training> getTrainingsByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType) {
        if (traineeUsername == null || traineeUsername.isBlank()) {
            throw new IllegalArgumentException("Trainee username is required");
        }
        return trainingStorage.findByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    @Override
    @Loggable
    public List<Training> getTrainingsByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        if (trainerUsername == null || trainerUsername.isBlank()) {
            throw new IllegalArgumentException("Trainer username is required");
        }
        return trainingStorage.findByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }
}