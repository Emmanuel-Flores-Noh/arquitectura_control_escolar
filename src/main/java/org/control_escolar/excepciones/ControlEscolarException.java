package org.control_escolar.excepciones;

/**
 * Excepción base para el sistema de Control Escolar
 */
public class ControlEscolarException extends RuntimeException {

    private final String codigo;

    public ControlEscolarException(String mensaje) {
        super(mensaje);
        this.codigo = "ERR_SISTEMA";
    }

    public ControlEscolarException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public ControlEscolarException(String codigo, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
