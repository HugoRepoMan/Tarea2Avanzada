package com.shadowops.redteam.dto;

import com.shadowops.redteam.model.HallazgoVulnerabilidad;

import java.time.LocalDateTime;

/**
 * DTO de salida desacoplado (Mejora Técnica Fase 2) para representar un
 * HallazgoVulnerabilidad hacia la capa HTTP sin exponer directamente la
 * entidad interna de persistencia, incorporando telemetría calculada de
 * alerta SOC y recomendación de rotación de infraestructura C2.
 */
public class HallazgoVulnerabilidadResponseDTO {

    private Long id;
    private Long operacionId;
    private Long objetivoId;
    private String tituloExploit;
    private String identificadorCve;
    private String vectorCvss;
    private Double puntajeCvss;
    private Double riesgoCalculado;
    private String severidad;
    private String huellaCriptografica;
    private LocalDateTime fechaDeteccion;
    private String nivelAlertaSocEstimado;
    private Boolean requiereRotacionInfraestructuraC2;

    public HallazgoVulnerabilidadResponseDTO() {
    }

    public HallazgoVulnerabilidadResponseDTO(Long id, Long operacionId, Long objetivoId,
                                             String tituloExploit, String identificadorCve,
                                             String vectorCvss, Double puntajeCvss,
                                             Double riesgoCalculado, String severidad,
                                             String huellaCriptografica, LocalDateTime fechaDeteccion,
                                             String nivelAlertaSocEstimado,
                                             Boolean requiereRotacionInfraestructuraC2) {
        this.id = id;
        this.operacionId = operacionId;
        this.objetivoId = objetivoId;
        this.tituloExploit = tituloExploit;
        this.identificadorCve = identificadorCve;
        this.vectorCvss = vectorCvss;
        this.puntajeCvss = puntajeCvss;
        this.riesgoCalculado = riesgoCalculado;
        this.severidad = severidad;
        this.huellaCriptografica = huellaCriptografica;
        this.fechaDeteccion = fechaDeteccion;
        this.nivelAlertaSocEstimado = nivelAlertaSocEstimado;
        this.requiereRotacionInfraestructuraC2 = requiereRotacionInfraestructuraC2;
    }

    /**
     * Método factoría estático que transforma una entidad interna HallazgoVulnerabilidad
     * en su contrato de respuesta desacoplado con indicadores tácticos anti-SOC.
     */
    public static HallazgoVulnerabilidadResponseDTO fromEntity(HallazgoVulnerabilidad entity) {
        double riesgo = entity.getRiesgoCalculado() != null ? entity.getRiesgoCalculado() : 0.0;
        String alertaSoc;
        if (riesgo >= 15.0) {
            alertaSoc = "CRITICO_INMINENTE";
        } else if (riesgo >= 10.0) {
            alertaSoc = "ELEVADO";
        } else {
            alertaSoc = "MODERADO_SIGILOSO";
        }

        boolean rotarC2 = riesgo >= 15.0 || "CRITICA".equalsIgnoreCase(entity.getSeveridad());

        return new HallazgoVulnerabilidadResponseDTO(
                entity.getId(),
                entity.getOperacionId(),
                entity.getObjetivoId(),
                entity.getTituloExploit(),
                entity.getIdentificadorCve(),
                entity.getVectorCvss(),
                entity.getPuntajeCvss(),
                entity.getRiesgoCalculado(),
                entity.getSeveridad(),
                entity.getHuellaCriptografica(),
                entity.getFechaDeteccion(),
                alertaSoc,
                rotarC2
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOperacionId() {
        return operacionId;
    }

    public void setOperacionId(Long operacionId) {
        this.operacionId = operacionId;
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

    public Double getRiesgoCalculado() {
        return riesgoCalculado;
    }

    public void setRiesgoCalculado(Double riesgoCalculado) {
        this.riesgoCalculado = riesgoCalculado;
    }

    public String getSeveridad() {
        return severidad;
    }

    public void setSeveridad(String severidad) {
        this.severidad = severidad;
    }

    public String getHuellaCriptografica() {
        return huellaCriptografica;
    }

    public void setHuellaCriptografica(String huellaCriptografica) {
        this.huellaCriptografica = huellaCriptografica;
    }

    public LocalDateTime getFechaDeteccion() {
        return fechaDeteccion;
    }

    public void setFechaDeteccion(LocalDateTime fechaDeteccion) {
        this.fechaDeteccion = fechaDeteccion;
    }

    public String getNivelAlertaSocEstimado() {
        return nivelAlertaSocEstimado;
    }

    public void setNivelAlertaSocEstimado(String nivelAlertaSocEstimado) {
        this.nivelAlertaSocEstimado = nivelAlertaSocEstimado;
    }

    public Boolean getRequiereRotacionInfraestructuraC2() {
        return requiereRotacionInfraestructuraC2;
    }

    public void setRequiereRotacionInfraestructuraC2(Boolean requiereRotacionInfraestructuraC2) {
        this.requiereRotacionInfraestructuraC2 = requiereRotacionInfraestructuraC2;
    }
}
