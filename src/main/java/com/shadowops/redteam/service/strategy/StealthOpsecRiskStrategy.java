package com.shadowops.redteam.service.strategy;

import org.springframework.stereotype.Component;

/**
 * Estrategia 2: Calcula el riesgo táctico ponderando tanto el puntaje CVSS v3.1
 * como la probabilidad de detección por el EDR/SOC del Blue Team según el nivel
 * de sigilo requerido en la operación (1 a 5).
 */
@Component("stealthOpsecRiskStrategy")
public class StealthOpsecRiskStrategy implements CalculadoraRiesgoExploitStrategy {

    @Override
    public double calcularRiesgo(double puntajeCvss, String severidad, int nivelSigiloOperacion) {
        double factorRuidoSoc = switch (severidad != null ? severidad.toUpperCase() : "MEDIA") {
            case "CRITICA" -> 1.35;
            case "ALTA" -> 1.20;
            case "MEDIA" -> 1.05;
            default -> 0.95;
        };
        double penalizacionSigilo = 1.0 + (nivelSigiloOperacion * 0.08);
        double resultado = puntajeCvss * factorRuidoSoc * penalizacionSigilo;
        return Math.round(resultado * 100.0) / 100.0;
    }

    @Override
    public String obtenerNombreEstrategia() {
        return "STEALTH_OPSEC_SOC_WEIGHTED_RISK";
    }
}
