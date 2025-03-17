package com.epamtask.repository;

import com.epamtask.model.TrainingTypeEntity;
import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository extends BaseRepository<TrainingTypeEntity, Long> {
    List<TrainingTypeEntity> findAll();
    Optional<TrainingTypeEntity> findByName(String name);
}