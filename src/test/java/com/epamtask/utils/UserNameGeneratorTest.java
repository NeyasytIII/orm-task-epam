package com.epamtask.utils;

import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class UserNameGeneratorTest {

    private final UserNameGenerator generator = new UserNameGenerator();

    @Test
    void testGenerateUserNameNoConflict() {
        Map<Long, Trainee> traineeMap = new HashMap<>();
        Map<Long, Trainer> trainerMap = new HashMap<>();
        String userName = generator.generateUserName("John", "Doe", traineeMap, trainerMap);
        assertEquals("John.Doe", userName);
    }

    @Test
    void testGenerateUserNameConflictWithTrainee() {
        Map<Long, Trainee> traineeMap = new HashMap<>();
        Map<Long, Trainer> trainerMap = new HashMap<>();
        Trainee trainee = new Trainee();
        trainee.setTraineeId(1L);
        trainee.setUserName("John.Doe");
        traineeMap.put(1L, trainee);

        String userName = generator.generateUserName("John", "Doe", traineeMap, trainerMap);
        assertNotEquals("John.Doe", userName);
        assertTrue(userName.startsWith("John.Doe"));
    }

    @Test
    void testGenerateUserNameConflictWithTrainer() {
        Map<Long, Trainee> traineeMap = new HashMap<>();
        Map<Long, Trainer> trainerMap = new HashMap<>();
        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("John.Doe");
        trainerMap.put(1L, trainer);

        String userName = generator.generateUserName("John", "Doe", traineeMap, trainerMap);
        assertNotEquals("John.Doe", userName);
        assertTrue(userName.startsWith("John.Doe"));
    }

    @Test
    void testGenerateUserNameMultipleConflicts() {
        Map<Long, Trainee> traineeMap = new HashMap<>();
        Map<Long, Trainer> trainerMap = new HashMap<>();
        Trainee t1 = new Trainee();
        t1.setTraineeId(1L);
        t1.setUserName("John.Doe");
        traineeMap.put(1L, t1);

        Trainer tr1 = new Trainer();
        tr1.setTrainerId(2L);
        tr1.setUserName("John.Doe1");
        trainerMap.put(2L, tr1);

        String userName = generator.generateUserName("John", "Doe", traineeMap, trainerMap);
        assertFalse("John.Doe".equals(userName));
        assertFalse("John.Doe1".equals(userName));
        assertTrue(userName.startsWith("John.Doe"));
    }
}