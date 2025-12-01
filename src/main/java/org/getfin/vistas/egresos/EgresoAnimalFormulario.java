package org.getfin.vistas.egresos;

import org.getfin.controlador.AnimalController;
import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Animal;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.EstadoAnimal;
import org.getfin.modelos.enums.TipoAnimal;
import org.getfin.modelos.enums.TipoTransaccion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EgresoAnimalFormulario extends JDialog {

    private JTextField txtNombre, txtIdentificador, txtPeso, txtCantidad, txtPrecioUnitario, txtIVA, txtRetencion, txtTotal;
    private JComboBox<TipoAnimal> comboTipo;
    private JComboBox<EstadoAnimal> comboEstado;

    private final egresoVista vista;

    public EgresoAnimalFormulario(JFrame parent, egresoVista vista) {
        super(parent, "Compra de Animal", true);
        this.vista = vista;

        setSize(500, 600);
        setLayout(new GridLayout(12, 2, 10, 10));
        setLocationRelativeTo(parent);

        // CAMPOS
        txtNombre = new JTextField();
        txtIdentificador = new JTextField();
        txtPeso = new JTextField();
        txtCantidad = new JTextField();
        txtPrecioUnitario = new JTextField();

        txtIVA = new JTextField();
        txtRetencion = new JTextField();
        txtTotal = new JTextField();
        txtTotal.setEditable(false);

        comboTipo = new JComboBox<>(TipoAnimal.values());
        comboEstado = new JComboBox<>(EstadoAnimal.values());

        JButton btnGuardar = new JButton("Guardar");

        // UI
        add(new JLabel("Nombre:")); add(txtNombre);
        add(new JLabel("Identificador:")); add(txtIdentificador);
        add(new JLabel("Tipo Animal:")); add(comboTipo);
        add(new JLabel("Estado:")); add(comboEstado);
        add(new JLabel("Cantidad:")); add(txtCantidad);
        add(new JLabel("Peso Promedio:")); add(txtPeso);
        add(new JLabel("Precio Unitario:")); add(txtPrecioUnitario);

        add(new JLabel("IVA (15% por defecto):")); add(txtIVA);
        add(new JLabel("Retención (2% por defecto):")); add(txtRetencion);
        add(new JLabel("TOTAL:")); add(txtTotal);

        add(btnGuardar);

        // LISTENER PARA CALCULAR AUTOMATICO
        DocumentListener calcListener = new SimpleDocumentListener(() -> calcularTotal());
        txtPrecioUnitario.getDocument().addDocumentListener(calcListener);
        txtCantidad.getDocument().addDocumentListener(calcListener);
        txtIVA.getDocument().addDocumentListener(calcListener);
        txtRetencion.getDocument().addDocumentListener(calcListener);

        // VALORES POR DEFECTO
        txtIVA.setText("0.15");
        txtRetencion.setText("0.02");

        btnGuardar.addActionListener(e -> guardar());
    }

    private void calcularTotal() {
        try {
            BigDecimal precio = new BigDecimal(txtPrecioUnitario.getText());
            BigDecimal cant = new BigDecimal(txtCantidad.getText());

            BigDecimal subtotal = precio.multiply(cant);

            BigDecimal iva = subtotal.multiply(new BigDecimal(txtIVA.getText()));
            BigDecimal ret = subtotal.multiply(new BigDecimal(txtRetencion.getText()));

            BigDecimal total = subtotal.add(iva).add(ret);

            txtTotal.setText(total.toPlainString());
        } catch (Exception ex) {
            txtTotal.setText("");
        }
    }

    private void guardar() {

        if (animalActual != null) {
            try {
                if (transaccionActual != null) {
                    BigDecimal precioUnit = new BigDecimal(txtPrecioUnitario.getText());
                    BigDecimal cantidad = new BigDecimal(txtCantidad.getText());
                    BigDecimal iva = new BigDecimal(txtIVA.getText());
                    BigDecimal ret = new BigDecimal(txtRetencion.getText());
                    BigDecimal total = new BigDecimal(txtTotal.getText());

                    transaccionActual.setCantidad(cantidad);
                    transaccionActual.setPrecioUnitario(precioUnit);
                    transaccionActual.setIva(iva);
                    transaccionActual.setRetencion(ret);
                    transaccionActual.setTotal(total);
                    TransaccionController.getInstance().editarTransaccion(transaccionActual);
                }

                // actualizar el animal existente
                animalActual.setNombre(txtNombre.getText());
                animalActual.setIdentificador(txtIdentificador.getText());
                animalActual.setTipo((TipoAnimal) comboTipo.getSelectedItem());
                animalActual.setEstado((EstadoAnimal) comboEstado.getSelectedItem());
                animalActual.setCantidad(Integer.parseInt(txtCantidad.getText()));
                animalActual.setPesoPromedio(new BigDecimal(txtPeso.getText()));

                AnimalController.getInstance().editarAnimal(animalActual);
                vista.recargarTabla();
                JOptionPane.showMessageDialog(this, "Animal actualizado correctamente.");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
            }
        } else {
            try {

                // 1️⃣ Crear ANIMAL
                Animal a = new Animal(
                        txtNombre.getText(),
                        txtIdentificador.getText(),
                        (TipoAnimal) comboTipo.getSelectedItem(),
                        Integer.parseInt(txtCantidad.getText()),
                        new BigDecimal(txtPeso.getText()),
                        "Compra de animal",
                        (EstadoAnimal) comboEstado.getSelectedItem()
                );

                AnimalController.getInstance().guardarAnimal(a);

                // 2️⃣ Valores cálculo
                BigDecimal precioUnit = new BigDecimal(txtPrecioUnitario.getText());
                BigDecimal cantidad = new BigDecimal(txtCantidad.getText());
                BigDecimal iva = new BigDecimal(txtIVA.getText());
                BigDecimal ret = new BigDecimal(txtRetencion.getText());
                BigDecimal total = new BigDecimal(txtTotal.getText());

                // 3️⃣ Crear TRANSACCIÓN usando TU CONSTRUCTOR
                Transaccion t = new Transaccion(
                        a,
                        "",                       // número factura
                        total,
                        ret,
                        precioUnit,
                        iva,
                        cantidad,
                        LocalDate.now(),
                        "",                       // nombre cliente
                        TipoTransaccion.EGRESO,
                        "Compra de animal"
                );

                TransaccionController.getInstance().guardarTransaccion(t);

                vista.recargarTabla();
                JOptionPane.showMessageDialog(this, "Animal registrado y transacción creada.");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    // SimpleDocumentListener integrado en la clase
    private static class SimpleDocumentListener implements DocumentListener {
        private final Runnable onChange;

        public SimpleDocumentListener(Runnable onChange) {
            this.onChange = onChange;
        }

        @Override public void insertUpdate(DocumentEvent e) { onChange.run(); }
        @Override public void removeUpdate(DocumentEvent e) { onChange.run(); }
        @Override public void changedUpdate(DocumentEvent e) { onChange.run(); }
    }
    private Animal animalActual = null;
    private Transaccion transaccionActual = null;
    public void cargarTransaccion(Transaccion t) {
        if (t == null || t.getAnimal() == null) return;

        animalActual = t.getAnimal();
        transaccionActual = t;  // guardamos la transacción

        // rellenar campos del animal
        txtNombre.setText(animalActual.getNombre());
        txtIdentificador.setText(animalActual.getIdentificador());
        comboTipo.setSelectedItem(animalActual.getTipo());
        comboEstado.setSelectedItem(animalActual.getEstado());
        txtCantidad.setText(String.valueOf(animalActual.getCantidad()));
        txtPeso.setText(animalActual.getPesoPromedio().toPlainString());

        // rellenar campos de la transacción
        txtPrecioUnitario.setText(t.getTotal().toPlainString());
        txtIVA.setText(t.getIva().toPlainString());
        txtRetencion.setText(t.getRetencion().toPlainString());
        txtTotal.setText(t.getTotal().toPlainString());
    }


}
