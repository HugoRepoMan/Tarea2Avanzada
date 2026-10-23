package com.shadowops.redteam.repository;

import com.shadowops.redteam.model.ObjetivoRed;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositorio en memoria thread-safe para la gestión de entidades ObjetivoRed.
 */
@Repository
public class ObjetivoRedRepository {

    private final Map<Long, ObjetivoRed> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ObjetivoRed save(ObjetivoRed objetivo) {
        if (objetivo.getId() == null) {
            objetivo.setId(idGenerator.getAndIncrement());
        }
        storage.put(objetivo.getId(), objetivo);
        return objetivo;
    }

    public Optional<ObjetivoRed> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<ObjetivoRed> findByOperacionId(Long operacionId) {
        return storage.values().stream()
                .filter(obj -> operacionId.equals(obj.getOperacionId()))
                .sorted(Comparator.comparing(ObjetivoRed::getId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void deleteAll() {
        storage.clear();
        idGenerator.set(1);
    }
}
