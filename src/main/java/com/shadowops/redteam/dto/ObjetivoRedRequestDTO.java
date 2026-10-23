package com.shadowops.redteam.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO de entrada para añadir un ObjetivoRed (Target Host) al alcance de una operación.
 */
public class ObjetivoRedRequestDTO {

    @NotBlank(message = "La dirección IP o dominio del objetivo es obligatoria")
    @Size(min = 4, max = 120, message = "La dirección IP o dominio debe tener entre 4 y 120 caracteres")
    private String direccionIpODominio;

    @NotBlank(message = "El sistema operativo detectado es obligatorio")
    @Size(min = 2, max = 80, message = "El sistema operativo debe tener entre 2 y 80 caracteres")
    private String sistemaOperativo;

    @NotEmpty(message = "Debe especificar al menos un puerto abierto en el reconocimiento")
    private List<@NotNull @Min(1) @Max(65535) Integer> puertosAbiertos;

    @NotBlank(message = "El nivel de exposición del activo es obligatorio")
    @Pattern(
            regexp = "^(EXTERNA_INTERNET|DMZ|RED_INTERNA|SEGMENTO_CRITICO)$",
            message = "Nivel de exposición inválido: debe ser EXTERNA_INTERNET, DMZ, RED_INTERNA o SEGMENTO_CRITICO"
    )
    private String nivelExposicion;

    @NotNull(message = "Debe indicar si el objetivo ya fue comprometido")
    private Boolean comprometido;

    public ObjetivoRedRequestDTO() {
    }

    public ObjetivoRedRequestDTO(String direccionIpODominio, String sistemaOperativo,
                                 List<Integer> puertosAbiertos, String nivelExposicion,
                                 Boolean comprometido) {
        this.direccionIpODominio = direccionIpODominio;
        this.sistemaOperativo = sistemaOperativo;
        this.puertosAbiertos = puertosAbiertos;
        this.nivelExposicion = nivelExposicion;
        this.comprometido = comprometido;
    }

    public String getDireccionIpODominio() {
        return direccionIpODominio;
    }

    public void setDireccionIpODominio(String direccionIpODominio) {
        this.direccionIpODominio = direccionIpODominio;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public List<Integer> getPuertosAbiertos() {
        return puertosAbiertos;
    }

    public void setPuertosAbiertos(List<Integer> puertosAbiertos) {
        this.puertosAbiertos = puertosAbiertos;
    }

    public String getNivelExposicion() {
        return nivelExposicion;
    }

    public void setNivelExposicion(String nivelExposicion) {
        this.nivelExposicion = nivelExposicion;
    }

    public Boolean getComprometido() {
        return comprometido;
    }

    public void setComprometido(Boolean comprometido) {
        this.comprometido = comprometido;
    }
}
