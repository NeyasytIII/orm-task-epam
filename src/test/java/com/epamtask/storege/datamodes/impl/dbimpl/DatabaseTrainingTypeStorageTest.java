package com.epamtask.storege.datamodes.impl.dbimpl;

import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseTrainingTypeStorageTest {

    private TrainingTypeRepository trainingTypeRepository;
    private DatabaseTrainingTypeStorage storage;

    @BeforeEach
    void setUp() {
        trainingTypeRepository = mock(TrainingTypeRepository.class);
        storage = new DatabaseTrainingTypeStorage(trainingTypeRepository);
    }

    @Test
    void testFindAll() {
        when(trainingTypeRepository.findAll()).thenReturn(List.of(new TrainingTypeEntity()));
        List<TrainingTypeEntity> result = storage.findAll();
        assertEquals(1, result.size());
        verify(trainingTypeRepository).findAll();
    }

    @Test
    void testFindById() {
        TrainingTypeEntity type = new TrainingTypeEntity();
        when(trainingTypeRepository.findById(1L)).thenReturn(Optional.of(type));
        Optional<TrainingTypeEntity> result = storage.findById(1L);
        assertTrue(result.isPresent());
        verify(trainingTypeRepository).findById(1L);
    }

    @Test
    void testFindByName() {
        TrainingTypeEntity type = new TrainingTypeEntity();
        when(trainingTypeRepository.findByName("YOGA")).thenReturn(Optional.of(type));
        Optional<TrainingTypeEntity> result = storage.findByName("YOGA");
        assertTrue(result.isPresent());
        verify(trainingTypeRepository).findByName("YOGA");
    }

    @Test
    void testSave() {
        TrainingTypeEntity type = new TrainingTypeEntity();
        storage.save(type);
        verify(trainingTypeRepository).save(type);
    }

    @Test
    void testDeleteById() {
        storage.deleteById(2L);
        verify(trainingTypeRepository).deleteById(2L);
    }

    @Test
    void testDelete() {
        TrainingTypeEntity type = new TrainingTypeEntity();
        storage.delete(type);
        verify(trainingTypeRepository).delete(type);
    }
}