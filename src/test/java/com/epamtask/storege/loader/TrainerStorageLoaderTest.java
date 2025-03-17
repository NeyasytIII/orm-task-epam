package com.epamtask.storege.loader;

import com.epamtask.exception.InvalidDataException;
import com.epamtask.model.Trainer;
import com.epamtask.storege.loader.filereaders.JsonFileReader;
import com.epamtask.storege.loader.validation.uservalidation.TrainerValidator;
import com.epamtask.storege.loader.validation.common.FileValidator;
import com.epamtask.storege.loader.validation.common.GenericUserNameVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class TrainerStorageLoaderTest {

    private ResourceLoader resourceLoader;
    private JsonFileReader jsonFileReader;
    private TrainerValidator trainerValidator;
    private GenericUserNameVerifier<Trainer> trainerUserNameVerifier;
    private Resource resource;
    private TrainerStorageLoader trainerStorageLoader;
    private Map<Long, Trainer> trainerStorage;

    private final String validTrainerFilePath = "trainer-json-data/valid-data/valid-trainer.json";
    private final String duplicateTrainerFilePath = "trainer-json-data/invalid-data/duplicate-trainer.json";
    private final String invalidNameTrainerFilePath = "trainer-json-data/invalid-data/invalid-name-trainer.json";
    private final String negativeIdTrainerFilePath = "trainer-json-data/invalid-data/negative-id-trainer.json";

    @BeforeEach
    void setUp() throws Exception {
        resourceLoader = org.mockito.Mockito.mock(ResourceLoader.class);
        jsonFileReader = org.mockito.Mockito.mock(JsonFileReader.class);
        trainerValidator = org.mockito.Mockito.mock(TrainerValidator.class);
        trainerUserNameVerifier = org.mockito.Mockito.mock(GenericUserNameVerifier.class);
        resource = org.mockito.Mockito.mock(Resource.class);
        trainerStorage = new HashMap<>();
        trainerStorageLoader = new TrainerStorageLoader(resourceLoader, validTrainerFilePath, jsonFileReader, trainerValidator, trainerUserNameVerifier);

        lenient().when(resourceLoader.getResource("classpath:" + validTrainerFilePath)).thenReturn(resource);
        lenient().when(resourceLoader.getResource("classpath:" + duplicateTrainerFilePath)).thenReturn(resource);
        lenient().when(resourceLoader.getResource("classpath:" + invalidNameTrainerFilePath)).thenReturn(resource);
        lenient().when(resourceLoader.getResource("classpath:" + negativeIdTrainerFilePath)).thenReturn(resource);
        lenient().when(resource.exists()).thenReturn(true);
        lenient().when(resource.getInputStream()).thenReturn(new ByteArrayInputStream("sample data".getBytes()));
    }

    @Test
    void testLoadValidTrainers() {
        Trainer trainer1 = new Trainer(1L, "Alice", "Brown", "Strength Training",true);
        Trainer trainer2 = new Trainer(2L, "Bob", "Smith", "Yoga",true);
        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, validTrainerFilePath)).thenReturn(true);
            when(jsonFileReader.readFromFile(resource, Trainer.class)).thenReturn(List.of(trainer1, trainer2));
            when(trainerValidator.validate(anyList())).thenReturn(Collections.emptyList());
            when(trainerUserNameVerifier.verifyUserNames(anyList(), anyMap())).thenReturn(Collections.emptyList());
            trainerStorageLoader.loadTrainers(trainerStorage);
            assertEquals(2, trainerStorage.size());
            assertEquals(trainer1, trainerStorage.get(1L));
            assertEquals(trainer2, trainerStorage.get(2L));
        }
    }

    @Test
    void testLoadDuplicateTrainers() {
        trainerStorageLoader = new TrainerStorageLoader(resourceLoader, duplicateTrainerFilePath, jsonFileReader, trainerValidator, trainerUserNameVerifier);
        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, duplicateTrainerFilePath)).thenReturn(true);
            when(jsonFileReader.readFromFile(resource, Trainer.class)).thenReturn(Collections.emptyList());
            when(trainerValidator.validate(anyList())).thenReturn(List.of("Duplicate trainer ID"));
            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
                    trainerStorageLoader.loadTrainers(trainerStorage)
            );
            assertTrue(exception.getMessage().contains("Errors validating trainers"));
            assertTrue(trainerStorage.isEmpty());
        }
    }

    @Test
    void testLoadInvalidNameTrainers() {
        trainerStorageLoader = new TrainerStorageLoader(resourceLoader, invalidNameTrainerFilePath, jsonFileReader, trainerValidator, trainerUserNameVerifier);
        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, invalidNameTrainerFilePath)).thenReturn(true);
            when(jsonFileReader.readFromFile(resource, Trainer.class)).thenReturn(Collections.emptyList());
            when(trainerValidator.validate(anyList())).thenReturn(List.of("Invalid trainer name"));
            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
                    trainerStorageLoader.loadTrainers(trainerStorage)
            );
            assertTrue(exception.getMessage().contains("Errors validating trainers"));
            assertTrue(trainerStorage.isEmpty());
        }
    }

    @Test
    void testLoadNegativeIdTrainers() {
        trainerStorageLoader = new TrainerStorageLoader(resourceLoader, negativeIdTrainerFilePath, jsonFileReader, trainerValidator, trainerUserNameVerifier);
        try (MockedStatic<FileValidator> fileValidatorMock = mockStatic(FileValidator.class)) {
            fileValidatorMock.when(() -> FileValidator.isFileValid(resource, negativeIdTrainerFilePath)).thenReturn(true);
            when(jsonFileReader.readFromFile(resource, Trainer.class)).thenReturn(Collections.emptyList());
            when(trainerValidator.validate(anyList())).thenReturn(List.of("Negative trainer ID"));
            InvalidDataException exception = assertThrows(InvalidDataException.class, () ->
                    trainerStorageLoader.loadTrainers(trainerStorage)
            );
            assertTrue(exception.getMessage().contains("Errors validating trainers"));
            assertTrue(trainerStorage.isEmpty());
        }
    }
}