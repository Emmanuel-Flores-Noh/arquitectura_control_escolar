package org.control_escolar.persistencia;

/*
Usamos la libreria de json de google para facilitarnos la tarea de manejar Json
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.control_escolar.excepciones.ControlEscolarException;
import org.control_escolar.excepciones.GestorErrores;
import org.control_escolar.modelo.Alumno;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Capa de persistencia y acceso a datos.
 */
public class AlumnoDAOJson implements IAlumnoDAO {

    private static final String DIRECTORIO_DEFAULT = "datos";
    private static final String ARCHIVO_DEFAULT = "alumnos.json";

    private final Path rutaArchivo;
    private final Gson gson;
    private final Object bloqueo = new Object();

    public AlumnoDAOJson() {
        this(Paths.get(DIRECTORIO_DEFAULT, ARCHIVO_DEFAULT));
    }

    public AlumnoDAOJson(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        inicializarAlmacenamiento();
    }

    private void inicializarAlmacenamiento() {
        try {
            Path directorio = rutaArchivo.getParent();
            if (directorio != null && !Files.exists(directorio)) {
                Files.createDirectories(directorio);
            }
            if (!Files.exists(rutaArchivo)) {
                guardarLista(new ArrayList<>());
            }
        } catch (IOException e) {
            GestorErrores.registrarFallo("Error al inicializar archivo JSON de persistencia", e);
            throw new ControlEscolarException("ERR_PERSISTENCIA", "No se pudo inicializar el almacenamiento JSON: " + e.getMessage(), e);
        }
    }

    private List<Alumno> cargarLista() {
        synchronized (bloqueo) {
            File archivo = rutaArchivo.toFile();
            if (!archivo.exists() || archivo.length() == 0) {
                return new ArrayList<>();
            }
            try (Reader reader = new FileReader(archivo, StandardCharsets.UTF_8)) {
                Type tipoLista = new TypeToken<List<Alumno>>() {}.getType();
                List<Alumno> lista = gson.fromJson(reader, tipoLista);
                return lista != null ? new ArrayList<>(lista) : new ArrayList<>();
            } catch (IOException e) {
                GestorErrores.registrarFallo("Error al leer datos desde el archivo JSON: " + rutaArchivo, e);
                throw new ControlEscolarException("ERR_PERSISTENCIA", "Error al leer datos JSON: " + e.getMessage(), e);
            }
        }
    }

    private void guardarLista(List<Alumno> alumnos) {
        synchronized (bloqueo) {
            File archivo = rutaArchivo.toFile();
            try (Writer writer = new FileWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(alumnos, writer);
            } catch (IOException e) {
                GestorErrores.registrarFallo("Error al escribir datos en el archivo JSON: " + rutaArchivo, e);
                throw new ControlEscolarException("ERR_PERSISTENCIA", "Error al escribir datos JSON: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Alumno> obtenerTodos() {
        return cargarLista();
    }

    @Override
    public Optional<Alumno> obtenerPorId(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return cargarLista().stream()
                .filter(a -> id.equals(a.getId()))
                .findFirst();
    }

    @Override
    public Optional<Alumno> obtenerPorMatricula(String matricula) {
        if (matricula == null) {
            return Optional.empty();
        }
        return cargarLista().stream()
                .filter(a -> matricula.equalsIgnoreCase(a.getMatricula()))
                .findFirst();
    }

    @Override
    public void guardar(Alumno alumno) {
        synchronized (bloqueo) {
            List<Alumno> lista = cargarLista();
            lista.add(alumno);
            guardarLista(lista);
        }
    }

    @Override
    public void actualizar(Alumno alumno) {
        synchronized (bloqueo) {
            List<Alumno> lista = cargarLista();
            boolean encontrado = false;
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equals(alumno.getId())) {
                    lista.set(i, alumno);
                    encontrado = true;
                    break;
                }
            }
            if (encontrado) {
                guardarLista(lista);
            }
        }
    }

    @Override
    public void eliminar(String id) {
        synchronized (bloqueo) {
            List<Alumno> lista = cargarLista();
            boolean eliminado = lista.removeIf(a -> a.getId().equals(id));
            if (eliminado) {
                guardarLista(lista);
            }
        }
    }
}
