package com.shadowops.redteam.model;

import java.time.LocalDateTime;

/**
 * Entidad secundaria que representa un hallazgo de vulnerabilidad o evidencia
 * de explotación (Vulnerability Finding / Exploit Evidence) sobre un ObjetivoRed.
 */
public class HallazgoVulnerabilidad {

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

    public HallazgoVulnerabilidad() {
    }

    public HallazgoVulnerabilidad(Long id, Long operacionId, Long objetivoId,
                                  String tituloExploit, String identificadorCve,
                                  String vectorCvss, Double puntajeCvss,
                                  Double riesgoCalculado, String severidad,
                                  String huellaCriptografica, LocalDateTime fechaDeteccion) {
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
}
