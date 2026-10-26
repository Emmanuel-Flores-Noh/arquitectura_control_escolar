package org.control_escolar.negocio;

import org.control_escolar.modelo.Alumno;
import org.control_escolar.modelo.Reporte;

import java.util.List;
import java.util.Optional;

/**
Contrato de la capa de logica de negocio
 Establece lo que el servicio de alumnos debe de realizar
 */
public interface IAlumnoServicio {

    Alumno registrarAlumno(String nombre, String matricula);

    void eliminarAlumno(String id);

    List<Alumno> listarAlumnos();

    Optional<Alumno> buscarPorMatricula(String matricula);

    Optional<Alumno> buscarPorId(String id);

    void agregarCalificacion(String alumnoId, String materia, double valor);

    double calcularPromedio(String alumnoId);

    Reporte generarReporte(String alumnoId);
}
