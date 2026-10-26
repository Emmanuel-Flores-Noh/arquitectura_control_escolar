package org.control_escolar.broker;

/**
 * Mensaje que devuelve el broker.
 */
public class RespuestaBroker<T> {

    private final boolean exitoso;
    private final String mensaje;
    private final T datos;
    private final String codigoError;

    private RespuestaBroker(boolean exitoso, String mensaje, T datos, String codigoError) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.datos = datos;
        this.codigoError = codigoError;
    }

    public static <T> RespuestaBroker<T> exito(T datos) {
        return new RespuestaBroker<>(true, "Operación exitosa", datos, null);
    }

    public static <T> RespuestaBroker<T> exito(String mensaje, T datos) {
        return new RespuestaBroker<>(true, mensaje, datos, null);
    }

    public static <T> RespuestaBroker<T> error(String mensaje) {
        return new RespuestaBroker<>(false, mensaje, null, "ERR_GENERAL");
    }

    public static <T> RespuestaBroker<T> error(String codigoError, String mensaje) {
        return new RespuestaBroker<>(false, mensaje, null, codigoError);
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public T getDatos() {
        return datos;
    }

    public String getCodigoError() {
        return codigoError;
    }
}
