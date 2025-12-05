package org.getfin.vistas.egresos;

import org.getfin.controlador.ProductoController;
import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Producto;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.CategoriaProducto;
import org.getfin.modelos.enums.EstadoProducto;
import org.getfin.modelos.enums.TipoTransaccion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EgresoProductoFormulario extends JDialog {

    private JTextField txtNombre, txtDescripcion, txtUnidad, txtStock, txtMonto, txtCaducidad;
    private JTextField txtIVA, txtRetencion, txtSubtotal, txtTotal;
    private JComboBox<CategoriaProducto> comboCategoria;

    private final egresoVista vista;
    private Transaccion transaccionActual = null; // <- Para edición

    public EgresoProductoFormulario(JFrame parent, egresoVista vista) {
        super(parent, "Compra de Producto", true);
        this.vista = vista;

        setSize(450, 600);
        setLayout(new GridLayout(14, 2, 10, 10));
        setLocationRelativeTo(parent);

        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtUnidad = new JTextField();
        txtStock = new JTextField();
        txtMonto = new JTextField();
        txtCaducidad = new JTextField(LocalDate.now().toString());

        txtIVA = new JTextField("0.00");
        txtRetencion = new JTextField("0.00");
        txtSubtotal = new JTextField();
        txtTotal = new JTextField();

        txtSubtotal.setEditable(false);
        txtTotal.setEditable(false);

        comboCategoria = new JComboBox<>(CategoriaProducto.values());
        JButton btnGuardar = new JButton("Guardar");

        // UI
        add(new JLabel("Nombre:")); add(txtNombre);
        add(new JLabel("Descripción:")); add(txtDescripcion);
        add(new JLabel("Unidad:")); add(txtUnidad);
        add(new JLabel("Categoría:")); add(comboCategoria);
        add(new JLabel("Fecha Caducidad:")); add(txtCaducidad);
        add(new JLabel("Cantidad (Stock):")); add(txtStock);
        add(new JLabel("Monto Pagado:")); add(txtMonto);

        add(new JLabel("IVA:")); add(txtIVA);
        add(new JLabel("Retención:")); add(txtRetencion);
        add(new JLabel("Subtotal:")); add(txtSubtotal);
        add(new JLabel("Total:")); add(txtTotal);

        add(btnGuardar);

        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { recalcular(); }
            public void removeUpdate(DocumentEvent e) { recalcular(); }
            public void changedUpdate(DocumentEvent e) { recalcular(); }
        };

        txtMonto.getDocument().addDocumentListener(listener);
        txtIVA.getDocument().addDocumentListener(listener);
        txtRetencion.getDocument().addDocumentListener(listener);

        btnGuardar.addActionListener(e -> guardar());
    }

    private void recalcular() {
        try {
            BigDecimal monto = new BigDecimal(txtMonto.getText());
            BigDecimal iva = new BigDecimal(txtIVA.getText());
            BigDecimal ret = new BigDecimal(txtRetencion.getText());

            BigDecimal subtotal = monto.add(iva).add(ret);

            txtSubtotal.setText(subtotal.toString());
            txtTotal.setText(subtotal.toString());

        } catch (Exception e) {
            txtSubtotal.setText("");
            txtTotal.setText("");
        }
    }

    public void cargarTransaccion(Transaccion t){
        this.transaccionActual = t;

        Producto p = t.getProducto();

        txtNombre.setText(p.getNombreProducto());
        txtDescripcion.setText(p.getDescripcion());
        txtUnidad.setText(p.getUnidadMedida());
        comboCategoria.setSelectedItem(p.getCategoria());
        txtCaducidad.setText(p.getFechaCaducidad().toString());
        txtStock.setText(t.getCantidad().toString());
        txtMonto.setText(t.getTotal().toString());
        txtIVA.setText(t.getIva().toString());
        txtRetencion.setText(t.getRetencion().toString());
        txtTotal.setText(t.getTotal().toString());
    }

    private void guardar() {
        try {
            BigDecimal stock = new BigDecimal(txtStock.getText());
            BigDecimal monto = new BigDecimal(txtMonto.getText());
            BigDecimal iva = new BigDecimal(txtIVA.getText());
            BigDecimal retencion = new BigDecimal(txtRetencion.getText());
            BigDecimal total = new BigDecimal(txtTotal.getText());
            BigDecimal subtotal = new BigDecimal(txtSubtotal.getText());

            if(transaccionActual == null){
                // Crear producto nuevo
                Producto p = new Producto(
                        txtNombre.getText(),
                        txtDescripcion.getText(),
                        txtUnidad.getText(),
                        (CategoriaProducto) comboCategoria.getSelectedItem(),
                        LocalDate.parse(txtCaducidad.getText()),
                        stock,
                        monto,
                        EstadoProducto.DISPONIBLE
                );

                ProductoController.getInstance().guardarProducto(p);

                Transaccion t = new Transaccion(
                        TipoTransaccion.EGRESO,
                        "Compra de " + txtNombre.getText(),
                        LocalDate.now(),
                        stock,
                        monto,
                        iva,
                        retencion,
                        total,
                        "",
                        p
                );

                TransaccionController.getInstance().guardarTransaccion(t);
                JOptionPane.showMessageDialog(this, "Producto agregado correctamente.");
            } else {
                // Actualizar producto existente
                Producto p = transaccionActual.getProducto();
                p.setNombreProducto(txtNombre.getText());
                p.setDescripcion(txtDescripcion.getText());
                p.setUnidadMedida(txtUnidad.getText());
                p.setCategoria((CategoriaProducto) comboCategoria.getSelectedItem());
                p.setFechaCaducidad(LocalDate.parse(txtCaducidad.getText()));
                p.setStock(stock);

                ProductoController.getInstance().editarProducto(p);

                transaccionActual.setCantidad(stock);

                transaccionActual.setIva(iva);
                transaccionActual.setRetencion(retencion);

                transaccionActual.setTotal(total);

                TransaccionController.getInstance().editarTransaccion(transaccionActual);
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");
            }

            vista.recargarTabla();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
