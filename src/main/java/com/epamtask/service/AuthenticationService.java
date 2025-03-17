package com.epamtask.service;

public interface AuthenticationService {
    boolean authenticate(String username, String password);
}
