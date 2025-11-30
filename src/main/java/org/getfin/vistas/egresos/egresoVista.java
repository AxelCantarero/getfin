package org.getfin.vistas.egresos;

import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Transaccion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class egresoVista extends JPanel {

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Tipo", "Fecha", "Proveedor", "Descripción",
                    "Cantidad", "Precio U.", "Total", "Factura", "Referencia"}, 0) {

        @Override public boolean isCellEditable(int row, int column) { return false; }

        @Override public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Long.class : super.getColumnClass(columnIndex);
        }
    };

    private final JTable tabla = new JTable(modelo);

    private final JButton botonAgregar = crearBoton("Agregar", new Color(40, 167, 69));
    private final JButton botonEditar = crearBoton("Editar", new Color(33, 150, 243));
    private final JButton botonEliminar = crearBoton("Eliminar", new Color(244, 67, 54));

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public egresoVista() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ------------------ HEADER ------------------
        JPanel superiorPanel = new JPanel(new BorderLayout());
        superiorPanel.setBackground(Color.WHITE);
        superiorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Gestión de Egresos");
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));

        superiorPanel.add(label, BorderLayout.WEST);
        superiorPanel.add(botonAgregar, BorderLayout.EAST);
        add(superiorPanel, BorderLayout.NORTH);

        // ------------------ TABLA ------------------
        configurarTabla();
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ------------------ BOTONES ------------------
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        botonesPanel.setBackground(Color.WHITE);
        botonesPanel.add(botonEditar);
        botonesPanel.add(botonEliminar);

        add(botonesPanel, BorderLayout.SOUTH);

        // ------------------ EVENTOS ------------------
        asignarEventos();

        // ------------------ CARGAR DATOS ------------------
        recargarTabla();
    }

    private void configurarTabla() {
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(230, 230, 230));
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // --------------------------------------------------------------------
    // EVENTOS
    // --------------------------------------------------------------------
    private void asignarEventos() {

        // AGREGAR
        botonAgregar.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new EgresoSeleccionMenu(parent, this).setVisible(true);  // Creamos luego
        });

        // EDITAR
        botonEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para editar.");
                return;
            }

            Long id = (Long) modelo.getValueAt(fila, 0);
            Transaccion t = obtenerTransaccionPorId(id);
            if (t == null) return;

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            // Aquí se integrarán los formularios según tipo:
            switch (t.getTipo()) {
                case "Herramienta":
                    // new compraHerramientaFormulario(parent, this).cargar(t);
                    break;

                case "GastoCultivo":
                    // new gastoCultivoFormulario(parent, this).cargar(t);
                    break;

                case "GastoAnimal":
                    // new gastoAnimalFormulario(parent, this).cargar(t);
                    break;

                default:
                    // new otrosEgresosFormulario(parent, this).cargar(t);
                    break;
            }
        });

        // ELIMINAR
        botonEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.");
                return;
            }

            Long id = (Long) modelo.getValueAt(fila, 0);
            Transaccion t = obtenerTransaccionPorId(id);
            if (t == null) return;

            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que desea eliminar el egreso " + id + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);

            if (ok == JOptionPane.YES_OPTION) {
                TransaccionController.getInstance().eliminarTransaccion(t);
                recargarTabla();
            }
        });
    }

    // --------------------------------------------------------------------
    // CARGAR DATOS A LA TABLA
    // --------------------------------------------------------------------
    public void recargarTabla() {
        modelo.setRowCount(0);

        List<Transaccion> lista = TransaccionController.getInstance().getTransacciones();

        for (Transaccion t : lista) {
            if (!"EGRESO".equalsIgnoreCase(t.getTipoGeneral())) continue;

            String referencia = "";
            if (t.getHerramienta() != null) referencia = t.getHerramienta().getNombre();
            if (t.getCultivo() != null) referencia = t.getCultivo().getNombreCultivo();
            if (t.getAnimal() != null) referencia = t.getAnimal().getNombre();

            modelo.addRow(new Object[]{
                    t.getIdTransaccion(),
                    t.getTipo(),
                    t.getFecha() != null ? FMT.format(t.getFecha()) : "N/A",
                    t.getNombreCliente(),  // proveedor
                    t.getDescripcion(),
                    t.getCantidad(),
                    t.getPrecioUnitario(),
                    t.getTotal(),
                    t.getNumeroFactura(),
                    referencia
            });
        }
    }

    private Transaccion obtenerTransaccionPorId(Long id) {
        return TransaccionController.getInstance().getTransacciones()
                .stream()
                .filter(tr -> tr.getIdTransaccion().equals(id))
                .findFirst()
                .orElse(null);
    }

    // --------------------------------------------------------------------
    // UI Utilidades
    // --------------------------------------------------------------------
    private static JButton crearBoton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(130, 35));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}
