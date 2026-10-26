package org.control_escolar;

import org.control_escolar.broker.ControlEscolarBroker;
import org.control_escolar.broker.IBroker;
import org.control_escolar.controlador.ControlEscolarControlador;
import org.control_escolar.negocio.AlumnoServicio;
import org.control_escolar.negocio.IAlumnoServicio;
import org.control_escolar.persistencia.AlumnoDAOJson;
import org.control_escolar.persistencia.IAlumnoDAO;
import org.control_escolar.vista.ControlEscolarVista;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Capa de acceso a datos
            IAlumnoDAO alumnoDAO = new AlumnoDAOJson();

            // Capa de logica de negocio
            IAlumnoServicio alumnoServicio = new AlumnoServicio(alumnoDAO);

            // Broker
            IBroker broker = new ControlEscolarBroker(alumnoServicio);

            // Controlador
            ControlEscolarControlador controlador = new ControlEscolarControlador(broker);

            // Vista
            ControlEscolarVista vista = new ControlEscolarVista(controlador);
            vista.iniciar();
        });
    }
}
