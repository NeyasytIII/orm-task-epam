package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.model.Training;
import com.epamtask.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseTrainingStorageTest {

    private TrainingRepository trainingRepository;
    private DatabaseTrainingStorage storage;

    @BeforeEach
    void setUp() {
        trainingRepository = mock(TrainingRepository.class);
        storage = new DatabaseTrainingStorage(trainingRepository);
    }

    @Test
    void testFindById() {
        Training training = new Training();
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));
        Optional<Training> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(trainingRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        when(trainingRepository.findAll()).thenReturn(List.of(new Training()));
        List<Training> result = storage.findAll();
        assertEquals(1, result.size());
        verify(trainingRepository).findAll();
    }

    @Test
    void testFindByTrainerId() {
        Training training = new Training();
        training.setTrainingId(10L);
        training.setTrainerId(1L);
        when(trainingRepository.findAll()).thenReturn(List.of(training));
        Map<Long, Training> result = storage.findByTrainerId(1L);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(10L));
    }

    @Test
    void testFindByTraineeId() {
        Training training = new Training();
        training.setTrainingId(20L);
        training.setTraineeId(2L);
        when(trainingRepository.findAll()).thenReturn(List.of(training));
        Map<Long, Training> result = storage.findByTraineeId(2L);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(20L));
    }

    @Test
    void testFindByTraineeUsernameAndCriteria() {
        List<Training> trainings = List.of(new Training());
        when(trainingRepository.findByTraineeUsernameAndCriteria("trainee", null, null, null, null)).thenReturn(trainings);
        List<Training> result = storage.findByTraineeUsernameAndCriteria("trainee", null, null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByTrainerUsernameAndCriteria() {
        List<Training> trainings = List.of(new Training());
        when(trainingRepository.findByTrainerUsernameAndCriteria("trainer", null, null, null)).thenReturn(trainings);
        List<Training> result = storage.findByTrainerUsernameAndCriteria("trainer", null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    void testSave() {
        Training training = new Training();
        storage.save(training);
        verify(trainingRepository).save(training);
    }

    @Test
    void testDeleteById() {
        storage.deleteById(5L);
        verify(trainingRepository).deleteById(5L);
    }
}