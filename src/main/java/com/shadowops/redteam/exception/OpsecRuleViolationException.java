package com.shadowops.redteam.exception;

/**
 * Excepción lanzada por el aspecto OpsecStealthRuleAspect cuando una acción
 * ofensiva infringe la Regla de Sigilo Operacional Anti-SOC (OPSEC Throttling Rule).
 */
public class OpsecRuleViolationException extends RuntimeException {

    public OpsecRuleViolationException(String message) {
        super(message);
    }
}
