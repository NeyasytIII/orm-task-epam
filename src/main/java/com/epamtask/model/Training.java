package com.epamtask.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainingId;

    @Column(nullable = false)
    private Long traineeId;

    @Column(nullable = false)
    private Long trainerId;

    @ManyToOne
    @JoinColumn(name = "trainer_id", referencedColumnName = "trainerId", nullable = false, insertable = false, updatable = false)
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "trainee_id", referencedColumnName = "traineeId", nullable = false, insertable = false, updatable = false)
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingTypeEntity trainingType;

    @Column(nullable = false)
    private String trainingName;

    @Column(nullable = false)
    private Date trainingDate;

    @Column(nullable = false)
    private String trainingDuration;

    public Training(
            Long trainingId,
            Long traineeId,
            Long trainerId,
            String trainingName,
            TrainingType type,
            Date trainingDate,
            String trainingDuration
    ) {
        this.trainingId = trainingId;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = (type != null) ? new TrainingTypeEntity(type) : null;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }
    public Training(
            Trainee trainee,
            Trainer trainer,
            TrainingType type,
            String trainingName,
            Date trainingDate,
            String trainingDuration
    ) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.traineeId = (trainee != null) ? trainee.getTraineeId() : null;
        this.trainerId = (trainer != null) ? trainer.getTrainerId() : null;
        this.trainingName = trainingName;
        this.trainingType = (type != null) ? new TrainingTypeEntity(type) : null;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public Training() {
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Long traineeId) {
        this.traineeId = traineeId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public TrainingTypeEntity getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingTypeEntity trainingType) {
        this.trainingType = trainingType;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(String trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public TrainingType getType() {
        return (trainingType != null) ? trainingType.getType() : null;
    }

    public void setType(TrainingType enumType) {
        if (enumType == null) {
            this.trainingType = null;
        } else if (this.trainingType == null) {
            this.trainingType = new TrainingTypeEntity(enumType);
        } else {
            this.trainingType.setType(enumType);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Objects.equals(trainingId, training.trainingId)
                && Objects.equals(traineeId, training.traineeId)
                && Objects.equals(trainerId, training.trainerId)
                && Objects.equals(trainingType, training.trainingType)
                && Objects.equals(trainingName, training.trainingName)
                && Objects.equals(trainingDate, training.trainingDate)
                && Objects.equals(trainingDuration, training.trainingDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                trainingId,
                traineeId,
                trainerId,
                trainingType,
                trainingName,
                trainingDate,
                trainingDuration
        );
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + trainingId +
                ", traineeId=" + traineeId +
                ", trainerId=" + trainerId +
                ", trainingType=" + trainingType +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration='" + trainingDuration + '\'' +
                '}';
    }
}