package org.control_escolar.negocio;

import org.control_escolar.excepciones.GestorErrores;
import org.control_escolar.excepciones.RecursoNoEncontradoException;
import org.control_escolar.excepciones.ValidacionException;
import org.control_escolar.modelo.Alumno;
import org.control_escolar.modelo.Calificacion;
import org.control_escolar.modelo.Reporte;
import org.control_escolar.persistencia.IAlumnoDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementacion de la capa de logica de negocio por medio de la interfaz previamente declarada
 */
public class AlumnoServicio implements IAlumnoServicio {
    private static final double CALIFICACION_MINIMA = 0.0;
    private static final double CALIFICACION_MAXIMA = 100.0;
    private static final double NOTA_APROBATORIA = 70.0;

    private final IAlumnoDAO alumnoDAO;

    /**
     * Se implementaron las funciones para manipular
     * la base de datos en esta capa para mantener la estructura por capas
     * Asi el broker solo accede a la capa de logica de negocio y no al dao directamente
     */
    public AlumnoServicio(IAlumnoDAO alumnoDAO) {
        if (alumnoDAO == null) {
            throw new IllegalArgumentException("El DAO de alumnos no puede ser nulo.");
        }
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public Alumno registrarAlumno(String nombre, String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new ValidacionException("La matrícula es obligatoria.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre del alumno es obligatorio.");
        }

        String matriculaLimpia = matricula.trim().toUpperCase();
        String nombreLimpio = nombre.trim();

        Optional<Alumno> existente = alumnoDAO.obtenerPorMatricula(matriculaLimpia);
        if (existente.isPresent()) {
            throw new ValidacionException("Ya existe un alumno con la matrícula: " + matriculaLimpia);
        }

        String id = UUID.randomUUID().toString();
        Alumno nuevo = new Alumno(id, matriculaLimpia, nombreLimpio);
        alumnoDAO.guardar(nuevo);
        return nuevo;
    }

    @Override
    public void eliminarAlumno(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidacionException("El identificador del alumno es obligatorio.");
        }
        Optional<Alumno> opt = alumnoDAO.obtenerPorId(id);
        if (opt.isEmpty()) {
            throw new RecursoNoEncontradoException("No se encontró ningún alumno con ID: " + id);
        }
        alumnoDAO.eliminar(id);
    }

    @Override
    public List<Alumno> listarAlumnos() {
        return alumnoDAO.obtenerTodos();
    }

    @Override
    public Optional<Alumno> buscarPorMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new ValidacionException("La matrícula de búsqueda no puede estar vacía.");
        }
        return alumnoDAO.obtenerPorMatricula(matricula.trim());
    }

    @Override
    public Optional<Alumno> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return alumnoDAO.obtenerPorId(id);
    }

    @Override
    public void agregarCalificacion(String alumnoId, String materia, double valor) {
        if (alumnoId == null || alumnoId.trim().isEmpty()) {
            throw new ValidacionException("El identificador de alumno es obligatorio.");
        }
        if (materia == null || materia.trim().isEmpty()) {
            throw new ValidacionException("El nombre de la materia es obligatorio.");
        }
        if (valor < CALIFICACION_MINIMA || valor > CALIFICACION_MAXIMA) {
            throw new ValidacionException("La calificación debe ubicarse en el rango de "
                    + CALIFICACION_MINIMA + " a " + CALIFICACION_MAXIMA + ".");
        }

        Alumno alumno = alumnoDAO.obtenerPorId(alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado con ID: " + alumnoId));

        alumno.agregarCalificacion(new Calificacion(materia.trim(), valor));
        alumnoDAO.actualizar(alumno);
    }

    @Override
    public double calcularPromedio(String alumnoId) {
        Alumno alumno = alumnoDAO.obtenerPorId(alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado con ID: " + alumnoId));

        List<Calificacion> calificaciones = alumno.getCalificaciones();
        if (calificaciones == null || calificaciones.isEmpty()) {
            return 0.0;
        }

        double suma = calificaciones.stream()
                .mapToDouble(Calificacion::getValor)
                .sum();

        double promedio = suma / calificaciones.size();
        return BigDecimal.valueOf(promedio)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public Reporte generarReporte(String alumnoId) {
        Alumno alumno = alumnoDAO.obtenerPorId(alumnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno no encontrado con ID: " + alumnoId));

        double promedio = calcularPromedio(alumnoId);
        String estado = (promedio >= NOTA_APROBATORIA) ? "APROBADO" : "REPROBADO";

        return new Reporte(
                alumno.getId(),
                alumno.getNombre(),
                alumno.getMatricula(),
                alumno.getCalificaciones(),
                promedio,
                estado
        );
    }
}
