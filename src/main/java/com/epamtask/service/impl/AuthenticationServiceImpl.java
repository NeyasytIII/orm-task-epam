package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.security.AuthContextHolder;
import com.epamtask.service.AuthenticationService;
import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TraineeStorage traineeStorage;
    private final TrainerStorage trainerStorage;

    public AuthenticationServiceImpl(
            @Qualifier("databaseTraineeStorage") TraineeStorage traineeStorage,
            @Qualifier("databaseTrainerStorage") TrainerStorage trainerStorage) {
        this.traineeStorage = traineeStorage;
        this.trainerStorage = trainerStorage;
    }

    @Loggable
    @Override
    public boolean authenticate(String username, String password) {
        boolean isTraineeValid = traineeStorage.findByUsername(username)
                .map(trainee -> trainee.getPassword().equals(password) && trainee.isActive())
                .orElse(false);

        boolean isTrainerValid = trainerStorage.findByUsername(username)
                .map(trainer -> trainer.getPassword().equals(password) && trainer.isActive())
                .orElse(false);

        if (isTraineeValid || isTrainerValid) {
            AuthContextHolder.setCredentials(username, password);
            return true;
        }

        return false;
    }
}