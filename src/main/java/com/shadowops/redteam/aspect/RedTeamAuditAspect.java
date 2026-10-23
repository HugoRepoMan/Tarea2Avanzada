package com.shadowops.redteam.aspect;

import com.shadowops.redteam.config.AuditoriaTelemetriaBuffer;
import com.shadowops.redteam.model.HallazgoVulnerabilidad;
import com.shadowops.redteam.model.ObjetivoRed;
import com.shadowops.redteam.model.OperacionPentest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Aspecto 2: Auditoría táctica (@AfterReturning) que registra un rastro inmutable
 * cada vez que se crea o actualiza una operación, se añade un objetivo o se reporta un exploit.
 */
@Aspect
@Component
@Order(3)
public class RedTeamAuditAspect {

    private static final Logger log = LoggerFactory.getLogger(RedTeamAuditAspect.class);

    private final AuditoriaTelemetriaBuffer auditoriaBuffer;

    public RedTeamAuditAspect(AuditoriaTelemetriaBuffer auditoriaBuffer) {
        this.auditoriaBuffer = auditoriaBuffer;
    }

    @AfterReturning(
            pointcut = "execution(* com.shadowops.redteam.service.OperacionPentestService.crearOperacion(..)) || " +
                       "execution(* com.shadowops.redteam.service.OperacionPentestService.cambiarEstadoOperacion(..))",
            returning = "operacion"
    )
    public void auditarOperacionPentest(JoinPoint joinPoint, OperacionPentest operacion) {
        if (operacion != null) {
            String resumen = String.format("accion=%s codigo=%s operador=%s estado=%s sigilo=%d",
                    joinPoint.getSignature().getName(),
                    operacion.getCodigoOperacion(),
                    operacion.getOperadorLider(),
                    operacion.getEstado(),
                    operacion.getNivelSigiloRequerido());
            log.info("[AOP-AUDIT-OPS] {}", resumen);
            auditoriaBuffer.registrarEvento("AOP-AUDIT-OPS", resumen);
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.shadowops.redteam.service.OperacionPentestService.registrarObjetivo(..))",
            returning = "objetivo"
    )
    public void auditarRegistroObjetivo(ObjetivoRed objetivo) {
        if (objetivo != null) {
            String resumen = String.format("nuevoObjetivo id=%d opId=%d host=%s exposicion=%s",
                    objetivo.getId(),
                    objetivo.getOperacionId(),
                    objetivo.getDireccionIpODominio(),
                    objetivo.getNivelExposicion());
            log.info("[AOP-AUDIT-TARGET] {}", resumen);
            auditoriaBuffer.registrarEvento("AOP-AUDIT-TARGET", resumen);
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.shadowops.redteam.service.OperacionPentestService.registrarHallazgo(..))",
            returning = "hallazgo"
    )
    public void auditarHallazgoExploit(HallazgoVulnerabilidad hallazgo) {
        if (hallazgo != null) {
            String resumen = String.format("exploitReportado id=%d opId=%d cve=%s severidad=%s riesgo=%.2f firma=%s",
                    hallazgo.getId(),
                    hallazgo.getOperacionId(),
                    hallazgo.getIdentificadorCve(),
                    hallazgo.getSeveridad(),
                    hallazgo.getRiesgoCalculado(),
                    hallazgo.getHuellaCriptografica());
            log.info("[AOP-AUDIT-EXPLOIT] {}", resumen);
            auditoriaBuffer.registrarEvento("AOP-AUDIT-EXPLOIT", resumen);
        }
    }
}
