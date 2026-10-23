package com.shadowops.redteam.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspecto 1: Trazabilidad técnica y medición de tiempo de ejecución en milisegundos
 * (@Around) para todas las invocaciones a la capa de servicio.
 */
@Aspect
@Component
@Order(1)
public class OperacionLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(OperacionLoggingAspect.class);

    @Around("execution(* com.shadowops.redteam.service.OperacionPentestService.*(..))")
    public Object measureAndLogServiceExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String args = Arrays.toString(joinPoint.getArgs());
        long startNano = System.nanoTime();

        log.info("[AOP-EXEC-START] Invocando {} con argumentos={}", methodName, args);
        try {
            Object result = joinPoint.proceed();
            long durationMs = (System.nanoTime() - startNano) / 1_000_000;
            log.info("[AOP-EXEC-END]   Completado {} exitosamente en {} ms", methodName, durationMs);
            return result;
        } catch (Throwable ex) {
            long durationMs = (System.nanoTime() - startNano) / 1_000_000;
            log.warn("[AOP-EXEC-FAIL]  Excepción en {} tras {} ms -> {}: {}",
                    methodName, durationMs, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }
}
