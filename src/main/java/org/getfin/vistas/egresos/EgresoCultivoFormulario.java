package org.getfin.vistas.egresos;

import org.getfin.controlador.CultivoController;
import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Cultivo;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.CategoriaCultivo;
import org.getfin.modelos.enums.EstadoCultivo;
import org.getfin.modelos.enums.TipoTransaccion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EgresoCultivoFormulario extends JDialog {

    private JTextField txtNombre, txtFecha, txtStock, txtMonto,
            txtIva, txtRetencion, txtTotal;

    private JComboBox<CategoriaCultivo> comboCategoria;
    private JComboBox<EstadoCultivo> comboEstado;
    private JButton btnGuardar;

    private final egresoVista vista;

    public EgresoCultivoFormulario(JFrame parent, egresoVista vista) {
        super(parent, "Compra de Cultivo", true);
        this.vista = vista;

        setSize(480, 600);
        setLayout(new GridLayout(11, 2, 10, 10));
        setLocationRelativeTo(parent);

        txtNombre = new JTextField();
        txtFecha = new JTextField(LocalDate.now().toString());
        txtStock = new JTextField();
        txtMonto = new JTextField();

        txtIva = new JTextField("0.00");
        txtRetencion = new JTextField("0.00");
        txtTotal = new JTextField("0.00");
        txtTotal.setEditable(false);

        comboCategoria = new JComboBox<>(CategoriaCultivo.values());
        comboEstado = new JComboBox<>(EstadoCultivo.values());

        btnGuardar = new JButton("Guardar");

        add(new JLabel("Nombre Cultivo:")); add(txtNombre);
        add(new JLabel("Fecha Compra:")); add(txtFecha);
        add(new JLabel("Categoría:")); add(comboCategoria);
        add(new JLabel("Estado:")); add(comboEstado);
        add(new JLabel("Cantidad (Stock):")); add(txtStock);
        add(new JLabel("Precio Unitario:")); add(txtMonto);
        add(new JLabel("IVA:")); add(txtIva);
        add(new JLabel("Retención:")); add(txtRetencion);
        add(new JLabel("TOTAL:")); add(txtTotal);
        add(btnGuardar);

        agregarListenersCalculo();
        btnGuardar.addActionListener(e -> guardar());
    }

    // ============================
    // CÁLCULOS AUTOMÁTICOS
    // ============================
    private void agregarListenersCalculo() {
        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calcularTotal(); }
            public void removeUpdate(DocumentEvent e) { calcularTotal(); }
            public void changedUpdate(DocumentEvent e) { calcularTotal(); }
        };

        txtMonto.getDocument().addDocumentListener(listener);
        txtStock.getDocument().addDocumentListener(listener);
        txtIva.getDocument().addDocumentListener(listener);
        txtRetencion.getDocument().addDocumentListener(listener);
    }

    private void calcularTotal() {
        try {
            BigDecimal cantidad = new BigDecimal(txtStock.getText());
            BigDecimal precioUnit = new BigDecimal(txtMonto.getText());
            BigDecimal iva = new BigDecimal(txtIva.getText());
            BigDecimal ret = new BigDecimal(txtRetencion.getText());

            BigDecimal subtotal = cantidad.multiply(precioUnit);
            BigDecimal total = subtotal.add(iva).subtract(ret);

            txtTotal.setText(total.toString());
        } catch (Exception e) {
            txtTotal.setText("0.00");
        }
    }

    // ============================
    // GUARDAR CULTIVO + TRANSACCIÓN
    // ============================
    private void guardar() {
        try {
            // 1️⃣ Crear cultivo
            Cultivo cultivo = new Cultivo(
                    txtNombre.getText(),
                    LocalDate.parse(txtFecha.getText()),
                    (CategoriaCultivo) comboCategoria.getSelectedItem(),
                    (EstadoCultivo) comboEstado.getSelectedItem(),
                    new BigDecimal(txtStock.getText()),
                    new BigDecimal(txtMonto.getText())
            );

            CultivoController.getInstance().guardarCultivo(cultivo);

            // 2️⃣ Crear transacción
            BigDecimal cantidad = new BigDecimal(txtStock.getText());
            BigDecimal precioUnit = new BigDecimal(txtMonto.getText());
            BigDecimal iva = new BigDecimal(txtIva.getText());
            BigDecimal ret = new BigDecimal(txtRetencion.getText());
            BigDecimal total = new BigDecimal(txtTotal.getText());

            Transaccion trans = new Transaccion(
                    TipoTransaccion.EGRESO,
                    "Compra de Cultivo",
                    "",            // nombreCliente (vacío)
                    cultivo,
                    "",            // número factura
                    total,
                    ret,
                    cantidad,
                    LocalDate.now(),
                    precioUnit,
                    iva
            );

            TransaccionController.getInstance().guardarTransaccion(trans);

            vista.recargarTabla();
            JOptionPane.showMessageDialog(this, "Cultivo agregado correctamente.");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
