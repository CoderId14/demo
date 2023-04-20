package com.example.demo.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.demo.api..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs(); // get method arguments
        String argsString = Arrays.toString(args); // convert to string
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - startTime;
        LOGGER.info("Method {} called with args {} took {} ms", joinPoint.getSignature(), argsString, elapsedTime);
        return result;
    }

}