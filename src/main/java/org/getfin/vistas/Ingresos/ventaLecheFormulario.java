package org.getfin.vistas.Ingresos;

import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Transaccion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ventaLecheFormulario extends JFrame {

    private final ingresoVista parentVista;

    private JDateChooser campoFecha;
    private JTextField campoNombre, campoCantidad, campoPrecio, campoRetencion, campoSubTotal, campoTotal;
    private JTextArea areaDescripcion;

    private Transaccion transaccionActual = null;

    public ventaLecheFormulario(JFrame parent, ingresoVista vista) {
        super("Venta de Leche");
        this.parentVista = vista;
        initFormulario();
    }

    private void initFormulario() {
        setSize(500, 450);
        setLocationRelativeTo(null);
        setTitle("Formulario Venta de Lácteos");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(5,5,5,5);
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weightx = 1;
        gbcMain.weighty = 1;

        // PANEL DE DATOS
        JPanel panelDatos = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;

        c.gridx = 0; c.gridy = 0; panelDatos.add(new JLabel("Fecha:"), c);
        c.gridx = 1; panelDatos.add(campoFecha = new JDateChooser(), c);

        c.gridx = 0; c.gridy++; panelDatos.add(new JLabel("Nombre del Cliente:"), c);
        c.gridx = 1; panelDatos.add(campoNombre = new JTextField(), c);

        c.gridx = 0; c.gridy++; panelDatos.add(new JLabel("Cantidad:"), c);
        c.gridx = 1; panelDatos.add(campoCantidad = new JTextField(), c);

        c.gridx = 0; c.gridy++; panelDatos.add(new JLabel("Precio Unitario:"), c);
        c.gridx = 1; panelDatos.add(campoPrecio = new JTextField(), c);

        c.gridx = 0; c.gridy++; panelDatos.add(new JLabel("Retención:"), c);
        c.gridx = 1; panelDatos.add(campoRetencion = new JTextField("0"), c);

        c.gridx = 0; c.gridy++; panelDatos.add(new JLabel("Subtotal:"), c);
        c.gridx = 1; panelDatos.add(campoSubTotal = new JTextField(), c);
        campoSubTotal.setEditable(false);

        c.gridx = 0; c.gridy++; panelDatos.add(new JLabel("Total:"), c);
        c.gridx = 1; panelDatos.add(campoTotal = new JTextField(), c);
        campoTotal.setEditable(false);

        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos de la venta"));

        // PANEL DESCRIPCIÓN
        areaDescripcion = new JTextArea(5,20);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDescripcion);
        JPanel panelDesc = new JPanel(new BorderLayout());
        panelDesc.setBorder(BorderFactory.createTitledBorder("Descripción"));
        panelDesc.add(scroll, BorderLayout.CENTER);

        // BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);

        botonCancelar.addActionListener(e -> dispose());
        botonGuardar.addActionListener(e -> guardarTransaccion());

        // AÑADIR TODO AL MAIN PANEL
        gbcMain.gridx = 0; gbcMain.gridy = 0; mainPanel.add(panelDatos, gbcMain);
        gbcMain.gridy = 1; mainPanel.add(panelDesc, gbcMain);
        gbcMain.gridy = 2; gbcMain.weighty = 0; mainPanel.add(panelBotones, gbcMain);

        add(mainPanel);
        setVisible(true);

        // LISTENER PARA RECALCULAR SUBTOTAL Y TOTAL
        DocumentListener recalcularListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { recalcularTotales(); }
            public void removeUpdate(DocumentEvent e) { recalcularTotales(); }
            public void changedUpdate(DocumentEvent e) { recalcularTotales(); }
        };
        campoCantidad.getDocument().addDocumentListener(recalcularListener);
        campoPrecio.getDocument().addDocumentListener(recalcularListener);
        campoRetencion.getDocument().addDocumentListener(recalcularListener);
    }

    private void recalcularTotales() {
        try {
            double cantidad = campoCantidad.getText().isEmpty() ? 0 : Double.parseDouble(campoCantidad.getText());
            double precio = campoPrecio.getText().isEmpty() ? 0 : Double.parseDouble(campoPrecio.getText());
            double retencion = campoRetencion.getText().isEmpty() ? 0 : Double.parseDouble(campoRetencion.getText());

            double subTotal = cantidad * precio;
            double total = subTotal - retencion;

            campoSubTotal.setText(String.format("%.2f", subTotal));
            campoTotal.setText(String.format("%.2f", total));
        } catch(NumberFormatException e) {
            campoSubTotal.setText("");
            campoTotal.setText("");
        }
    }

    private void guardarTransaccion() {
        if(campoNombre.getText().isEmpty() || campoCantidad.getText().isEmpty() || campoPrecio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.");
            return;
        }

        try {
            String cliente = campoNombre.getText();
            BigDecimal cantidad = new BigDecimal(campoCantidad.getText().replace(",", "."));
            BigDecimal precio = new BigDecimal(campoPrecio.getText().replace(",", "."));
            BigDecimal retencion = campoRetencion.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(campoRetencion.getText().replace(",", "."));
            LocalDate fechaLocal = campoFecha.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            String descripcion = areaDescripcion.getText();

            if(transaccionActual == null){
                // NUEVA TRANSACCIÓN
                Transaccion trans = new Transaccion(cliente, cantidad, precio, retencion, fechaLocal, descripcion);
                trans.setCategoria("Lácteo");
                TransaccionController.getInstance().guardarTransaccion(trans);
            } else {
                // EDICIÓN
                transaccionActual.setNombreCliente(cliente);
                transaccionActual.setCantidad(cantidad);
                transaccionActual.setPrecioUnitario(precio);
                transaccionActual.setRetencion(retencion);
                transaccionActual.setFecha(fechaLocal);
                transaccionActual.setDescripcion(descripcion);
                TransaccionController.getInstance().editarTransaccion(transaccionActual);
            }

            if(parentVista != null) parentVista.recargarTabla();
            dispose();

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la transacción: " + e.getMessage());
        }
    }

    // Método para cargar transacción existente y mostrar en formulario
    public void cargarTransaccion(Transaccion t){
        if(t == null) return;
        this.transaccionActual = t;

        campoFecha.setDate(java.sql.Date.valueOf(t.getFecha()));
        campoNombre.setText(t.getNombreCliente());
        campoCantidad.setText(t.getCantidad().toString());
        campoPrecio.setText(t.getPrecioUnitario().toString());
        campoRetencion.setText(t.getRetencion().toString());
        campoSubTotal.setText(t.getCantidad().multiply(t.getPrecioUnitario()).toString());
        BigDecimal total = t.getCantidad().multiply(t.getPrecioUnitario()).subtract(t.getRetencion());
        campoTotal.setText(total.toString());
        areaDescripcion.setText(t.getDescripcion());
    }
}
