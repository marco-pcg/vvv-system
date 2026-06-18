package com.cefet.VVVSystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Define the pointcut for all methods within the controller and service packages.
     */
    @Pointcut("within(com.cefet.VVVSystem.controller..*) || within(com.cefet.VVVSystem.service..*)")
    public void applicationPackagePointcut() {
        // Pointcut definition
    }

    /**
     * Intercepts method execution to log entry, exit, and execution time.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Entrada: {}.{}() com argumento[s] = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        } else {
            log.info("Entrada: {}.{}()",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;

            if (log.isDebugEnabled()) {
                log.debug("Saída: {}.{}() com resultado = {} (Tempo: {} ms)",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        result,
                        elapsedTime);
            } else {
                log.info("Saída: {}.{}() (Tempo: {} ms)",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        elapsedTime);
            }

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Argumento ilegal em {}.{}()",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            throw e;
        }
    }

    /**
     * Intercepts methods that throw an exception to log the error details.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exceção lançada em {}.{}() com causa = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL", e);
    }
}
