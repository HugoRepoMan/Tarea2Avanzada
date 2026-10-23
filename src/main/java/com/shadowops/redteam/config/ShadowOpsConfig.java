package com.shadowops.redteam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración de Spring (@Configuration) que registra explícitamente
 * beans de infraestructura táctica en el contenedor IoC.
 */
@Configuration
public class ShadowOpsConfig {

    @Bean
    public AuditoriaTelemetriaBuffer auditoriaTelemetriaBuffer() {
        return new AuditoriaTelemetriaBuffer(500);
    }

    @Bean
    public GeneradorHuellaCriptograficaBean generadorHuellaCriptograficaBean() {
        return new GeneradorHuellaCriptograficaBean("EVD-SHA256");
    }
}
