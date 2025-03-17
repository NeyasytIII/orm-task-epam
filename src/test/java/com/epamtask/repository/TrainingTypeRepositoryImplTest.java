package com.epamtask.repository;

import com.epamtask.model.TrainingTypeEntity;
import com.epamtask.repository.impl.TrainingTypeRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingTypeRepositoryImplTest {

    private TrainingTypeRepositoryImpl repository;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() throws Exception {
        entityManager = mock(EntityManager.class);
        repository = new TrainingTypeRepositoryImpl();
        var field = TrainingTypeRepositoryImpl.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void testFindAll() {
        TypedQuery<TrainingTypeEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(TrainingTypeEntity.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new TrainingTypeEntity()));
        List<TrainingTypeEntity> result = repository.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdate() {
        TrainingTypeEntity entity = new TrainingTypeEntity();
        repository.update(entity);
        verify(entityManager).merge(entity);
    }

    @Test
    void testFindByName() {
        TypedQuery<TrainingTypeEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(TrainingTypeEntity.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(List.of(new TrainingTypeEntity()).stream());
        Optional<TrainingTypeEntity> result = repository.findByName("YOGA");
        assertTrue(result.isPresent());
    }

    @Test
    void testFindById() {
        TrainingTypeEntity entity = new TrainingTypeEntity();
        when(entityManager.find(TrainingTypeEntity.class, 1L)).thenReturn(entity);
        Optional<TrainingTypeEntity> result = repository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void testSave() {
        TrainingTypeEntity entity = new TrainingTypeEntity();
        TrainingTypeEntity result = repository.save(entity);
        verify(entityManager).persist(entity);
        assertEquals(entity, result);
    }

    @Test
    void testDeleteAttached() {
        TrainingTypeEntity entity = new TrainingTypeEntity();
        when(entityManager.contains(entity)).thenReturn(true);
        repository.delete(entity);
        verify(entityManager).remove(entity);
    }

    @Test
    void testDeleteDetached() {
        TrainingTypeEntity entity = new TrainingTypeEntity();
        when(entityManager.contains(entity)).thenReturn(false);
        when(entityManager.merge(entity)).thenReturn(entity);
        repository.delete(entity);
        verify(entityManager).remove(entity);
    }

    @Test
    void testDeleteById() {
        TrainingTypeEntity entity = new TrainingTypeEntity();
        when(entityManager.find(TrainingTypeEntity.class, 1L)).thenReturn(entity);
        repository.deleteById(1L);
        verify(entityManager).remove(entity);
    }
}