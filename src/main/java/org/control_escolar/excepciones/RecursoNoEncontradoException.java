package org.control_escolar.excepciones;

/**
 * Excepción lanzada cuando no se localiza un recurso solicitado por ejemplo un alumno.
 */
public class RecursoNoEncontradoException extends ControlEscolarException {

    public RecursoNoEncontradoException(String mensaje) {
        super("ERR_NO_ENCONTRADO", mensaje);
    }
}
