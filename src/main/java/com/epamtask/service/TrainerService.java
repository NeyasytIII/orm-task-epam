package com.epamtask.service;

import com.epamtask.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    void createTrainer(Long userId, String firstName, String lastName, String specialization);
    void updateTrainer(Trainer trainer);
    Optional<Trainer> getTrainerById(Long id);
    Optional<Trainer> getTrainerByUsername(String username);
    List<Trainer> getAllTrainers();
    void updatePassword(String username, String newPassword);
    void activateUser(String username);
    void deactivateUser(String username);
}