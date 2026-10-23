package com.shadowops.redteam.dto;

import com.shadowops.redteam.validation.ValidVectorCVSS;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para reportar un HallazgoVulnerabilidad (Exploit Evidence)
 * sobre un objetivo de red dentro de una operación activa.
 */
public class HallazgoVulnerabilidadRequestDTO {

    @NotNull(message = "El ID del objetivo de red es obligatorio")
    @Positive(message = "El ID del objetivo de red debe ser un número positivo")
    private Long objetivoId;

    @NotBlank(message = "El título del exploit o hallazgo es obligatorio")
    @Size(min = 5, max = 150, message = "El título del exploit debe tener entre 5 y 150 caracteres")
    private String tituloExploit;

    @NotBlank(message = "El identificador CVE/CWE es obligatorio")
    @Pattern(
            regexp = "^(CVE-\\d{4}-\\d{4,7}|CWE-\\d{1,5})$",
            message = "El identificador debe tener formato oficial CVE-YYYY-NNNN o CWE-NNN"
    )
    private String identificadorCve;

    @NotBlank(message = "El vector CVSS es obligatorio")
    @ValidVectorCVSS
    private String vectorCvss;

    @NotNull(message = "El puntaje base CVSS es obligatorio")
    @DecimalMin(value = "0.0", message = "El puntaje CVSS no puede ser menor a 0.0")
    @DecimalMax(value = "10.0", message = "El puntaje CVSS no puede superar 10.0")
    private Double puntajeCvss;

    @NotBlank(message = "La severidad táctica es obligatoria")
    @Pattern(
            regexp = "^(BAJA|MEDIA|ALTA|CRITICA)$",
            message = "La severidad debe ser BAJA, MEDIA, ALTA o CRITICA"
    )
    private String severidad;

    public HallazgoVulnerabilidadRequestDTO() {
    }

    public HallazgoVulnerabilidadRequestDTO(Long objetivoId, String tituloExploit,
                                            String identificadorCve, String vectorCvss,
                                            Double puntajeCvss, String severidad) {
        this.objetivoId = objetivoId;
        this.tituloExploit = tituloExploit;
        this.identificadorCve = identificadorCve;
        this.vectorCvss = vectorCvss;
        this.puntajeCvss = puntajeCvss;
        this.severidad = severidad;
    }

    public Long getObjetivoId() {
        return objetivoId;
    }

    public void setObjetivoId(Long objetivoId) {
        this.objetivoId = objetivoId;
    }

    public String getTituloExploit() {
        return tituloExploit;
    }

    public void setTituloExploit(String tituloExploit) {
        this.tituloExploit = tituloExploit;
    }

    public String getIdentificadorCve() {
        return identificadorCve;
    }

    public void setIdentificadorCve(String identificadorCve) {
        this.identificadorCve = identificadorCve;
    }

    public String getVectorCvss() {
        return vectorCvss;
    }

    public void setVectorCvss(String vectorCvss) {
        this.vectorCvss = vectorCvss;
    }

    public Double getPuntajeCvss() {
        return puntajeCvss;
    }

    public void setPuntajeCvss(Double puntajeCvss) {
        this.puntajeCvss = puntajeCvss;
    }

    public String getSeveridad() {
        return severidad;
    }

    public void setSeveridad(String severidad) {
        this.severidad = severidad;
    }
}
