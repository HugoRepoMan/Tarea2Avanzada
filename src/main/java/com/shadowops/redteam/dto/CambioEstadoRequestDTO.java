package com.shadowops.redteam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO de entrada para la transición de estado de una OperacionPentest.
 */
public class CambioEstadoRequestDTO {

    @NotBlank(message = "El nuevo estado de la operación es obligatorio")
    @Pattern(
            regexp = "^(PLANIFICADA|EN_EJECUCION|CONTENIDA|FINALIZADA)$",
            message = "Estado inválido: debe ser PLANIFICADA, EN_EJECUCION, CONTENIDA o FINALIZADA"
    )
    private String nuevoEstado;

    public CambioEstadoRequestDTO() {
    }

    public CambioEstadoRequestDTO(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }
}
