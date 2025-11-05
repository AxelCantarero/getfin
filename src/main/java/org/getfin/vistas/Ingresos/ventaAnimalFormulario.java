package org.getfin.vistas.Ingresos;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;

public class ventaAnimalFormulario extends JFrame {

    public ventaAnimalFormulario() {
        initFormulario();
    }

    private void initFormulario() {
        setTitle("Venta de Animales - Formulario de Registro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600); // ← Tamaño fijo de ventana
        setLocationRelativeTo(null); // ← Centrar en pantalla

        // === PANEL PRINCIPAL ===
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // === PANEL IZQUIERDO ===
        JPanel panelIzquierdo = new JPanel(new GridLayout(7, 2, 10, 10));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Datos de la venta"));
        panelIzquierdo.setBackground(Color.WHITE);

        panelIzquierdo.add(new JLabel("Fecha:"));
        JDateChooser campoFecha = new JDateChooser();
        panelIzquierdo.add(campoFecha);

        panelIzquierdo.add(new JLabel("Tipo de Animal:"));
        JComboBox campoTipoAnimal = new JComboBox(new String[]{"Vacas, caballos, perro"});
        panelIzquierdo.add(campoTipoAnimal);

        panelIzquierdo.add(new JLabel("Cantidad:"));
        JTextField campoCantidad = new JTextField();
        panelIzquierdo.add(campoCantidad);

        panelIzquierdo.add(new JLabel("Peso total (kg):"));
        JTextField campoPeso = new JTextField();
        panelIzquierdo.add(campoPeso);

        panelIzquierdo.add(new JLabel("Precio por kg:"));
        JTextField campoPrecioKg = new JTextField();
        panelIzquierdo.add(campoPrecioKg);

        panelIzquierdo.add(new JLabel("Cliente:"));
        JTextField campoCliente = new JTextField();
        panelIzquierdo.add(campoCliente);

        panelIzquierdo.add(new JLabel("Número de Factura:"));
        JTextField campoFactura = new JTextField();
        panelIzquierdo.add(campoFactura);

        // === PANEL DERECHO ===
        JPanel panelDerecho = new JPanel(new GridLayout(4, 2, 10, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Totales y otros datos"));
        panelDerecho.setBackground(Color.WHITE);

        panelDerecho.add(new JLabel("Retención:"));
        JTextField campoRetencion = new JTextField();
        panelDerecho.add(campoRetencion);

        panelDerecho.add(new JLabel("IVA (%):"));
        JTextField campoIVA = new JTextField();
        panelDerecho.add(campoIVA);

        panelDerecho.add(new JLabel("Total:"));
        JTextField campoTotal = new JTextField();
        panelDerecho.add(campoTotal);


        // === PANEL DESCRIPCIÓN ===
        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        JTextArea areaDescripcion = new JTextArea(5, 20);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDescripcion);
        panelDescripcion.add(scroll, BorderLayout.CENTER);

        JPanel contenedorDerecho = new JPanel(new BorderLayout(10, 10));
        contenedorDerecho.add(panelDerecho, BorderLayout.NORTH);
        contenedorDerecho.add(panelDescripcion, BorderLayout.CENTER);

        // === PANEL BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        JButton botonVolver = new JButton("Volver al menú");
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);
        panelBotones.add(botonVolver);

        botonVolver.addActionListener(e -> {
            new IngresoSeleccionMenu();
            dispose();
        });

        // === AGREGAR TODO AL MAIN PANEL ===
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.5; gbc.weighty = 1;
        mainPanel.add(panelIzquierdo, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.5; gbc.weighty = 1;
        mainPanel.add(contenedorDerecho, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1; gbc.weighty = 0;
        mainPanel.add(panelBotones, gbc);

        add(mainPanel);
        getContentPane().setBackground(new Color(240, 240, 240));
        setVisible(true);
    }
}
