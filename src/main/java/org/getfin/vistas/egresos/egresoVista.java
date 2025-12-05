package org.getfin.vistas.egresos;

import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.TipoTransaccion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class egresoVista extends JPanel {

    private JTable tablaEgresos;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo, btnActualizar, btnEliminar, btnEditar;

    public egresoVista() {
        super(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

// ================= CABECERA =================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Egreso");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

// ================= TABLA =================
        tablaEgresos = new JTable();
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Fecha", "Tipo", "Referencia", "Detalle", "Cantidad", "Monto"},
                0
        );
        tablaEgresos.setModel(modeloTabla);
        tablaEgresos.setRowHeight(30);

        mainPanel.add(new JScrollPane(tablaEgresos), BorderLayout.CENTER);

// ================= BOTONES ABAJO =================
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        botonesPanel.setBackground(Color.WHITE);

        btnNuevo = crearBoton("Nuevo Egreso", new Color(46, 204, 113));
        btnEditar = crearBoton("Editar", new Color(241, 196, 15));
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));

        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        mainPanel.add(botonesPanel, BorderLayout.SOUTH);

// ================= AÑADIR AL PANEL PRINCIPAL =================
        add(mainPanel);

// ================= EVENTOS =================
        btnNuevo.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new EgresoSeleccionMenu(parent, this).setVisible(true);
        });

        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());

// CARGAR TABLA
        recargarTabla();


    }

    // ====================================================
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 40));
        return btn;
    }

    // ====================================================
    public void recargarTabla() {
        modeloTabla.setRowCount(0);

        List<Transaccion> lista = TransaccionController.getInstance()
                .getTransacciones()
                .stream()
                .filter(t -> t.getTipo() == TipoTransaccion.EGRESO)
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Transaccion t : lista) {
            String referencia = "";
            if (t.getProducto() != null) referencia = t.getProducto().getNombreProducto();
            else if (t.getCultivo() != null) referencia = t.getCultivo().getNombreCultivo();
            else if (t.getAnimal() != null) referencia = t.getAnimal().getNombre();
                // si quieres, puedes poner algo genérico como "Lácteo" si no hay objeto
            else referencia = t.getCategoria() != null ? t.getCategoria() : "-";

            modeloTabla.addRow(new Object[]{
                    t.getIdTransaccion(),
                    t.getFecha().format(formatter),
                    t.getTipo(),
                    referencia,  // aquí reemplazamos "Categoría" por la referencia
                    t.getDescripcion(),
                    t.getCantidad(),
                    t.getTotal()
            });
        }
    }

    // ====================================================
    //  EDITAR EGRESO (NUEVO)
    // ====================================================
    private void editar() {
        int fila = tablaEgresos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un egreso para editar.");
            return;
        }

        Long id = Long.parseLong(modeloTabla.getValueAt(fila, 0).toString());

        Transaccion t = TransaccionController.getInstance()
                .getTransacciones()
                .stream()
                .filter(x -> x.getIdTransaccion().equals(id))
                .findFirst()
                .orElse(null);

        if (t == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la transacción.");
            return;
        }

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if(t.getCultivo() != null){
            EgresoCultivoFormulario form = new EgresoCultivoFormulario(parent, this);
            form.cargarTransaccion(t); // <--- cargar datos
            form.setVisible(true);
        }
        else if (t.getAnimal() != null){
            EgresoAnimalFormulario form = new EgresoAnimalFormulario(parent, this);
            form.cargarTransaccion(t); // <--- cargar datos
            form.setVisible(true);
        }
        else if (t.getProducto() != null){
            EgresoProductoFormulario form = new EgresoProductoFormulario(parent, this);
            form.cargarTransaccion(t); // <--- cargar datos
            form.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(this, "Categoría desconocida: " + t.getCategoria());
        }

    }
    private void eliminar() {
        int fila = tablaEgresos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un egreso para eliminar.");
            return;
        }

        Long id = Long.parseLong(modeloTabla.getValueAt(fila, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar este egreso?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        Transaccion transaccionAEliminar = TransaccionController.getInstance()
                .getTransacciones()
                .stream()
                .filter(t -> t.getIdTransaccion().equals(id))
                .findFirst()
                .orElse(null);

        if (transaccionAEliminar == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la transacción.");
            return;
        }

        TransaccionController.getInstance().eliminarTransaccion(transaccionAEliminar);

        recargarTabla();
        JOptionPane.showMessageDialog(this, "Egreso eliminado correctamente.");
    }
}
