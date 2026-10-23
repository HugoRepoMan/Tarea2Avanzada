package com.shadowops.redteam.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadowops.redteam.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * Interceptor Orden 2: Verifica la presencia y validez criptográfica del header
 * personalizado X-RedTeam-Clearance-Token (prefijo obligatorio RT-OPS- seguido
 * de al menos 8 caracteres alfanuméricos, ej. RT-OPS-9948A7F2).
 */
@Component
public class RedTeamClearanceInterceptor implements HandlerInterceptor {

    public static final String CLEARANCE_HEADER = "X-RedTeam-Clearance-Token";
    private static final Pattern CLEARANCE_TOKEN_PATTERN = Pattern.compile("^RT-OPS-[A-Za-z0-9]{8,}$");
    private static final Logger log = LoggerFactory.getLogger(RedTeamClearanceInterceptor.class);

    private final ObjectMapper objectMapper;

    public RedTeamClearanceInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        String clearanceToken = request.getHeader(CLEARANCE_HEADER);

        if (clearanceToken == null || clearanceToken.isBlank()) {
            log.warn("[ROE-SECURITY-ALERT] Petición bloqueada sin header {} en URI {}",
                    CLEARANCE_HEADER, request.getRequestURI());
            writeErrorResponse(
                    response,
                    HttpStatus.UNAUTHORIZED,
                    "ROE_MISSING_CLEARANCE_TOKEN",
                    "Violación de Rules of Engagement (RoE): Falta el header obligatorio X-RedTeam-Clearance-Token",
                    request.getRequestURI()
            );
            return false;
        }

        if (!CLEARANCE_TOKEN_PATTERN.matcher(clearanceToken.trim()).matches()) {
            log.warn("[ROE-SECURITY-ALERT] Token de operador inválido '{}' en URI {}",
                    clearanceToken, request.getRequestURI());
            writeErrorResponse(
                    response,
                    HttpStatus.FORBIDDEN,
                    "ROE_INVALID_CLEARANCE_TOKEN",
                    "Violación de Rules of Engagement (RoE): Token de operador inválido. Debe iniciar con RT-OPS- seguido de al menos 8 caracteres alfanuméricos",
                    request.getRequestURI()
            );
            return false;
        }

        return true;
    }

    private void writeErrorResponse(HttpServletResponse response,
                                    HttpStatus status,
                                    String errorCode,
                                    String message,
                                    String path) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ApiErrorResponse errorBody = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                errorCode,
                message,
                path,
                Collections.emptyList()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorBody));
    }
}
