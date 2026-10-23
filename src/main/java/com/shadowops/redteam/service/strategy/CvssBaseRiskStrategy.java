package com.shadowops.redteam.service.strategy;

import org.springframework.stereotype.Component;

/**
 * Estrategia 1: Calcula el riesgo técnico puro basado directamente en el puntaje
 * base CVSS v3.1 y un factor de severidad estándar, sin penalización OPSEC.
 */
@Component("cvssBaseRiskStrategy")
public class CvssBaseRiskStrategy implements CalculadoraRiesgoExploitStrategy {

    @Override
    public double calcularRiesgo(double puntajeCvss, String severidad, int nivelSigiloOperacion) {
        double multiplicadorSeveridad = switch (severidad != null ? severidad.toUpperCase() : "MEDIA") {
            case "CRITICA" -> 1.15;
            case "ALTA" -> 1.05;
            case "MEDIA" -> 1.00;
            default -> 0.90;
        };
        double resultado = puntajeCvss * multiplicadorSeveridad;
        return Math.round(resultado * 100.0) / 100.0;
    }

    @Override
    public String obtenerNombreEstrategia() {
        return "CVSS_BASE_TECHNICAL_RISK";
    }
}
