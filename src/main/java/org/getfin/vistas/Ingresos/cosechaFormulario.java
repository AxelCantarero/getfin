package org.getfin.vistas.Ingresos;

import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;

public class cosechaFormulario extends JFrame {

    public cosechaFormulario() {
        initFormulario();
    }

    private void initFormulario() {
        setTitle("Cosecha - Formulario de Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        // PANEL PRINCIPAL (usa GridBagLayout)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH; // Permite expandir todo el contenido
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // === PANEL IZQUIERDO ===
        JPanel panelIzquierdo = new JPanel(new GridLayout(7, 2, 10, 10));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Datos principales"));

        panelIzquierdo.add(new JLabel("Fecha:"));
        JDateChooser campoFecha = new JDateChooser();
        panelIzquierdo.add(campoFecha);

        panelIzquierdo.add(new JLabel("Categoria:"));
        JTextField campoTipoProducto = new JTextField();
        panelIzquierdo.add(campoTipoProducto);

        panelIzquierdo.add(new JLabel("Cultivos:"));
        JTextField campoProducto = new JTextField();
        panelIzquierdo.add(campoProducto);

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

        // Panel superior (totales)
        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 10, 10));
        panelTotales.add(new JLabel("Retención:"));
        JTextField campoRetencion = new JTextField();
        panelTotales.add(campoRetencion);

        panelTotales.add(new JLabel("Total:"));
        JTextField campoTotal = new JTextField();
        panelTotales.add(campoTotal);

        panelTotales.add(new JLabel("Número de Factura:"));
        JTextField campoFactura = new JTextField();
        panelTotales.add(campoFactura);

        // Panel inferior (descripción)
        JTextArea areaDescripcion = new JTextArea(5, 20);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDescripcion);

        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setBorder(BorderFactory.createTitledBorder("Descripción"));
        panelDescripcion.add(scroll, BorderLayout.CENTER);

        // Agregar los subpaneles al panel derecho
        panelDerecho.add(panelTotales, BorderLayout.NORTH);
        panelDerecho.add(panelDescripcion, BorderLayout.CENTER);

        // === PANEL BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);

        // === AGREGAR TODO AL MAIN PANEL ===
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 1;
        mainPanel.add(panelIzquierdo, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 1;
        mainPanel.add(panelDerecho, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 0;
        mainPanel.add(panelBotones, gbc);

        botonCancelar.addActionListener(actionEvent -> {
            new IngresoSeleccionMenu();
            dispose();
        });


        add(mainPanel);
        setVisible(true);
    }

}
