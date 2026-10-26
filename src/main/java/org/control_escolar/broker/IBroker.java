package org.control_escolar.broker;

import org.control_escolar.modelo.Alumno;
import org.control_escolar.modelo.Reporte;

import java.util.List;

/**
 * Contrato del patron broker
 */
public interface IBroker {

    RespuestaBroker<Alumno> registrarAlumno(String nombre, String matricula);

    RespuestaBroker<Void> eliminarAlumno(String id);

    RespuestaBroker<List<Alumno>> listarAlumnos();

    RespuestaBroker<Alumno> buscarPorMatricula(String matricula);

    RespuestaBroker<Void> agregarCalificacion(String alumnoId, String materia, double valor);

    RespuestaBroker<Double> calcularPromedio(String alumnoId);

    RespuestaBroker<Reporte> generarReporte(String alumnoId);
}
