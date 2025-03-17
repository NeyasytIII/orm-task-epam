package com.epamtask.service.impl;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.service.TraineeService;
import com.epamtask.storege.datamodes.TraineeStorage;
import com.epamtask.storege.datamodes.TrainerStorage;
import com.epamtask.utils.PasswordGenerator;
import com.epamtask.utils.UserNameGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TraineeServiceImpl implements TraineeService {

    private final TraineeStorage traineeStorage;
    private final TrainerStorage trainerStorage;
    private final UserNameGenerator userNameGenerator;

    public TraineeServiceImpl(
            @Value("${data.source}") String dataSource,
            @Qualifier("databaseTraineeStorage") TraineeStorage databaseStorage,
            @Qualifier("fileTraineeStorage") TraineeStorage fileStorage,
            @Qualifier("databaseTrainerStorage") TrainerStorage trainerStorage,
            UserNameGenerator userNameGenerator
    ) {
        this.traineeStorage = "DATABASE".equalsIgnoreCase(dataSource) ? databaseStorage : fileStorage;
        this.trainerStorage = trainerStorage;
        this.userNameGenerator = userNameGenerator;
    }

    @Loggable
    @Override
    public void createTrainee(Long userId, String firstName, String lastName, String address, Date birthdayDate) {
        try {
            if (userId == null || userId <= 0 || firstName.isBlank() || lastName.isBlank()) {
                throw new IllegalArgumentException("Invalid input data");
            }
            if (traineeStorage.findById(userId).isPresent()) {
                throw new IllegalArgumentException("Trainee with ID " + userId + " already exists");
            }
            Map<Long, Trainee> traineeMap = traineeStorage.findAll().stream()
                    .collect(Collectors.toMap(Trainee::getTraineeId, trainee -> trainee));
            String uniqueUsername = userNameGenerator.generateUserName(firstName, lastName, traineeMap, Map.of());
            String password = PasswordGenerator.generatePassword();
            Trainee trainee = new Trainee(userId, firstName, lastName, address, birthdayDate, true);
            trainee.setUserName(uniqueUsername);
            trainee.setPassword(password);
            traineeStorage.save(trainee);
        } catch (Exception e) {
            System.err.println("Error creating trainee: " + e.getMessage());
        }
    }

    @Override
    @Loggable
    @Authenticated
    public void updateTrainee(Trainee trainee) {
        try {
            if (trainee == null || trainee.getTraineeId() == null) {
                throw new IllegalArgumentException("Trainee and ID cannot be null");
            }
            traineeStorage.save(trainee);
        } catch (Exception e) {
            System.err.println("Error updating trainee: " + e.getMessage());
        }
    }

    @Override
    @Loggable
    @Authenticated
    public void deleteTrainee(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("ID cannot be null");
            }
            traineeStorage.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error deleting trainee: " + e.getMessage());
        }
    }

    @Override
    @Loggable
    @Authenticated
    public Optional<Trainee> getTraineeById(Long id) {
        try {
            return traineeStorage.findById(id);
        } catch (Exception e) {
            System.err.println("Error getting trainee by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Loggable
    @Authenticated
    public Optional<Trainee> getTraineeByUsername(String username) {
        try {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            return traineeStorage.findByUsername(username);
        } catch (Exception e) {
            System.err.println("Error getting trainee by username: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Loggable
    @Authenticated
    public List<Trainee> getAllTrainees() {
        try {
            List<Trainee> result = traineeStorage.findAll();
            if (result == null || result.isEmpty()) {
                throw new IllegalStateException("No trainees found");
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error getting all trainees: " + e.getMessage());
            return List.of();
        }
    }

    @Loggable
    @Authenticated
    @Override
    public void assignTrainersToTrainee(String traineeUsername, List<String> trainerUsernames) {
        try {
            if (trainerUsernames == null || trainerUsernames.isEmpty()) {
                return;
            }
            Trainee trainee = traineeStorage.findByUsername(traineeUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Trainee not found: " + traineeUsername));
            List<Trainer> trainers = trainerUsernames.stream()
                    .map(trainerStorage::findByUsername)
                    .flatMap(Optional::stream)
                    .filter(trainer -> !trainee.getTrainers().contains(trainer))
                    .toList();
            if (trainers.isEmpty()) {
                return;
            }
            trainee.getTrainers().addAll(trainers);
            traineeStorage.save(trainee);
        } catch (Exception e) {
            System.err.println("Error assigning trainers: " + e.getMessage());
        }
    }

    @Loggable
    @Authenticated
    @Override
    public void deleteTraineeByUsername(String username) {
        try {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            traineeStorage.deleteByUsername(username);
        } catch (Exception e) {
            System.err.println("Error deleting trainee by username: " + e.getMessage());
        }
    }

    @Loggable
    @Authenticated
    @Override
    public void updatePassword(String username, String newPassword) {
        try {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            if (newPassword == null || newPassword.isBlank()) {
                throw new IllegalArgumentException("New password cannot be null or empty");
            }
            traineeStorage.findByUsername(username)
                    .ifPresentOrElse(
                            trainee -> {
                                trainee.setPassword(newPassword);
                                traineeStorage.save(trainee);
                            },
                            () -> {
                                throw new IllegalArgumentException("Trainee not found: " + username);
                            }
                    );
        } catch (Exception e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
    }

    @Loggable
    @Authenticated
    @Override
    public void activateUser(String username) {
        try {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            traineeStorage.findByUsername(username)
                    .ifPresentOrElse(
                            trainee -> {
                                trainee.setActive(true);
                                traineeStorage.save(trainee);
                            },
                            () -> {
                                throw new IllegalArgumentException("Trainee not found: " + username);
                            }
                    );
        } catch (Exception e) {
            System.err.println("Error activating user: " + e.getMessage());
        }
    }

    @Loggable
    @Authenticated
    @Override
    public void deactivateUser(String username) {
        try {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            traineeStorage.findByUsername(username)
                    .ifPresentOrElse(
                            trainee -> {
                                trainee.setActive(false);
                                traineeStorage.save(trainee);
                            },
                            () -> {
                                throw new IllegalArgumentException("Trainee not found: " + username);
                            }
                    );
        } catch (Exception e) {
            System.err.println("Error deactivating user: " + e.getMessage());
        }
    }
}
