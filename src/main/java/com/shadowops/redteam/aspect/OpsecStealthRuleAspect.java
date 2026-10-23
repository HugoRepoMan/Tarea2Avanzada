package com.shadowops.redteam.aspect;

import com.shadowops.redteam.dto.CambioEstadoRequestDTO;
import com.shadowops.redteam.dto.HallazgoVulnerabilidadRequestDTO;
import com.shadowops.redteam.dto.OperacionPentestRequestDTO;
import com.shadowops.redteam.exception.OpsecRuleViolationException;
import com.shadowops.redteam.model.OperacionPentest;
import com.shadowops.redteam.repository.HallazgoVulnerabilidadRepository;
import com.shadowops.redteam.repository.OperacionPentestRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Aspecto 3: Hace cumplir la Regla de Sigilo Operacional Anti-SOC (OPSEC Throttling Rule)
 * mediante consejos @Before:
 * 1) Ningún RedTeamOperator puede mantener más de 2 operaciones en estado EN_EJECUCION simultáneamente.
 * 2) Ninguna operación puede registrar más de 3 hallazgos de severidad CRITICA en una ventana menor a 60 minutos.
 */
@Aspect
@Component
@Order(2)
public class OpsecStealthRuleAspect {

    public static final int MAX_OPERACIONES_SIMULTANEAS = 2;
    public static final int MAX_HALLAZGOS_CRITICOS_POR_VENTANA = 3;
    public static final int VENTANA_MINUTOS_OPSEC = 60;

    private static final String ESTADO_ACTIVO = "EN_EJECUCION";
    private static final String SEVERIDAD_CRITICA = "CRITICA";
    private static final Logger log = LoggerFactory.getLogger(OpsecStealthRuleAspect.class);

    private final OperacionPentestRepository operacionRepository;
    private final HallazgoVulnerabilidadRepository hallazgoRepository;

    public OpsecStealthRuleAspect(OperacionPentestRepository operacionRepository,
                                  HallazgoVulnerabilidadRepository hallazgoRepository) {
        this.operacionRepository = operacionRepository;
        this.hallazgoRepository = hallazgoRepository;
    }

    /**
     * Verifica el límite de operaciones simultáneas EN_EJECUCION al crear una operación.
     */
    @Before("execution(* com.shadowops.redteam.service.OperacionPentestService.crearOperacion(..)) && args(dto)")
    public void verificarSigiloAlCrearOperacion(OperacionPentestRequestDTO dto) {
        if (dto != null && ESTADO_ACTIVO.equalsIgnoreCase(dto.getEstado())) {
            validarConcurrenciaOperador(dto.getOperadorLider());
        }
    }

    /**
     * Verifica el límite de operaciones simultáneas EN_EJECUCION al cambiar el estado de una operación.
     */
    @Before("execution(* com.shadowops.redteam.service.OperacionPentestService.cambiarEstadoOperacion(..)) && args(operacionId, dto)")
    public void verificarSigiloAlCambiarEstado(Long operacionId, CambioEstadoRequestDTO dto) {
        if (dto != null && ESTADO_ACTIVO.equalsIgnoreCase(dto.getNuevoEstado())) {
            Optional<OperacionPentest> existenteOpt = operacionRepository.findById(operacionId);
            if (existenteOpt.isPresent()) {
                OperacionPentest existente = existenteOpt.get();
                if (!ESTADO_ACTIVO.equalsIgnoreCase(existente.getEstado())) {
                    validarConcurrenciaOperador(existente.getOperadorLider());
                }
            }
        }
    }

    /**
     * Verifica que no se registren más de 3 hallazgos de severidad CRITICA en menos de 60 minutos
     * sobre la misma operación.
     */
    @Before("execution(* com.shadowops.redteam.service.OperacionPentestService.registrarHallazgo(..)) && args(operacionId, dto)")
    public void verificarRafagaHallazgosCriticos(Long operacionId, HallazgoVulnerabilidadRequestDTO dto) {
        if (dto != null && SEVERIDAD_CRITICA.equalsIgnoreCase(dto.getSeveridad())) {
            LocalDateTime inicioVentana = LocalDateTime.now().minusMinutes(VENTANA_MINUTOS_OPSEC);
            long criticosRecientes = hallazgoRepository.countRecentByOperacionAndSeveridad(
                    operacionId,
                    SEVERIDAD_CRITICA,
                    inicioVentana
            );

            if (criticosRecientes >= MAX_HALLAZGOS_CRITICOS_POR_VENTANA) {
                log.warn("[OPSEC-THROTTLING-TRIGGERED] Operación ID {} bloqueada: {} hallazgos CRITICA en < {} min",
                        operacionId, criticosRecientes, VENTANA_MINUTOS_OPSEC);
                throw new OpsecRuleViolationException(
                        String.format("¡Alerta OPSEC! El SOC ha detectado tráfico anómalo: Se ha alcanzado el límite máximo de %d hallazgos de severidad CRITICA en una ventana menor a %d minutos para la operación ID %d.",
                                MAX_HALLAZGOS_CRITICOS_POR_VENTANA, VENTANA_MINUTOS_OPSEC, operacionId)
                );
            }
        }
    }

    private void validarConcurrenciaOperador(String operadorLider) {
        long activas = operacionRepository.countByOperadorLiderAndEstado(operadorLider, ESTADO_ACTIVO);
        if (activas >= MAX_OPERACIONES_SIMULTANEAS) {
            log.warn("[OPSEC-THROTTLING-TRIGGERED] Operador '{}' bloqueado: {} operaciones EN_EJECUCION activas",
                    operadorLider, activas);
            throw new OpsecRuleViolationException(
                    String.format("¡Alerta OPSEC! El SOC ha detectado tráfico anómalo: El operador '%s' ya mantiene el máximo permitido de %d operaciones EN_EJECUCION de manera simultánea.",
                            operadorLider, MAX_OPERACIONES_SIMULTANEAS)
            );
        }
    }
}
