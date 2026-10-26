package org.control_escolar.vista;

import org.control_escolar.broker.RespuestaBroker;
import org.control_escolar.controlador.ControlEscolarControlador;
import org.control_escolar.modelo.Alumno;
import org.control_escolar.modelo.Calificacion;
import org.control_escolar.modelo.Reporte;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Esta es la capa de presentacion. Aqui hicimos todo el codigo de Swing (la libreria de UI)
 */
public class ControlEscolarVista {
    private final ControlEscolarControlador controlador;
    private JFrame ventana;
    private JTable tablaAlumnos;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;
    private List<Alumno> alumnosActuales;

    public ControlEscolarVista(ControlEscolarControlador controlador) {
        this.controlador = controlador;
    }

    public void iniciar() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        ventana = new JFrame("Control Escolar");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(620, 420);
        ventana.setMinimumSize(new Dimension(500, 350));
        ventana.setLocationRelativeTo(null);

        // Barra de acciones
        JPanel panelBarra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBarra.setBackground(new Color(245, 247, 250));
        panelBarra.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 224, 230)));

        JButton btnNuevo = crearBoton("+ Alumno", new Color(37, 99, 235), Color.WHITE);
        JButton btnNota = crearBoton("+ Nota", new Color(241, 245, 249), new Color(30, 41, 59));
        JButton btnReporte = crearBoton("Reporte", new Color(241, 245, 249), new Color(30, 41, 59));
        JButton btnEliminar = crearBoton("Eliminar", new Color(239, 68, 68), Color.WHITE);
        JButton btnRecargar = crearBoton("↻", new Color(241, 245, 249), new Color(71, 85, 105));

        panelBarra.add(btnNuevo);
        panelBarra.add(btnNota);
        panelBarra.add(btnReporte);
        panelBarra.add(new JSeparator(SwingConstants.VERTICAL));
        panelBarra.add(btnEliminar);
        panelBarra.add(btnRecargar);

        // Tabla de alumnos
        String[] columnas = {"Matrícula", "Nombre", "Materias", "Promedio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAlumnos = new JTable(modeloTabla);
        tablaAlumnos.setRowHeight(28);
        tablaAlumnos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAlumnos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaAlumnos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaAlumnos.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        tablaAlumnos.getColumnModel().getColumn(0).setCellRenderer(centro);
        tablaAlumnos.getColumnModel().getColumn(2).setCellRenderer(centro);
        tablaAlumnos.getColumnModel().getColumn(3).setCellRenderer(centro);

        tablaAlumnos.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaAlumnos.getColumnModel().getColumn(1).setPreferredWidth(260);
        tablaAlumnos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaAlumnos.getColumnModel().getColumn(3).setPreferredWidth(90);

        JScrollPane scrollTabla = new JScrollPane(tablaAlumnos);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        // ── Barra Inferior de Estado ──
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        panelEstado.setBackground(Color.WHITE);
        panelEstado.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));
        lblEstado = new JLabel("Listo");
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(100, 116, 139));
        panelEstado.add(lblEstado);

        // ── Disposición Principal ──
        ventana.getContentPane().setLayout(new BorderLayout());
        ventana.getContentPane().add(panelBarra, BorderLayout.NORTH);
        ventana.getContentPane().add(scrollTabla, BorderLayout.CENTER);
        ventana.getContentPane().add(panelEstado, BorderLayout.SOUTH);

        // ── Eventos ──
        btnNuevo.addActionListener(e -> mostrarDialogoNuevoAlumno());
        btnNota.addActionListener(e -> mostrarDialogoNuevaNota());
        btnReporte.addActionListener(e -> mostrarDialogoReporte());
        btnEliminar.addActionListener(e -> ejecutarEliminar());
        btnRecargar.addActionListener(e -> recargarDatos());

        ventana.setVisible(true);
        recargarDatos();
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(fondo);
        btn.setForeground(textoColor);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        return btn;
    }

    public void recargarDatos() {
        alumnosActuales = controlador.obtenerAlumnos();
        modeloTabla.setRowCount(0);

        for (Alumno a : alumnosActuales) {
            double promedio = controlador.obtenerPromedio(a.getId());
            modeloTabla.addRow(new Object[]{
                    a.getMatricula(),
                    a.getNombre(),
                    a.getCalificaciones().size(),
                    promedio
            });
        }
        lblEstado.setText(alumnosActuales.size() + " alumno(s)");
    }

    private Alumno getAlumnoSeleccionado() {
        int fila = tablaAlumnos.getSelectedRow();
        if (fila >= 0 && fila < alumnosActuales.size()) {
            return alumnosActuales.get(fila);
        }
        return null;
    }

    private void mostrarDialogoNuevoAlumno() {
        JDialog dlg = new JDialog(ventana, "Nuevo Alumno", true);
        dlg.setSize(300, 190);
        dlg.setLocationRelativeTo(ventana);
        dlg.setResizable(false);

        JPanel panel = new JPanel(new GridLayout(3, 1, 8, 8));
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JTextField txtMatricula = new JTextField();
        txtMatricula.setBorder(BorderFactory.createTitledBorder("Matrícula"));

        JTextField txtNombre = new JTextField();
        txtNombre.setBorder(BorderFactory.createTitledBorder("Nombre"));

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        JButton btnGuardar = crearBoton("Guardar", new Color(37, 99, 235), Color.WHITE);
        JButton btnCancelar = crearBoton("Cancelar", new Color(241, 245, 249), new Color(30, 41, 59));

        btnGuardar.addActionListener(e -> {
            RespuestaBroker<Alumno> resp = controlador.registrarAlumno(txtNombre.getText(), txtMatricula.getText());
            if (resp.isExitoso()) {
                dlg.dispose();
                recargarDatos();
                lblEstado.setText("Registrado: " + resp.getDatos().getMatricula());
            } else {
                JOptionPane.showMessageDialog(dlg, resp.getMensaje(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dlg.dispose());

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);

        panel.add(txtMatricula);
        panel.add(txtNombre);
        panel.add(pnlBotones);

        dlg.add(panel);
        dlg.setVisible(true);
    }

    private void mostrarDialogoNuevaNota() {
        Alumno seleccionado = getAlumnoSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(ventana, "Seleccione un alumno.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog(ventana, "Nota: " + seleccionado.getMatricula(), true);
        dlg.setSize(300, 190);
        dlg.setLocationRelativeTo(ventana);
        dlg.setResizable(false);

        JPanel panel = new JPanel(new GridLayout(3, 1, 8, 8));
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JTextField txtMateria = new JTextField();
        txtMateria.setBorder(BorderFactory.createTitledBorder("Materia"));

        JTextField txtValor = new JTextField();
        txtValor.setBorder(BorderFactory.createTitledBorder("Calificación (0 - 100)"));

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        JButton btnGuardar = crearBoton("Guardar", new Color(37, 99, 235), Color.WHITE);
        JButton btnCancelar = crearBoton("Cancelar", new Color(241, 245, 249), new Color(30, 41, 59));

        btnGuardar.addActionListener(e -> {
            try {
                double valor = Double.parseDouble(txtValor.getText().trim());
                RespuestaBroker<Void> resp = controlador.agregarCalificacion(seleccionado.getId(), txtMateria.getText(), valor);
                if (resp.isExitoso()) {
                    dlg.dispose();
                    recargarDatos();
                    lblEstado.setText("Calificación guardada.");
                } else {
                    JOptionPane.showMessageDialog(dlg, resp.getMensaje(), "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Ingrese un número válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dlg.dispose());

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnCancelar);

        panel.add(txtMateria);
        panel.add(txtValor);
        panel.add(pnlBotones);

        dlg.add(panel);
        dlg.setVisible(true);
    }

    private void mostrarDialogoReporte() {
        Alumno seleccionado = getAlumnoSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(ventana, "Seleccione un alumno.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        RespuestaBroker<Reporte> resp = controlador.generarReporte(seleccionado.getId());
        if (!resp.isExitoso()) {
            JOptionPane.showMessageDialog(ventana, resp.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Reporte rep = resp.getDatos();

        JDialog dlg = new JDialog(ventana, "Reporte", true);
        dlg.setSize(380, 360);
        dlg.setLocationRelativeTo(ventana);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JLabel lblInfo = new JLabel(rep.getAlumnoMatricula() + " - " + rep.getAlumnoNombre());
        lblInfo.setFont(new Font("SansSerif", Font.BOLD, 14));

        String[] cols = {"Materia", "Calificación"};
        DefaultTableModel modelCalifs = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Calificacion c : rep.getCalificaciones()) {
            modelCalifs.addRow(new Object[]{c.getMateria(), c.getValor()});
        }
        JTable tblCalifs = new JTable(modelCalifs);
        tblCalifs.setRowHeight(24);

        JPanel panelInferior = new JPanel(new GridLayout(3, 1, 4, 4));
        JLabel lblProm = new JLabel("Promedio: " + rep.getPromedio() + "  [" + rep.getEstadoAprobacion() + "]");
        lblProm.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblProm.setForeground("APROBADO".equals(rep.getEstadoAprobacion()) ? new Color(21, 128, 61) : new Color(185, 28, 28));

        JLabel lblFecha = new JLabel(rep.getFechaGeneracion());
        lblFecha.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblFecha.setForeground(Color.GRAY);

        JPanel pnlCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton btnCerrar = crearBoton("Cerrar", new Color(241, 245, 249), new Color(30, 41, 59));
        btnCerrar.addActionListener(e -> dlg.dispose());
        pnlCerrar.add(btnCerrar);

        panelInferior.add(lblProm);
        panelInferior.add(lblFecha);
        panelInferior.add(pnlCerrar);

        panel.add(lblInfo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblCalifs), BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        dlg.add(panel);
        dlg.setVisible(true);
    }

    private void ejecutarEliminar() {
        Alumno seleccionado = getAlumnoSeleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(ventana, "Seleccione un alumno.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventana,
                "¿Eliminar alumno " + seleccionado.getMatricula() + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            RespuestaBroker<Void> resp = controlador.eliminarAlumno(seleccionado.getId());
            if (resp.isExitoso()) {
                recargarDatos();
                lblEstado.setText("Alumno eliminado.");
            } else {
                JOptionPane.showMessageDialog(ventana, resp.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
