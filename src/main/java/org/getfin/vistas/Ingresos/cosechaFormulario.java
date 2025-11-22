package org.getfin.vistas.Ingresos;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import org.getfin.controlador.CultivoController;
import org.getfin.modelos.Cultivo;
import org.getfin.modelos.enums.CategoriaCultivo;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class cosechaFormulario extends JFrame {

    private List<Cultivo> listaCultivos;

    public cosechaFormulario() {
        initFormulario();
    }

    private void initFormulario() {
        setTitle("Cosecha - Formulario de Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // === PANEL IZQUIERDO ===
        JPanel panelIzquierdo = new JPanel(new GridLayout(7, 2, 10, 10));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Datos principales"));

        panelIzquierdo.add(new JLabel("Fecha:"));
        JDateChooser campoFecha = new JDateChooser();
        panelIzquierdo.add(campoFecha);

        panelIzquierdo.add(new JLabel("Categoría:"));
        JComboBox<CategoriaCultivo> campoTipoProducto = new JComboBox<>(CategoriaCultivo.values());
        panelIzquierdo.add(campoTipoProducto);

        // 🔹 Obtener cultivos desde el controlador
        CultivoController cultivoController = new CultivoController();
        listaCultivos = cultivoController.getCultivo();

        panelIzquierdo.add(new JLabel("Cultivos:"));
        JComboBox<Cultivo> campoProducto = new JComboBox<>();
        panelIzquierdo.add(campoProducto);

        // 🔹 Personalizar cómo se muestran los cultivos en el combo
        campoProducto.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cultivo cultivo) {
                    setText(cultivo.getNombreCultivo());
                }
                return this;
            }
        });

        // 🔹 Evento: filtrar cultivos por categoría seleccionada
        campoTipoProducto.addActionListener(e -> {
            CategoriaCultivo categoriaSeleccionada = (CategoriaCultivo) campoTipoProducto.getSelectedItem();
            campoProducto.removeAllItems();
            for (Cultivo c : listaCultivos) {
                if (c.getCategoria() == categoriaSeleccionada) {
                    campoProducto.addItem(c);
                }
            }
        });

        // 🔹 Cargar los cultivos de la primera categoría por defecto
        if (campoTipoProducto.getItemCount() > 0) {
            campoTipoProducto.setSelectedIndex(0);
            CategoriaCultivo categoriaInicial = (CategoriaCultivo) campoTipoProducto.getSelectedItem();
            for (Cultivo c : listaCultivos) {
                if (c.getCategoria() == categoriaInicial) {
                    campoProducto.addItem(c);
                }
            }
        }

        // === Campos restantes ===
        panelIzquierdo.add(new JLabel("Cliente:"));
        JTextField campoCliente = new JTextField();
        panelIzquierdo.add(campoCliente);

        panelIzquierdo.add(new JLabel("Cantidad:"));
        JTextField campoCantidad = new JTextField();
        panelIzquierdo.add(campoCantidad);

        panelIzquierdo.add(new JLabel("Precio Unitario:"));
        JTextField campoPrecio = new JTextField();
        panelIzquierdo.add(campoPrecio);

        panelIzquierdo.add(new JLabel("IVA (%):"));
        JTextField campoIVA = new JTextField();
        panelIzquierdo.add(campoIVA);

        // === PANEL DERECHO ===
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Totales y descripción"));

        JPanel panelTotales = new JPanel(new GridLayout(4, 2, 10, 10));

        panelTotales.add(new JLabel("Retención:"));
        JTextField campoRetencion = new JTextField();
        panelTotales.add(campoRetencion);

        panelTotales.add(new JLabel("Sub Total:"));
        JTextField campoSubTotal = new JTextField();
        panelTotales.add(campoSubTotal);

        panelTotales.add(new JLabel("Total:"));
        JTextField campoTotal = new JTextField();
        campoTotal.setEditable(false);
        panelTotales.add(campoTotal);

        panelTotales.add(new JLabel("Número de Factura:"));
        JTextField campoFactura = new JTextField();
        panelTotales.add(campoFactura);

        JTextArea areaDescripcion = new JTextArea(5, 20);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDescripcion);

        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setBorder(BorderFactory.createTitledBorder("Descripción"));
        panelDescripcion.add(scroll, BorderLayout.CENTER);

        panelDerecho.add(panelTotales, BorderLayout.NORTH);
        panelDerecho.add(panelDescripcion, BorderLayout.CENTER);

        // === PANEL BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 1;
        mainPanel.add(panelIzquierdo, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 1;
        mainPanel.add(panelDerecho, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 0;
        mainPanel.add(panelBotones, gbc);

        // === EVENTOS ===
        botonCancelar.addActionListener(actionEvent -> dispose());

        KeyAdapter recalcularListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTotal(campoSubTotal, campoIVA, campoRetencion, campoTotal);
            }
        };
        campoSubTotal.addKeyListener(recalcularListener);
        campoIVA.addKeyListener(recalcularListener);
        campoRetencion.addKeyListener(recalcularListener);

        botonGuardar.addActionListener(actionEvent -> {
            Date fecha = campoFecha.getDate();
            CategoriaCultivo categoria = (CategoriaCultivo) campoTipoProducto.getSelectedItem();
            Cultivo cultivoSeleccionado = (Cultivo) campoProducto.getSelectedItem();
            String cliente = campoCliente.getText().trim();
            String cantidadStr = campoCantidad.getText().trim();
            String precioStr = campoPrecio.getText().trim();
            String ivaStr = campoIVA.getText().trim();

            if (fecha == null || categoria == null || cultivoSeleccionado == null ||
                    cliente.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty() || ivaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.",
                        "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            BigDecimal cantidad;
            try {
                cantidad = new BigDecimal(cantidadStr);
                if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.",
                            "Valor inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número válido.",
                        "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (cultivoSeleccionado.getStock() == null) {
                JOptionPane.showMessageDialog(this, "El cultivo seleccionado no tiene stock registrado.",
                        "Error de stock", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (cantidad.compareTo(cultivoSeleccionado.getStock()) > 0) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad ingresada (" + cantidad + ") supera el stock disponible (" +
                                cultivoSeleccionado.getStock() + ").",
                        "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "✔ Registro válido.\nPuede continuar con el guardado en base de datos.",
                    "Validación exitosa", JOptionPane.INFORMATION_MESSAGE);
        });

        add(mainPanel);
        setVisible(true);
    }

    private void calcularTotal(JTextField campoSubTotal, JTextField campoIVA, JTextField campoRetencion, JTextField campoTotal) {
        try {
            BigDecimal subTotal = new BigDecimal(campoSubTotal.getText());
            BigDecimal iva = new BigDecimal(campoIVA.getText());
            BigDecimal retencion = new BigDecimal(campoRetencion.getText());

            BigDecimal ivaCalculado = subTotal.multiply(iva).divide(new BigDecimal(100));
            BigDecimal total = subTotal.subtract(ivaCalculado).subtract(retencion);

            campoTotal.setText(total.toString());
        } catch (NumberFormatException ex) {
            campoTotal.setText("");
        }
    }
}
