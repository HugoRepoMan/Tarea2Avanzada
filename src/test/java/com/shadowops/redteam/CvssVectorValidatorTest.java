package com.shadowops.redteam;

import com.shadowops.redteam.validation.CvssVectorValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para el validador personalizado CvssVectorValidator (@ValidVectorCVSS).
 */
class CvssVectorValidatorTest {

    private CvssVectorValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CvssVectorValidator();
    }

    @Test
    @DisplayName("Debe aceptar un vector CVSS v3.1 crítico válido de red")
    void shouldAcceptValidCriticalNetworkVector() {
        String vector = "CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H";
        assertTrue(validator.isValid(vector, null));
    }

    @Test
    @DisplayName("Debe aceptar un vector CVSS v3.1 local con alcance cambiado (S:C)")
    void shouldAcceptValidLocalChangedScopeVector() {
        String vector = "CVSS:3.1/AV:L/AC:H/PR:L/UI:R/S:C/C:H/I:L/A:N";
        assertTrue(validator.isValid(vector, null));
    }

    @Test
    @DisplayName("Debe rechazar vectores nulos, vacíos o con versión CVSS 2.0/4.0")
    void shouldRejectNullBlankOrWrongVersion() {
        assertFalse(validator.isValid(null, null));
        assertFalse(validator.isValid("   ", null));
        assertFalse(validator.isValid("CVSS:2.0/AV:N/AC:L/Au:N/C:C/I:C/A:C", null));
        assertFalse(validator.isValid("CVSS:3.1/AV:X/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H", null));
    }

    @Test
    @DisplayName("Debe rechazar un vector CVSS v3.1 incompleto al que le falten métricas base")
    void shouldRejectIncompleteCvssVector() {
        String incomplete = "CVSS:3.1/AV:N/AC:L/PR:N/UI:N";
        assertFalse(validator.isValid(incomplete, null));
    }
}
