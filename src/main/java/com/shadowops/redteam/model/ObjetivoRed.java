package com.shadowops.redteam.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad secundaria que representa un activo u host objetivo (Target Host/Asset)
 * dentro del alcance autorizado de una OperacionPentest.
 */
public class ObjetivoRed {

    private Long id;
    private Long operacionId;
    private String direccionIpODominio;
    private String sistemaOperativo;
    private List<Integer> puertosAbiertos = new ArrayList<>();
    private String nivelExposicion;
    private Boolean comprometido;

    public ObjetivoRed() {
    }

    public ObjetivoRed(Long id, Long operacionId, String direccionIpODominio,
                       String sistemaOperativo, List<Integer> puertosAbiertos,
                       String nivelExposicion, Boolean comprometido) {
        this.id = id;
        this.operacionId = operacionId;
        this.direccionIpODominio = direccionIpODominio;
        this.sistemaOperativo = sistemaOperativo;
        this.puertosAbiertos = puertosAbiertos != null ? new ArrayList<>(puertosAbiertos) : new ArrayList<>();
        this.nivelExposicion = nivelExposicion;
        this.comprometido = comprometido;
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
