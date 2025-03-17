package com.epamtask.utils;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserNameGenerator {

    @Loggable
    public String generateUserName(String firstName, String lastName, Map<Long, Trainee> traineeStorage, Map<Long, Trainer> trainerStorage) {
        String baseUserName = firstName + "." + lastName;
        String userName = baseUserName;
        AtomicInteger counter = new AtomicInteger(1);

        while (isUserNameTaken(userName, traineeStorage, trainerStorage)) {
            userName = baseUserName + counter.getAndIncrement();
        }

        return userName;
    }

    @Loggable
    private  boolean isUserNameTaken(String userName, Map<Long, Trainee> traineeStorage, Map<Long, Trainer> trainerStorage) {
        return traineeStorage.values().stream().anyMatch(t -> t.getUserName().equals(userName))
                || trainerStorage.values().stream().anyMatch(t -> t.getUserName().equals(userName));
    }
}