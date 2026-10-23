package com.shadowops.redteam.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Implementación de ConstraintValidator que valida estructuralmente la cadena
 * de métricas base del estándar CVSS v3.1 (AV, AC, PR, UI, S, C, I, A).
 */
public class CvssVectorValidator implements ConstraintValidator<ValidVectorCVSS, String> {

    private static final Pattern CVSS_V31_PATTERN = Pattern.compile(
            "^CVSS:3\\.1/AV:[NALP]/AC:[LH]/PR:[NLH]/UI:[NR]/S:[UC]/C:[NLH]/I:[NLH]/A:[NLH]$"
    );

    @Override
    public void initialize(ValidVectorCVSS constraintAnnotation) {
        // Sin inicialización adicional requerida
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return CVSS_V31_PATTERN.matcher(value.trim()).matches();
    }
}
