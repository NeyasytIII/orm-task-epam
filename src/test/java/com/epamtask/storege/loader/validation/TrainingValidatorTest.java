package com.epamtask.storege.loader.validation;

import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.storege.loader.validation.common.DateValidator;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TrainingValidatorTest {
    private final DateValidator dateValidator = new DateValidator();
    private final TrainingValidator validator = new TrainingValidator(dateValidator);

    @Test
    void validTraining() {
        Trainee trainee = new Trainee(1L, "Alice", "Smith", "Addr", new Date(System.currentTimeMillis() - 1000),true);
        Trainer trainer = new Trainer(2L, "John", "Doe", "Fitness",true);
        Date trainingDate = new Date(System.currentTimeMillis() - 2000);
        Training training = new Training(100L, 1L, 2L, "Yoga", TrainingType.STRENGTH, trainingDate, "60 minutes");
        Map<Long, Trainee> traineeStorage = new HashMap<>();
        traineeStorage.put(1L, trainee);
        Map<Long, Trainer> trainerStorage = new HashMap<>();
        trainerStorage.put(2L, trainer);
        List<String> errors = validator.validate(List.of(training), traineeStorage, trainerStorage);
        assertTrue(errors.isEmpty());
    }

    @Test
    void invalidTraining() {
        Training training = new Training(0L, 99L, 88L, "", null, new Date(System.currentTimeMillis() + 100000), "");
        Map<Long, Trainee> traineeStorage = new HashMap<>();
        Map<Long, Trainer> trainerStorage = new HashMap<>();
        List<String> errors = validator.validate(List.of(training), traineeStorage, trainerStorage);
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(msg -> msg.contains("Invalid training ID")));
    }
}