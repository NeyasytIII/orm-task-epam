package com.epamtask.storege.loader;

import com.epamtask.aspect.annotation.Loggable;
import com.epamtask.exception.InvalidDataException;
import com.epamtask.model.Trainee;
import com.epamtask.storege.loader.filereaders.JsonFileReader;
import com.epamtask.storege.loader.validation.common.FileValidator;
import com.epamtask.storege.loader.validation.uservalidation.TraineeValidator;
import com.epamtask.storege.loader.validation.common.GenericUserNameVerifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TraineeStorageLoader {

    private final ResourceLoader resourceLoader;
    private final String traineeFilePath;
    private final FileValidator fileValidator;
    private final JsonFileReader traineeFileReader;
    private final TraineeValidator traineeValidator;
    private final GenericUserNameVerifier<Trainee> traineeUserNameVerifier;

    public TraineeStorageLoader(ResourceLoader resourceLoader,
                                @Qualifier("traineeFilePath") String traineeFilePath,
                                FileValidator fileValidator,
                                JsonFileReader traineeFileReader,
                                TraineeValidator traineeValidator,
                                GenericUserNameVerifier<Trainee> traineeUserNameVerifier) {
        this.resourceLoader = resourceLoader;
        this.traineeFilePath = traineeFilePath;
        this.fileValidator = fileValidator;
        this.traineeFileReader = traineeFileReader;
        this.traineeValidator = traineeValidator;
        this.traineeUserNameVerifier = traineeUserNameVerifier;
    }

    @Loggable
    public void loadTrainees(Map<Long, Trainee> traineeStorage) {
        Resource resource = resourceLoader.getResource("classpath:" + traineeFilePath);

        if (!fileValidator.isFileValid(resource, traineeFilePath)) {
            throw new InvalidDataException("Invalid trainee file: " + traineeFilePath);
        }

        List<Trainee> trainees = traineeFileReader.readFromFile(resource, Trainee.class);

        List<String> validationErrors = traineeValidator.validate(trainees);
        if (!validationErrors.isEmpty()) {
            throw new InvalidDataException("Validation errors: " + String.join("; ", validationErrors));
        }

        List<String> userNameErrors = traineeUserNameVerifier.verifyUserNames(trainees, traineeStorage);
        if (!userNameErrors.isEmpty()) {
            throw new InvalidDataException("UserName verification errors: " + String.join("; ", userNameErrors));
        }

        trainees.forEach(trainee -> traineeStorage.put(trainee.getTraineeId(), trainee));
    }
}