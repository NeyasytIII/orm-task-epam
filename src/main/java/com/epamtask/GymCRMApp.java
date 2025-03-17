package com.epamtask;

import com.epamtask.facade.CoordinatorFacade;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.security.AuthContextHolder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.List;

public class GymCRMApp {

    public void start() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.epamtask");
        CoordinatorFacade facade = context.getBean(CoordinatorFacade.class);

        System.out.println("\nStarting GymCRMApp FULL Test...\n");

        try {
            facade.getTrainerFacade().createTrainer(100L, "John", "Doe", "CARDIO");
            facade.getTraineeFacade().createTrainee(101L, "Alice", "Brown", "123 Elm Street", new Date());
        } catch (Exception e) {
            System.out.println("Failed to create users: " + e.getMessage());
        }

        String trainerUsername = null;
        String traineeUsername = null;

        try {
            trainerUsername = facade.getTrainerFacade().getTrainerById(100L).orElseThrow().getUserName();
            traineeUsername = facade.getTraineeFacade().getTraineeById(101L).orElseThrow().getUserName();
        } catch (Exception e) {
            System.out.println("Failed to retrieve usernames: " + e.getMessage());
        }

        try {
            facade.getTrainerFacade().updatePassword(trainerUsername, "johnPass");
            facade.getTraineeFacade().updatePassword(traineeUsername, "alicePass");
        } catch (SecurityException e) {
            System.out.println("Access denied during password update: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Password update failed: " + e.getMessage());
        }

        try {
            facade.getTraineeFacade().deactivateTrainee(traineeUsername);
            System.out.println("Error: method should not execute without authentication");
        } catch (SecurityException e) {
            System.out.println("Access denied without authentication: " + e.getMessage());
        }

        AuthContextHolder.setCredentials(traineeUsername, "alicePass");

        try {
            facade.getTraineeFacade().updateTrainee(new Trainee(101L, "Alice", "Brown", "456 Maple St", new Date(), true));
            System.out.println("Trainee updated");

            facade.getTraineeFacade().assignTrainersToTrainee(traineeUsername, List.of(trainerUsername));
            System.out.println("Trainer assigned");

            facade.getTraineeFacade().deactivateTrainee(traineeUsername);
            System.out.println("Trainee deactivated");

            facade.getTraineeFacade().updateTrainee(new Trainee(101L, "Alice", "Brown", "789 Pine St", new Date(), true));
            System.out.println("Error: method should not work after deactivation");
        } catch (SecurityException e) {
            System.out.println("Access denied after deactivation: " + e.getMessage());
        }

        AuthContextHolder.clear();

        AuthContextHolder.setCredentials(trainerUsername, "johnPass");

        try {
            facade.getTrainerFacade().updateTrainer(new Trainer(100L, "John", "Doe", "CROSSFIT", true));
            System.out.println("Trainer updated");

            facade.getTrainerFacade().deactivateUser(trainerUsername);
            System.out.println("Trainer deactivated");

            facade.getTrainerFacade().updateTrainer(new Trainer(100L, "John", "Doe", "CROSSFIT", true));
            System.out.println("Error: method should not work after deactivation");
        } catch (SecurityException e) {
            System.out.println("Access denied after deactivation: " + e.getMessage());
        }

        AuthContextHolder.clear();
        context.close();

        System.out.println("\nGymCRMApp FULL Test Finished.");
    }
}
