package com.epamtask.service.impl;
import com.epamtask.model.TrainingType;
import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.storege.datamodes.TrainingTypeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingTypeServiceImplTest {

    private TrainingTypeStorage trainingTypeStorage;
    private TrainingTypeServiceImpl trainingTypeService;

    @BeforeEach
    void setUp() {
        trainingTypeStorage = mock(TrainingTypeStorage.class);
        trainingTypeService = new TrainingTypeServiceImpl("DATABASE", trainingTypeStorage);
    }

    @Test
    void testGetAllTrainingTypes_Success() {
        List<TrainingTypeEntity> types = List.of(new TrainingTypeEntity(TrainingType.YOGA));
        when(trainingTypeStorage.findAll()).thenReturn(types);

        List<TrainingTypeEntity> result = trainingTypeService.getAllTrainingTypes();

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllTrainingTypes_Empty_ShouldThrowException() {
        when(trainingTypeStorage.findAll()).thenReturn(Collections.emptyList());

        assertThrows(IllegalStateException.class, () -> trainingTypeService.getAllTrainingTypes());
    }

    @Test
    void testGetTrainingTypeById_Success() {
        TrainingTypeEntity entity = new TrainingTypeEntity(TrainingType.CARDIO);
        when(trainingTypeStorage.findById(1L)).thenReturn(Optional.of(entity));

        Optional<TrainingTypeEntity> result = trainingTypeService.getTrainingTypeById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetTrainingTypeById_InvalidId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.getTrainingTypeById(null));
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.getTrainingTypeById(0L));
    }

    @Test
    void testGetTrainingTypeByName_Success() {
        TrainingTypeEntity entity = new TrainingTypeEntity(TrainingType.CROSSFIT);
        when(trainingTypeStorage.findByName("Crossfit")).thenReturn(Optional.of(entity));

        Optional<TrainingTypeEntity> result = trainingTypeService.getTrainingTypeByName("Crossfit");

        assertTrue(result.isPresent());
    }

    @Test
    void testGetTrainingTypeByName_InvalidName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.getTrainingTypeByName(null));
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.getTrainingTypeByName(" "));
    }

    @Test
    void testCreateTrainingType_Success() {
        TrainingTypeEntity result = trainingTypeService.createTrainingType("strength");

        assertEquals(TrainingType.STRENGTH, result.getType());
        verify(trainingTypeStorage).save(result);
    }

    @Test
    void testCreateTrainingType_InvalidName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.createTrainingType("invalidType"));
    }

    @Test
    void testCreateTrainingType_EmptyOrNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.createTrainingType(null));
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.createTrainingType(" "));
    }

    @Test
    void testDeleteTrainingTypeById_Success() {
        trainingTypeService.deleteTrainingTypeById(5L);
        verify(trainingTypeStorage).deleteById(5L);
    }

    @Test
    void testDeleteTrainingTypeById_InvalidId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.deleteTrainingTypeById(null));
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.deleteTrainingTypeById(0L));
    }

    @Test
    void testDeleteTrainingType_Success() {
        TrainingTypeEntity entity = new TrainingTypeEntity(TrainingType.YOGA);
        entity.setId(3L);
        trainingTypeService.deleteTrainingType(entity);
        verify(trainingTypeStorage).deleteById(3L);
    }

    @Test
    void testDeleteTrainingType_InvalidEntity_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.deleteTrainingType(null));
        assertThrows(IllegalArgumentException.class, () -> {
            TrainingTypeEntity entity = new TrainingTypeEntity(TrainingType.YOGA);
            entity.setId(null);
            trainingTypeService.deleteTrainingType(entity);
        });
    }
}