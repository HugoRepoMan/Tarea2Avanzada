package com.shadowops.redteam.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Bean de infraestructura administrado explícitamente en ShadowOpsConfig (@Bean)
 * para almacenar de forma thread-safe los eventos de telemetría HTTP y auditoría AOP.
 */
public class AuditoriaTelemetriaBuffer {

    private final int capacidadMaxima;
    private final List<String> eventos = new CopyOnWriteArrayList<>();

    public AuditoriaTelemetriaBuffer(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public void registrarEvento(String categoria, String detalle) {
        String entrada = String.format("[%s] [%s] %s", LocalDateTime.now(), categoria, detalle);
        if (eventos.size() >= capacidadMaxima && !eventos.isEmpty()) {
            eventos.remove(0);
        }
        eventos.add(entrada);
    }

    public List<String> obtenerEventos() {
        return Collections.unmodifiableList(new ArrayList<>(eventos));
    }

    public int totalEventos() {
        return eventos.size();
    }

    public void limpiar() {
        eventos.clear();
    }
}
