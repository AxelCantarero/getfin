package org.getfin.vistas.egresos;

import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.TipoTransaccion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class egresoVista extends JPanel {

    private JTable tablaEgresos;
    private DefaultTableModel modeloTabla;
    private JButton btnNuevo, btnActualizar, btnEliminar, btnEditar;

    public egresoVista() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ============ BOTONES SUPERIORES ============
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.setBackground(Color.WHITE);

        btnNuevo = crearBoton("Nuevo Egreso", new Color(46, 204, 113));
        btnEditar = crearBoton("Editar", new Color(241, 196, 15));
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.NORTH);

        // ============ TABLA ============
        tablaEgresos = new JTable();
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Fecha", "Tipo", "Categoría", "Detalle", "Cantidad", "Monto"},
                0
        );
        tablaEgresos.setModel(modeloTabla);
        tablaEgresos.setRowHeight(30);

        add(new JScrollPane(tablaEgresos), BorderLayout.CENTER);

        // ============ EVENTOS ============

        // NUEVO
        btnNuevo.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new EgresoSeleccionMenu(parent, this).setVisible(true);
        });

        // EDITAR (AQUÍ ES DONDE SE AGREGA LA LÓGICA QUE PEDISTE)
        btnEditar.addActionListener(e -> editar());


        // ELIMINAR
        btnEliminar.addActionListener(e -> eliminar());

        // CARGAR
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
            modeloTabla.addRow(new Object[]{
                    t.getIdTransaccion(),
                    t.getFecha().format(formatter),
                    t.getTipo(),
                    t.getCategoria() != null ? t.getCategoria() : "-",
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

        // ============================
        // ABRIR FORMULARIO CORRECTO
        // ============================

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

    // ====================================================
    //  ELIMINAR (YA FUNCIONA)
    // ====================================================
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
