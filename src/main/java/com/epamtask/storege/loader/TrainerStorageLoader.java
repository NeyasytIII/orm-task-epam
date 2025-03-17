package com.epamtask.storege.loader;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.exception.InvalidDataException;
import com.epamtask.model.Trainer;
import com.epamtask.storege.loader.filereaders.JsonFileReader;
import com.epamtask.storege.loader.validation.uservalidation.TrainerValidator;
import com.epamtask.storege.loader.validation.common.FileValidator;
import com.epamtask.storege.loader.validation.common.GenericUserNameVerifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TrainerStorageLoader {

    private final ResourceLoader resourceLoader;
    private final String trainerFilePath;
    private final JsonFileReader trainerFileReader;
    private final TrainerValidator trainerValidator;
    private final GenericUserNameVerifier<Trainer> trainerUserNameVerifier;

    public TrainerStorageLoader(ResourceLoader resourceLoader,
                                @Qualifier("trainerFilePath") String trainerFilePath,
                                JsonFileReader trainerFileReader,
                                TrainerValidator trainerValidator,
                                GenericUserNameVerifier<Trainer> trainerUserNameVerifier) {
        this.resourceLoader = resourceLoader;
        this.trainerFilePath = trainerFilePath;
        this.trainerFileReader = trainerFileReader;
        this.trainerValidator = trainerValidator;
        this.trainerUserNameVerifier = trainerUserNameVerifier;
    }

    @Loggable
    public void loadTrainers(Map<Long, Trainer> trainerStorage) {
        Resource resource = resourceLoader.getResource("classpath:" + trainerFilePath);

        if (!FileValidator.isFileValid(resource, trainerFilePath)) {
            throw new InvalidDataException("Invalid trainer file: " + trainerFilePath);
        }

        List<Trainer> trainers = trainerFileReader.readFromFile(resource, Trainer.class);
        List<String> validationErrors = trainerValidator.validate(trainers);

        if (!validationErrors.isEmpty()) {
            throw new InvalidDataException("Errors validating trainers:\n" + String.join("\n", validationErrors));
        }

        List<String> usernameErrors = trainerUserNameVerifier.verifyUserNames(trainers, trainerStorage);

        if (!usernameErrors.isEmpty()) {
            throw new InvalidDataException("Errors verifying trainer usernames:\n" + String.join("\n", usernameErrors));
        }

        trainers.forEach(trainer -> trainerStorage.put(trainer.getTrainerId(), trainer));
    }
}