package com.shadowops.redteam.interceptor;

import com.shadowops.redteam.config.AuditoriaTelemetriaBuffer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor Orden 1: Registra telemetría HTTP de entrada y salida (método, URI,
 * IP de origen, timestamp y estado HTTP de respuesta) en cada petición al nodo C2.
 */
@Component
public class HttpTelemetryLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(HttpTelemetryLoggingInterceptor.class);
    private static final String START_TIME_ATTR = "shadowops.startTimeMs";

    private final AuditoriaTelemetriaBuffer telemetriaBuffer;

    public HttpTelemetryLoggingInterceptor(AuditoriaTelemetriaBuffer telemetriaBuffer) {
        this.telemetriaBuffer = telemetriaBuffer;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTR, startTime);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();

        log.info("[C2-HTTP-IN]  method={} uri={} ip={}", method, uri, clientIp);
        telemetriaBuffer.registrarEvento("HTTP-IN",
                String.format("%s %s from %s", method, uri, clientIp));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Object startAttr = request.getAttribute(START_TIME_ATTR);
        long elapsedMs = 0L;
        if (startAttr instanceof Long startTime) {
            elapsedMs = System.currentTimeMillis() - startTime;
        }
        int status = response.getStatus();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.info("[C2-HTTP-OUT] method={} uri={} status={} latencyMs={}",
                method, uri, status, elapsedMs);
        telemetriaBuffer.registrarEvento("HTTP-OUT",
                String.format("%s %s -> status=%d (%dms)", method, uri, status, elapsedMs));
    }
}
