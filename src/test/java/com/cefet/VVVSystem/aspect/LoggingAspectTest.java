package com.cefet.VVVSystem.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @InjectMocks
    private LoggingAspect loggingAspect;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @BeforeEach
    void setUp() {
        // leniency can be applied if needed, but we'll mock per test
    }

    @Test
    void logAround_ShouldProceedAndReturnResult() throws Throwable {
        // Arrange
        String expectedResult = "Sucesso";
        when(proceedingJoinPoint.proceed()).thenReturn(expectedResult);
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringTypeName()).thenReturn("com.cefet.VVVSystem.service.DummyService");
        when(signature.getName()).thenReturn("dummyMethod");

        // Act
        Object actualResult = loggingAspect.logAround(proceedingJoinPoint);

        // Assert
        assertEquals(expectedResult, actualResult);
        verify(proceedingJoinPoint, times(1)).proceed();
        verify(proceedingJoinPoint, atLeastOnce()).getSignature();
    }

    @Test
    void logAround_ShouldThrowIllegalArgumentException() throws Throwable {
        // Arrange
        when(proceedingJoinPoint.proceed()).thenThrow(new IllegalArgumentException("Argumento inválido"));
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringTypeName()).thenReturn("com.cefet.VVVSystem.service.DummyService");
        when(signature.getName()).thenReturn("dummyMethod");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loggingAspect.logAround(proceedingJoinPoint);
        });

        assertEquals("Argumento inválido", exception.getMessage());
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    void logAfterThrowing_ShouldExecuteWithoutErrors() {
        // Arrange
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getDeclaringTypeName()).thenReturn("com.cefet.VVVSystem.service.DummyService");
        when(signature.getName()).thenReturn("dummyMethod");
        
        Exception exception = new RuntimeException("Erro genérico");

        // Act & Assert
        assertDoesNotThrow(() -> {
            loggingAspect.logAfterThrowing(joinPoint, exception);
        });
        
        verify(joinPoint, atLeastOnce()).getSignature();
    }
}
