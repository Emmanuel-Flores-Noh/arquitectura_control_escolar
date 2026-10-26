package org.control_escolar.broker;

import org.control_escolar.excepciones.ControlEscolarException;
import org.control_escolar.excepciones.GestorErrores;
import org.control_escolar.modelo.Alumno;
import org.control_escolar.modelo.Reporte;
import org.control_escolar.negocio.IAlumnoServicio;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del Patrón Broker
 * Recibe las peticiones del controlador y las procesa dependiendo de los servicios disponibles
 * En este caso, unicamente existe el servicio alumno.
 */
public class ControlEscolarBroker implements IBroker {

    private final IAlumnoServicio alumnoServicio;

    public ControlEscolarBroker(IAlumnoServicio alumnoServicio) {
        if (alumnoServicio == null) {
            throw new IllegalArgumentException("El servicio de alumnos no puede ser nulo.");
        }
        this.alumnoServicio = alumnoServicio;
    }

    @Override
    public RespuestaBroker<Alumno> registrarAlumno(String nombre, String matricula) {
        try {
            Alumno alumno = alumnoServicio.registrarAlumno(nombre, matricula);
            return RespuestaBroker.exito("Alumno registrado correctamente.", alumno);
        } catch (ControlEscolarException e) {
            GestorErrores.registrarAdvertencia("Fallo en registro de alumno: " + e.getMessage());
            return RespuestaBroker.error(e.getCodigo(), e.getMessage());
        } catch (Exception e) {
            GestorErrores.registrarFallo("Error inesperado en broker al registrar alumno", e);
            return RespuestaBroker.error(GestorErrores.extraerMensaje(e));
        }
    }

    @Override
    public RespuestaBroker<Void> eliminarAlumno(String id) {
        try {
            alumnoServicio.eliminarAlumno(id);
            return RespuestaBroker.exito("Alumno eliminado correctamente.", null);
        } catch (ControlEscolarException e) {
            GestorErrores.registrarAdvertencia("Fallo al eliminar alumno: " + e.getMessage());
            return RespuestaBroker.error(e.getCodigo(), e.getMessage());
        } catch (Exception e) {
            GestorErrores.registrarFallo("Error inesperado en broker al eliminar alumno", e);
            return RespuestaBroker.error(GestorErrores.extraerMensaje(e));
        }
    }

    @Override
    public RespuestaBroker<List<Alumno>> listarAlumnos() {
        try {
            List<Alumno> lista = alumnoServicio.listarAlumnos();
            return RespuestaBroker.exito(lista);
        } catch (Exception e) {
            GestorErrores.registrarFallo("Error inesperado en broker al listar alumnos", e);
            return RespuestaBroker.error("No se pudieron cargar los alumnos: " + e.getMessage());
        }
    }

    @Override
    public RespuestaBroker<Alumno> buscarPorMatricula(String matricula) {
        try {
            Optional<Alumno> opt = alumnoServicio.buscarPorMatricula(matricula);
            return opt.map(RespuestaBroker::exito)
                    .orElseGet(() -> RespuestaBroker.error("ERR_NO_ENCONTRADO", "No se encontró ningún alumno con la matrícula especificada."));
        } catch (ControlEscolarException e) {
            return RespuestaBroker.error(e.getCodigo(), e.getMessage());
        } catch (Exception e) {
            GestorErrores.registrarFallo("Error inesperado en broker al buscar por matrícula", e);
            return RespuestaBroker.error(GestorErrores.extraerMensaje(e));
        }
    }

    @Override
    public RespuestaBroker<Void> agregarCalificacion(String alumnoId, String materia, double valor) {
        try {
            alumnoServicio.agregarCalificacion(alumnoId, materia, valor);
            return RespuestaBroker.exito("Calificación registrada con éxito.", null);
        } catch (ControlEscolarException e) {
            GestorErrores.registrarAdvertencia("Fallo al agregar calificación: " + e.getMessage());
            return RespuestaBroker.error(e.getCodigo(), e.getMessage());
        } catch (Exception e) {
            GestorErrores.registrarFallo("Error inesperado en broker al agregar calificación", e);
            return RespuestaBroker.error(GestorErrores.extraerMensaje(e));
        }
    }

    @Override
    public RespuestaBroker<Double> calcularPromedio(String alumnoId) {
        try {
            double promedio = alumnoServicio.calcularPromedio(alumnoId);
            return RespuestaBroker.exito(promedio);
        } catch (ControlEscolarException e) {
            return RespuestaBroker.error(e.getCodigo(), e.getMessage());
        } catch (Exception e) {
            GestorErrores.registrarFallo("Error inesperado en broker al calcular promedio", e);
            return RespuestaBroker.error(GestorErrores.extraerMensaje(e));
        }
    }

    @Override
    public RespuestaBroker<Reporte> generarReporte(String alumnoId) {
        try {
            Reporte reporte = alumnoServicio.generarReporte(alumnoId);
            return RespuestaBroker.exito(reporte);
        } catch (ControlEscolarException e) {
            GestorErrores.registrarAdvertencia("Fallo al generar reporte: " + e.getMessage());
            return RespuestaBroker.error(e.getCodigo(), e.getMessage());
        } catch (Exception e) {
            GestorErrores.registrarFallo("Error inesperado en broker al generar reporte", e);
            return RespuestaBroker.error(GestorErrores.extraerMensaje(e));
        }
    }
}
