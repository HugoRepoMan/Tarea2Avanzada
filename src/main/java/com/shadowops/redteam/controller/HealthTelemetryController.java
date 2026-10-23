package com.shadowops.redteam.controller;

import com.shadowops.redteam.config.AuditoriaTelemetriaBuffer;
import com.shadowops.redteam.service.OperacionPentestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador público de monitoreo de disponibilidad del nodo C2 (excluido
 * del interceptor RedTeamClearanceInterceptor en WebMvcConfig).
 */
@RestController
@RequestMapping("/api/v1/health")
public class HealthTelemetryController {

    private final AuditoriaTelemetriaBuffer telemetriaBuffer;
    private final OperacionPentestService operacionService;

    public HealthTelemetryController(AuditoriaTelemetriaBuffer telemetriaBuffer,
                                     OperacionPentestService operacionService) {
        this.telemetriaBuffer = telemetriaBuffer;
        this.operacionService = operacionService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkC2Status() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("sistema", "ShadowOps: Red Team Engagement & Exploit Telemetry API");
        body.put("estadoNodoC2", "OPERACIONAL_SIGILO_ACTIVO");
        body.put("estrategiaRiesgoActiva", operacionService.obtenerEstrategiaRiesgoActiva());
        body.put("eventosAuditoriaRegistrados", telemetriaBuffer.totalEventos());
        body.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(body);
    }
}
