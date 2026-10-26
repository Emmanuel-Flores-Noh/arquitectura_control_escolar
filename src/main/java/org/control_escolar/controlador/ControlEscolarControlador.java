package org.control_escolar.controlador;

import org.control_escolar.broker.IBroker;
import org.control_escolar.broker.RespuestaBroker;
import org.control_escolar.modelo.Alumno;
import org.control_escolar.modelo.Reporte;

import java.util.Collections;
import java.util.List;

/**
 * Controlador del mvc
 * maneja las peticiones de la capa de presentacion y se las pasa al broker.
 */
public class ControlEscolarControlador {

    private final IBroker broker;

    public ControlEscolarControlador(IBroker broker) {
        if (broker == null) {
            throw new IllegalArgumentException("El broker no puede ser nulo.");
        }
        this.broker = broker;
    }

    public List<Alumno> obtenerAlumnos() {
        RespuestaBroker<List<Alumno>> respuesta = broker.listarAlumnos();
        return respuesta.isExitoso() ? respuesta.getDatos() : Collections.emptyList();
    }

    public RespuestaBroker<Alumno> registrarAlumno(String nombre, String matricula) {
        return broker.registrarAlumno(nombre, matricula);
    }

    public RespuestaBroker<Void> eliminarAlumno(String id) {
        return broker.eliminarAlumno(id);
    }

    public RespuestaBroker<Void> agregarCalificacion(String alumnoId, String materia, double valor) {
        return broker.agregarCalificacion(alumnoId, materia, valor);
    }

    public RespuestaBroker<Reporte> generarReporte(String alumnoId) {
        return broker.generarReporte(alumnoId);
    }

    public double obtenerPromedio(String alumnoId) {
        RespuestaBroker<Double> respuesta = broker.calcularPromedio(alumnoId);
        return respuesta.isExitoso() ? respuesta.getDatos() : 0.0;
    }
}
