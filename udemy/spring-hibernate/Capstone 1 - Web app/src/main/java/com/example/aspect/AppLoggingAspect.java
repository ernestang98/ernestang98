package com.example.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AppLoggingAspect {

    private Logger myLogger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* com.example.controller.*.*(..))")
    private void forControllerPackage() {}

    @Pointcut("execution(* com.example.service.*.*(..))")
    private void forServicePackage() {}

    @Pointcut("execution(* com.example.dao.*.*(..))")
    private void forDaoPackage() {}

    @Pointcut("forControllerPackage() || forServicePackage() || forDaoPackage()")
    private void forAppFlow() {}

    @Before("forAppFlow()")
    public void before(JoinPoint theJoinPoint) {
        String theMethod = theJoinPoint.getSignature().toShortString();
        myLogger.info("=====>> in @Before: calling method: " + theMethod);
        Object[] args = theJoinPoint.getArgs();
        for (Object tempArg : args) {
            myLogger.info("=====>> argument: " + tempArg);
        }
    }

    @AfterReturning(
            pointcut="forAppFlow()",
            returning="theResult"
    )
    public void afterReturning(JoinPoint theJoinPoint, Object theResult) {
        String theMethod = theJoinPoint.getSignature().toShortString();
        myLogger.info("=====>> in @AfterReturning: from method: " + theMethod);
        myLogger.info("=====>> result: " + theResult);
    }

}
