package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.service.TrainerService;
import com.epamtask.storege.datamodes.TrainerStorage;
import com.epamtask.utils.PasswordGenerator;
import com.epamtask.utils.UserNameGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerStorage trainerStorage;
    private final UserNameGenerator userNameGenerator;

    public TrainerServiceImpl(
            @Value("${data.source}") String dataSource,
            @Qualifier("databaseTrainerStorage") TrainerStorage databaseStorage,
            @Qualifier("fileTrainerStorage") TrainerStorage fileStorage,
            UserNameGenerator userNameGenerator
    ) {
        this.trainerStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseStorage : fileStorage;
        this.userNameGenerator = userNameGenerator;
    }
    @Loggable
    @Override
    public void createTrainer(Long userId, String firstName, String lastName, String specialization) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("First name and last name are required");
        }
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization is required");
        }
        if (trainerStorage.findById(userId).isPresent()) {
            throw new IllegalArgumentException("Trainer with ID " + userId + " already exists");
        }

        Map<Long, Trainee> emptyTraineeMap = Map.of();
        Map<Long, Trainer> trainerMap = trainerStorage.findAll().stream()
                .collect(Collectors.toMap(
                        Trainer::getTrainerId,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        java.util.LinkedHashMap::new
                ));
        String uniqueUsername = userNameGenerator.generateUserName(firstName, lastName, emptyTraineeMap, trainerMap);

        Trainer trainer = new Trainer(userId, firstName, lastName, specialization, false);
        trainer.setUserName(uniqueUsername);
        trainer.setPassword(PasswordGenerator.generatePassword());
        trainer.setActive(true);

        trainerStorage.save(trainer);
    }

    @Loggable
    @Authenticated
    @Override
    public void updateTrainer(Trainer trainer) {
        if (trainer == null || trainer.getTrainerId() == null) {
            throw new IllegalArgumentException("Trainer and ID cannot be null");
        }
        trainerStorage.save(trainer);
    }

    @Loggable
    @Authenticated
    @Override
    public Optional<Trainer> getTrainerById(Long id) {
        return trainerStorage.findById(id);
    }

    @Loggable
    @Authenticated
    @Override
    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerStorage.findByUsername(username);
    }

    @Loggable
    @Authenticated
    @Override
    public List<Trainer> getAllTrainers() {
        return trainerStorage.findAll();
    }

    @Loggable
    @Authenticated
    public void deleteTrainer(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        trainerStorage.deleteById(id);
    }
    @Loggable
    @Authenticated
    @Override
    public void updatePassword(String username, String newPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }

        trainerStorage.findByUsername(username)
                .ifPresent(trainer -> {
                    trainer.setPassword(newPassword);
                    trainerStorage.save(trainer);
                });
    }

    @Loggable
    @Authenticated
    @Override
    public void activateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        trainerStorage.findByUsername(username)
                .ifPresent(trainer -> {
                    trainer.setActive(true);
                    trainerStorage.save(trainer);
                });
    }

    @Loggable
    @Authenticated
    @Override
    public void deactivateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        trainerStorage.findByUsername(username)
                .ifPresent(trainer -> {
                    trainer.setActive(false);
                    trainerStorage.save(trainer);
                });
    }



}