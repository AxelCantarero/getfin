package org.getfin.vistas.Ingresos;
import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.Cultivo;
import org.getfin.modelos.Animal;
import org.getfin.vistas.Ingresos.cosechaFormulario;
import org.getfin.vistas.Ingresos.ventaAnimalFormulario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ingresoVista extends JPanel {

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Tipo", "Fecha", "Cliente", "Descripción",
                    "Cantidad", "Precio U.", "Total", "Factura", "Referencia"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
        @Override public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Long.class;
            return super.getColumnClass(columnIndex);
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final JButton botonAgregar = crearBoton("Agregar", new Color(40, 167, 69));
    private final JButton botonEditar = crearBoton("Editar", new Color(33, 150, 243));
    private final JButton botonEliminar = crearBoton("Eliminar", new Color(244, 67, 54));

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ingresoVista() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // HEADER
        JPanel superiorPanel = new JPanel(new BorderLayout());
        superiorPanel.setBackground(Color.WHITE);
        superiorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel("Gestión de Ingresos");
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        superiorPanel.add(label, BorderLayout.WEST);
        superiorPanel.add(botonAgregar, BorderLayout.EAST);
        add(superiorPanel, BorderLayout.NORTH);

        // TABLA
        configurarTabla();
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // BOTONES
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        botonesPanel.setBackground(Color.WHITE);
        botonesPanel.add(botonEditar);
        botonesPanel.add(botonEliminar);
        add(botonesPanel, BorderLayout.SOUTH);

        // EVENTOS
        asignarEventos();

        // CARGAR DATOS
        recargarTabla();
    }

    private void configurarTabla() {
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(230, 230, 230));
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void asignarEventos() {
        botonAgregar.addActionListener(e -> new IngresoSeleccionMenu((JFrame) SwingUtilities.getWindowAncestor(this), this).setVisible(true));

        botonEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para editar.");
                return;
            }

            Long id = (Long) modelo.getValueAt(fila, 0);
            Transaccion t = TransaccionController.getInstance().getTransacciones()
                    .stream()
                    .filter(tr -> tr.getIdTransaccion().equals(id))
                    .findFirst()
                    .orElse(null);

            if (t == null) return;

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            // === COSECHA ===
            if (t.getCultivo() != null) {
                cosechaFormulario formulario = new cosechaFormulario(parent, this);
                formulario.cargarTransaccion(t);
                formulario.setVisible(true);
            }
            // === VENTA ANIMAL ===
            else if (t.getAnimal() != null) {
                ventaAnimalFormulario formulario = new ventaAnimalFormulario(parent, this);
                formulario.cargarTransaccion(t);
                formulario.setVisible(true);
            }
            // === VENTA DE LECHE (LÁCTEO) ===
            else if ("Lácteo".equals(t.getCategoria())) {
                ventaLecheFormulario formulario = new ventaLecheFormulario(parent, this);
                formulario.cargarTransaccion(t); // Aquí sí carga los datos
                formulario.setVisible(true);
            }
        });


        botonEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar.");
                return;
            }

            Long id = (Long) modelo.getValueAt(fila, 0);
            Transaccion t = TransaccionController.getInstance().getTransacciones()
                    .stream()
                    .filter(tr -> tr.getIdTransaccion().equals(id))
                    .findFirst()
                    .orElse(null);
            if (t == null) return;

            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que desea eliminar el ingreso " + id + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);

            if (ok == JOptionPane.YES_OPTION) {
                TransaccionController.getInstance().eliminarTransaccion(t);
                recargarTabla();
            }
        });
    }

    public void recargarTabla() {
        modelo.setRowCount(0);
        List<Transaccion> lista = TransaccionController.getInstance().getTransacciones();
        for (Transaccion t : lista) {
            String referencia = "";
            if (t.getProducto() != null) referencia = t.getProducto().getNombreProducto();
            if (t.getCultivo() != null) referencia = t.getCultivo().getNombreCultivo();
            if (t.getAnimal() != null) referencia = t.getAnimal().getNombre();
            else if ("Lácteo".equals(t.getCategoria())) referencia = "Lácteo";
            modelo.addRow(new Object[]{
                    t.getIdTransaccion(),
                    t.getTipo(),
                    t.getFecha() != null ? FMT.format(t.getFecha()) : "N/A",
                    t.getNombreCliente(),
                    t.getDescripcion(),
                    t.getCantidad(),
                    t.getPrecioUnitario(),
                    t.getTotal(),
                    t.getNumeroFactura(),
                    referencia
            });
        }
    }

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
