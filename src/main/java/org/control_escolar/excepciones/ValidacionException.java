package org.control_escolar.excepciones;

/**
 * Excepción lanzada cuando los datos de entrada no cumplen con las reglas de negocio.
 */
public class ValidacionException extends ControlEscolarException {
    public ValidacionException(String mensaje) {
        super("ERR_VALIDACION", mensaje);
    }
}
