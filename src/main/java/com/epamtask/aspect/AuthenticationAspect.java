package com.epamtask.aspect;

import com.epamtask.aspect.annotation.Authenticated;
import com.epamtask.security.AuthContextHolder;
import com.epamtask.service.AuthenticationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAspect {

    private final AuthenticationService authenticationService;

    public AuthenticationAspect(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Around("@annotation(authenticated)")
    public Object authenticate(ProceedingJoinPoint joinPoint, Authenticated authenticated) throws Throwable {
        String username = AuthContextHolder.getUsername();
        String password = AuthContextHolder.getPassword();

        if (username == null || password == null || !authenticationService.authenticate(username, password)) {
            throw new SecurityException(" Access denied: user is not authenticated.");
        }

        return joinPoint.proceed();
    }
}