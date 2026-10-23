package com.shadowops.redteam.dto;

import com.shadowops.redteam.model.HallazgoVulnerabilidad;
import com.shadowops.redteam.model.ObjetivoRed;
import com.shadowops.redteam.model.OperacionPentest;

import java.util.List;

/**
 * DTO de respuesta enriquecido que consolida una OperacionPentest junto
 * con sus objetivos de red en alcance y hallazgos de vulnerabilidad.
 */
public class OperacionDetalleResponseDTO {

    private OperacionPentest operacion;
    private List<ObjetivoRed> objetivos;
    private List<HallazgoVulnerabilidad> hallazgos;

    public OperacionDetalleResponseDTO() {
    }

    public OperacionDetalleResponseDTO(OperacionPentest operacion,
                                       List<ObjetivoRed> objetivos,
                                       List<HallazgoVulnerabilidad> hallazgos) {
        this.operacion = operacion;
        this.objetivos = objetivos;
        this.hallazgos = hallazgos;
    }

    public OperacionPentest getOperacion() {
        return operacion;
    }

    public void setOperacion(OperacionPentest operacion) {
        this.operacion = operacion;
    }

    public List<ObjetivoRed> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(List<ObjetivoRed> objetivos) {
        this.objetivos = objetivos;
    }

    public List<HallazgoVulnerabilidad> getHallazgos() {
        return hallazgos;
    }

    public void setHallazgos(List<HallazgoVulnerabilidad> hallazgos) {
        this.hallazgos = hallazgos;
    }
}
