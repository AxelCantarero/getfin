package org.getfin.vistas.visitas;

import com.github.lgooddatepicker.components.DatePicker;
import org.getfin.modelos.Visita;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FormularioVisita extends JDialog {

    // Campos del formulario
    private final JTextField txtNombreResponsable = crearCampo();
    private final JTextField txtCarrera = crearCampo();
    private final JTextField txtMujeres = crearCampo();
    private final JTextField txtHombres = crearCampo();
    private final DatePicker chooserFecha = new DatePicker();

    private final VisitaVista vista;
    private final Visita visitaEditar;

    public FormularioVisita(JFrame parent, VisitaVista vista, Visita visitaEditar) {
        super(parent, "", true); // El título se establece dinámicamente
        this.vista = vista;
        this.visitaEditar = visitaEditar;

        setSize(520, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // --- ESTRUCTURA DEL FORMULARIO MODIFICADA ---

        // 1. PANEL SUPERIOR: Solo el Título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        String tituloDialogo = visitaEditar == null ? "Agregar Visita" : "Editar Visita";
        JLabel lblTitulo = new JLabel(tituloDialogo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(41, 82, 128));

        // El título ahora está centrado
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // 2. PANEL CENTRAL: Campos del formulario (sin cambios)
        JPanel camposPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        camposPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        camposPanel.setBackground(Color.WHITE);

        camposPanel.add(new JLabel("Nombre Responsable:"));
        camposPanel.add(txtNombreResponsable);
        camposPanel.add(new JLabel("Carrera:"));
        camposPanel.add(txtCarrera);
        camposPanel.add(new JLabel("Mujeres:"));
        camposPanel.add(txtMujeres);
        camposPanel.add(new JLabel("Hombres:"));
        camposPanel.add(txtHombres);
        camposPanel.add(new JLabel("Fecha de Visita:"));
        camposPanel.add(chooserFecha);

        add(camposPanel, BorderLayout.CENTER);

        // 3. PANEL INFERIOR: Botones Guardar y Cancelar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 30, 15, 30));

        // CAMBIO: Ambos botones se crean aquí
        JButton btnGuardar = crearBoton("Guardar", new Color(50, 150, 50));
        JButton btnCancelar = crearBoton("Cancelar", Color.GRAY);

        panelInferior.add(btnCancelar);
        panelInferior.add(btnGuardar);
        add(panelInferior, BorderLayout.SOUTH);

        // --- CARGA DE DATOS (EDICIÓN) ---
        if (visitaEditar != null) {
            txtNombreResponsable.setText(visitaEditar.getNombreResponsable());
            txtCarrera.setText(visitaEditar.getCarrera());
            txtMujeres.setText(String.valueOf(visitaEditar.getMujeres()));
            txtHombres.setText(String.valueOf(visitaEditar.getHombres()));
            chooserFecha.setDate(visitaEditar.getFecha());
        }

        // --- ACTION LISTENERS ---
        btnCancelar.addActionListener(e -> dispose());

        // CAMBIO: El listener se asigna al botón creado en el panel inferior
        btnGuardar.addActionListener(e -> {
            if (txtNombreResponsable.getText().trim().isEmpty() || txtCarrera.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa los campos obligatorios (Nombre Responsable y Carrera).");
                return;
            }

            int mujeres = 0, hombres = 0;
            try {
                mujeres = Integer.parseInt(txtMujeres.getText().trim());
                hombres = Integer.parseInt(txtHombres.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Las cantidades deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fechaVisita = chooserFecha.getDate();
            if (fechaVisita == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha para la visita.");
                return;
            }

            Visita v = visitaEditar != null ? visitaEditar : new Visita();
            v.setNombreResponsable(txtNombreResponsable.getText().trim());
            v.setCarrera(txtCarrera.getText().trim());
            v.setMujeres(mujeres);
            v.setHombres(hombres);
            v.setFecha(fechaVisita);
            v.setTotal(mujeres + hombres);

            vista.guardarOActualizar(v);
            dispose();
        });
    }

    // --- MÉTODOS AUXILIARES ---

    // CAMBIO: El método crearBotonPrincipal ya no es necesario y se puede eliminar.
    // private JButton crearBotonPrincipal(...) { ... }

    private static JTextField crearCampo() {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBackground(new Color(220, 220, 220));
        t.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        return t;
    }

    private static JButton crearBoton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(100, 35));
        return b;
    }
}