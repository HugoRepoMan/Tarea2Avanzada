package com.shadowops.redteam.exception;

/**
 * Excepción lanzada cuando existe un conflicto de estado o pertenencia de alcance
 * en una operación de pentesting (ej. objetivo no pertenece a la operación indicada).
 */
public class BusinessConflictException extends RuntimeException {

    public BusinessConflictException(String message) {
        super(message);
    }
}
