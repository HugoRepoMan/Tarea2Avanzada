package com.shadowops.redteam.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación de validación personalizada que verifica que el vector de severidad
 * técnica cumpla estrictamente con la nomenclatura oficial del estándar CVSS v3.1.
 * Ejemplo válido: CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H
 */
@Documented
@Constraint(validatedBy = CvssVectorValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidVectorCVSS {

    String message() default "Vector CVSS inválido: debe cumplir estrictamente el estándar oficial CVSS v3.1 (ej. CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
