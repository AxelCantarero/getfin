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
    private JButton btnGuardar;

    private final egresoVista vista;

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
        txtCaducidad = new JTextField("2025-01-01");

        txtIVA = new JTextField();
        txtIVA.setEditable(false);

        txtRetencion = new JTextField();
        txtRetencion.setEditable(false);

        txtSubtotal = new JTextField();
        txtSubtotal.setEditable(false);

        txtTotal = new JTextField();
        txtTotal.setEditable(false);

        comboCategoria = new JComboBox<>(CategoriaProducto.values());
        btnGuardar = new JButton("Guardar");

        add(new JLabel("Nombre:")); add(txtNombre);
        add(new JLabel("Descripción:")); add(txtDescripcion);
        add(new JLabel("Unidad:")); add(txtUnidad);
        add(new JLabel("Categoría:")); add(comboCategoria);
        add(new JLabel("Fecha Caducidad (YYYY-MM-DD):")); add(txtCaducidad);
        add(new JLabel("Stock Inicial:")); add(txtStock);
        add(new JLabel("Monto Pagado:")); add(txtMonto);

        add(new JLabel("IVA (15%):")); add(txtIVA);
        add(new JLabel("Retención (2%):")); add(txtRetencion);
        add(new JLabel("Subtotal:")); add(txtSubtotal);
        add(new JLabel("Total:")); add(txtTotal);

        add(btnGuardar);

        // Listener integrado SIN clase externa
        txtMonto.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { recalcularTotales(); }
            @Override public void removeUpdate(DocumentEvent e) { recalcularTotales(); }
            @Override public void changedUpdate(DocumentEvent e) { recalcularTotales(); }
        });

        btnGuardar.addActionListener(e -> guardar());
    }

    private void recalcularTotales() {
        try {
            double monto = Double.parseDouble(txtMonto.getText());

            double iva = monto * 0.15;
            double ret = monto * 0.02;
            double subtotal = monto + iva + ret;

            txtIVA.setText(String.format("%.2f", iva));
            txtRetencion.setText(String.format("%.2f", ret));
            txtSubtotal.setText(String.format("%.2f", subtotal));
            txtTotal.setText(String.format("%.2f", subtotal));

        } catch (Exception ex) {
            txtIVA.setText("");
            txtRetencion.setText("");
            txtSubtotal.setText("");
            txtTotal.setText("");
        }
    }

    private void guardar() {
        try {
            String stockText = txtStock.getText().replace(",", ".");
            String montoText = txtMonto.getText().replace(",", ".");

            BigDecimal stock = new BigDecimal(stockText);
            BigDecimal monto = new BigDecimal(montoText);
            // Crear producto
            Producto p = new Producto(
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    txtUnidad.getText(),
                    (CategoriaProducto) comboCategoria.getSelectedItem(),
                    LocalDate.parse(txtCaducidad.getText()),
                    new BigDecimal(txtStock.getText()),
                    new BigDecimal(txtMonto.getText()),
                    EstadoProducto.DISPONIBLE
            );

            ProductoController.getInstance().guardarProducto(p);
            BigDecimal precioUnitario = stock.compareTo(BigDecimal.ZERO) > 0 ? monto.divide(stock, 2, BigDecimal.ROUND_HALF_UP) : monto;

            // Crear transacción (con tu constructor exacto)
            Transaccion t = new Transaccion(
                    TipoTransaccion.EGRESO,
                    "Compra de " + txtNombre.getText(),
                    LocalDate.now(),
                    stock,       // cantidad
                    monto,       // precio unitario
                    new BigDecimal(txtIVA.getText().replace(",", ".")),
                    new BigDecimal(txtRetencion.getText().replace(",", ".")),   // retención
                    new BigDecimal(txtTotal.getText().replace(",", ".")),       // total
                    "",                                        // numeroFactura
                    p                                          // producto
            );

            TransaccionController.getInstance().guardarTransaccion(t);

            vista.recargarTabla();
            JOptionPane.showMessageDialog(this, "Producto comprado correctamente.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
