package org.control_escolar.modelo;

/**
 * Entidad que representa la calificación obtenida por un alumno en una materia.
 */
public class Calificacion {

    private String materia;
    private double valor;

    public Calificacion() {
    }

    public Calificacion(String materia, double valor) {
        this.materia = materia;
        this.valor = valor;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return materia + ": " + valor;
    }
}
