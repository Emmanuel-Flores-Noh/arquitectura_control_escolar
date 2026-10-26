package org.control_escolar.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de datos que representa el reporte académico consolidado de un alumno.
 */
public class Reporte {

    private String alumnoId;
    private String alumnoNombre;
    private String alumnoMatricula;
    private List<Calificacion> calificaciones;
    private double promedio;
    private String estadoAprobacion;
    private String fechaGeneracion;

    public Reporte() {
        this.calificaciones = new ArrayList<>();
    }

    public Reporte(String alumnoId, String alumnoNombre, String alumnoMatricula,
                   List<Calificacion> calificaciones, double promedio, String estadoAprobacion) {
        this.alumnoId = alumnoId;
        this.alumnoNombre = alumnoNombre;
        this.alumnoMatricula = alumnoMatricula;
        this.calificaciones = calificaciones != null ? new ArrayList<>(calificaciones) : new ArrayList<>();
        this.promedio = promedio;
        this.estadoAprobacion = estadoAprobacion;
        this.fechaGeneracion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(String alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getAlumnoNombre() {
        return alumnoNombre;
    }

    public void setAlumnoNombre(String alumnoNombre) {
        this.alumnoNombre = alumnoNombre;
    }

    public String getAlumnoMatricula() {
        return alumnoMatricula;
    }

    public void setAlumnoMatricula(String alumnoMatricula) {
        this.alumnoMatricula = alumnoMatricula;
    }

    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public String getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(String estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}
