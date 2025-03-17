package com.epamtask.repository;
import com.epamtask.model.Training;
import com.epamtask.repository.impl.TrainingRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingRepositoryImplTest {

    private TrainingRepositoryImpl repository;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {
        entityManager = mock(EntityManager.class);
        repository = new TrainingRepositoryImpl();
        var field = TrainingRepositoryImpl.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void testSave() {
        Training training = new Training();
        Training result = repository.save(training);
        assertEquals(training, result);
        verify(entityManager).persist(training);
    }

    @Test
    void testUpdate() {
        Training training = new Training();
        repository.update(training);
        verify(entityManager).merge(training);
    }

    @Test
    void testFindById() {
        Training training = new Training();
        when(entityManager.find(Training.class, 1L)).thenReturn(training);
        Optional<Training> result = repository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(training, result.get());
    }

    @Test
    void testFindAll() {
        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Training()));
        List<Training> result = repository.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById() {
        Training training = new Training();
        when(entityManager.find(Training.class, 1L)).thenReturn(training);
        repository.deleteById(1L);
        verify(entityManager).remove(training);
    }

    @Test
    void testDeleteAttached() {
        Training training = new Training();
        when(entityManager.contains(training)).thenReturn(true);
        repository.delete(training);
        verify(entityManager).remove(training);
    }

    @Test
    void testDeleteDetached() {
        Training training = new Training();
        when(entityManager.contains(training)).thenReturn(false);
        when(entityManager.merge(training)).thenReturn(training);
        repository.delete(training);
        verify(entityManager).remove(training);
    }

    @Test
    void testFindByTraineeUsernameAndCriteria() {
        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        List<Training> result = repository.findByTraineeUsernameAndCriteria("trainee", new Date(), new Date(), "trainer", "YOGA");
        assertNotNull(result);
    }

    @Test
    void testFindByTrainerUsernameAndCriteria() {
        TypedQuery<Training> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        List<Training> result = repository.findByTrainerUsernameAndCriteria("trainer", new Date(), new Date(), "trainee");
        assertNotNull(result);
    }
}