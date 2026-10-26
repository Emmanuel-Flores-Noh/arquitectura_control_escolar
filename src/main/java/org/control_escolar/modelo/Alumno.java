package org.control_escolar.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio que representa a un Alumno.
 */
public class Alumno {

    private String id;
    private String matricula;
    private String nombre;
    private List<Calificacion> calificaciones;

    public Alumno() {
        this.calificaciones = new ArrayList<>();
    }

    public Alumno(String id, String matricula, String nombre) {
        this.id = id;
        this.matricula = matricula;
        this.nombre = nombre;
        this.calificaciones = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Calificacion> getCalificaciones() {
        if (calificaciones == null) {
            calificaciones = new ArrayList<>();
        }
        return calificaciones;
    }

    public void setCalificaciones(List<Calificacion> calificaciones) {
        this.calificaciones = calificaciones != null ? calificaciones : new ArrayList<>();
    }

    public void agregarCalificacion(Calificacion calificacion) {
        getCalificaciones().add(calificacion);
    }

    @Override
    public String toString() {
        return matricula + " - " + nombre;
    }
}
