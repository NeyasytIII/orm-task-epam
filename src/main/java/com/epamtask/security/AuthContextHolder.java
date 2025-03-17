package com.epamtask.security;

public class AuthContextHolder {
    private static final ThreadLocal<String> usernameHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> passwordHolder = new ThreadLocal<>();

    public static void setCredentials(String username, String password) {
        usernameHolder.set(username);
        passwordHolder.set(password);
    }

    public static String getUsername() {
        return usernameHolder.get();
    }

    public static String getPassword() {
        return passwordHolder.get();
    }

    public static void clear() {
        usernameHolder.remove();
        passwordHolder.remove();
    }
}