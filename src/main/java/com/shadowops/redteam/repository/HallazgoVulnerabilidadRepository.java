package com.shadowops.redteam.repository;

import com.shadowops.redteam.model.HallazgoVulnerabilidad;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria thread-safe para la gestión de entidades HallazgoVulnerabilidad.
 */
@Repository
public class HallazgoVulnerabilidadRepository {

    private final Map<Long, HallazgoVulnerabilidad> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public HallazgoVulnerabilidad save(HallazgoVulnerabilidad hallazgo) {
        if (hallazgo.getId() == null) {
            hallazgo.setId(idGenerator.getAndIncrement());
        }
        if (hallazgo.getFechaDeteccion() == null) {
            hallazgo.setFechaDeteccion(LocalDateTime.now());
        }
        storage.put(hallazgo.getId(), hallazgo);
        return hallazgo;
    }

    public Optional<HallazgoVulnerabilidad> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<HallazgoVulnerabilidad> findByOperacionId(Long operacionId) {
        return storage.values().stream()
                .filter(h -> operacionId.equals(h.getOperacionId()))
                .sorted(Comparator.comparing(HallazgoVulnerabilidad::getId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Cuenta cuántos hallazgos de una severidad específica (ej. CRITICA) se han
     * registrado en una operación dentro de la ventana temporal posterior a 'desde'.
     */
    public long countRecentByOperacionAndSeveridad(Long operacionId, String severidad, LocalDateTime desde) {
        return storage.values().stream()
                .filter(h -> operacionId.equals(h.getOperacionId()))
                .filter(h -> severidad.equalsIgnoreCase(h.getSeveridad()))
                .filter(h -> h.getFechaDeteccion() != null && !h.getFechaDeteccion().isBefore(desde))
                .count();
    }

    public void deleteAll() {
        storage.clear();
        idGenerator.set(1);
    }
}
