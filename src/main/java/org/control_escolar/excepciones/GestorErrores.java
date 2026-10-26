package org.control_escolar.excepciones;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Modulo centralizado de manejo de excepciones
 */
public final class GestorErrores {

    private static final Logger LOGGER = Logger.getLogger("ControlEscolar");

    private GestorErrores() {
    }

    public static void registrarAdvertencia(String mensaje) {
        LOGGER.log(Level.WARNING, mensaje);
    }

    public static void registrarFallo(String mensaje, Throwable causa) {
        LOGGER.log(Level.SEVERE, mensaje, causa);
    }

    public static String extraerMensaje(Throwable t) {
        if (t instanceof ControlEscolarException) {
            return t.getMessage();
        }
        if (t.getCause() != null && t.getCause() instanceof ControlEscolarException) {
            return t.getCause().getMessage();
        }
        return "Ocurrió un error inesperado en el sistema: " + t.getMessage();
    }
}
