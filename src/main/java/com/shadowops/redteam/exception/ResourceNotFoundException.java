package com.shadowops.redteam.exception;

/**
 * Excepción lanzada cuando una OperacionPentest u ObjetivoRed solicitado no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
