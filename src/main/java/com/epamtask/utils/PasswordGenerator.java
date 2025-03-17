package com.epamtask.utils;

import com.epamtask.aspect.annotation.Loggable;

import java.util.UUID;

public class PasswordGenerator {
    @Loggable
    public static String generatePassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}