package com.shadowops.redteam.config;

import com.shadowops.redteam.interceptor.HttpTelemetryLoggingInterceptor;
import com.shadowops.redteam.interceptor.RedTeamClearanceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de Spring MVC que registra y ordena los interceptores HTTP
 * y define las rutas excluidas para monitoreo de disponibilidad C2.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final HttpTelemetryLoggingInterceptor telemetryLoggingInterceptor;
    private final RedTeamClearanceInterceptor clearanceInterceptor;

    public WebMvcConfig(HttpTelemetryLoggingInterceptor telemetryLoggingInterceptor,
                        RedTeamClearanceInterceptor clearanceInterceptor) {
        this.telemetryLoggingInterceptor = telemetryLoggingInterceptor;
        this.clearanceInterceptor = clearanceInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Orden 1: Telemetría HTTP para todas las rutas de la API
        registry.addInterceptor(telemetryLoggingInterceptor)
                .addPathPatterns("/api/**")
                .order(1);

        // Orden 2: Validación de X-RedTeam-Clearance-Token excluyendo monitoreo /api/v1/health/** y /error
        registry.addInterceptor(clearanceInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/health/**", "/error")
                .order(2);
    }
}
