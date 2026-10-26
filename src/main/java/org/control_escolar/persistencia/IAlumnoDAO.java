package org.control_escolar.persistencia;

import org.control_escolar.modelo.Alumno;

import java.util.List;
import java.util.Optional;

/**
 * Contrato del DAO del alumno.
 * Establece las acciones que debe de realizar la capa de acceso de datos.
 */
public interface IAlumnoDAO {

    List<Alumno> obtenerTodos();

    Optional<Alumno> obtenerPorId(String id);

    Optional<Alumno> obtenerPorMatricula(String matricula);

    void guardar(Alumno alumno);

    void actualizar(Alumno alumno);

    void eliminar(String id);
}
