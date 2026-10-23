package com.shadowops.redteam;

import com.shadowops.redteam.config.AuditoriaTelemetriaBuffer;
import com.shadowops.redteam.config.GeneradorHuellaCriptograficaBean;
import com.shadowops.redteam.service.OperacionPentestService;
import com.shadowops.redteam.service.strategy.CalculadoraRiesgoExploitStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de Inversión de Control (IoC), estrategias @Qualifier y beans explícitos (@Bean).
 */
@SpringBootTest
class RiskCalculationStrategyIoCTest {

    @Autowired
    @Qualifier("cvssBaseRiskStrategy")
    private CalculadoraRiesgoExploitStrategy baseStrategy;

    @Autowired
    @Qualifier("stealthOpsecRiskStrategy")
    private CalculadoraRiesgoExploitStrategy stealthStrategy;

    @Autowired
    private OperacionPentestService operacionService;

    @Autowired
    private GeneradorHuellaCriptograficaBean huellaBean;

    @Autowired
    private AuditoriaTelemetriaBuffer telemetriaBuffer;

    @Test
    @DisplayName("El contenedor IoC debe inyectar StealthOpsecRiskStrategy en OperacionPentestService mediante @Qualifier")
    void shouldInjectStealthStrategyInPrimaryService() {
        assertEquals("STEALTH_OPSEC_SOC_WEIGHTED_RISK", operacionService.obtenerEstrategiaRiesgoActiva());
    }

    @Test
    @DisplayName("Ambas estrategias deben calcular riesgos diferenciados considerando el nivel de sigilo OPSEC")
    void shouldCalculateDifferentRiskBetweenStrategies() {
        double riesgoBase = baseStrategy.calcularRiesgo(9.0, "CRITICA", 5);
        double riesgoStealth = stealthStrategy.calcularRiesgo(9.0, "CRITICA", 5);

        assertTrue(riesgoStealth > riesgoBase,
                "La estrategia StealthOpsec debe ponderar un mayor riesgo táctico cuando el nivel de sigilo es 5");
    }

    @Test
    @DisplayName("Los beans explícitos de ShadowOpsConfig (@Bean) deben estar activos y funcionales")
    void shouldProvideExplicitConfigurationBeans() {
        assertNotNull(huellaBean);
        assertNotNull(telemetriaBuffer);

        String firma = huellaBean.generarHuellaEvidencia(1L, 2L, "CVE-2024-21413",
                "CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H");
        assertTrue(firma.startsWith("EVD-SHA256-"));
        assertEquals(27, firma.length());
    }
}
