package org.getfin.vistas.Ingresos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class ingresoVista extends JPanel implements ActionListener {

    private JButton botonAgregar;

    public ingresoVista() {

        ventanaIngreso();
    }

    private void ventanaIngreso() {
        setLayout(new BorderLayout(10, 10));

        // ---------- 1️⃣ ENCABEZADO ----------
        JPanel superiorPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Gestión de Ingresos");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        superiorPanel.add(label, BorderLayout.WEST);

        botonAgregar = new JButton("Agregar Ingreso");
        botonAgregar.addActionListener(this);
        superiorPanel.add(botonAgregar, BorderLayout.EAST);
        add(superiorPanel, BorderLayout.NORTH);

        // ---------- 2️⃣ PANEL CENTRAL (formulario + tabla + resumen) ----------
        JPanel centroPanel = new JPanel(new BorderLayout(10, 10));
        add(centroPanel, BorderLayout.CENTER);

        // ---------- 3️⃣ FORMULARIO ----------
        JPanel formularioPanel = new JPanel(new GridLayout(2, 3, 10, 5));

        formularioPanel.add(new JLabel("Tipo:"));
        formularioPanel.add(new JLabel("Fecha:"));
        formularioPanel.add(new JLabel("Cliente:"));

        JTextField campoTipo = new JTextField();
        JTextField campoFecha = new JTextField();
        JTextField campoCliente = new JTextField();

        formularioPanel.add(campoTipo);
        formularioPanel.add(campoFecha);
        formularioPanel.add(campoCliente);

        centroPanel.add(formularioPanel, BorderLayout.NORTH);

        // ---------- 4️⃣ TABLA ----------
        String[] columnas = {"ID", "Tipo", "Fecha", "Cliente", "Monto", "Descripción", "Estado"};
        Object[][] datos = {
                {1, "Venta", "2025-10-07", "Juan Pérez", 250.0, "Venta de maíz", "Completado"},
                {2, "Donación", "2025-10-07", "Fundación ABC", 100.0, "Apoyo social", "Pendiente"},
                {3, "Venta", "2025-10-07", "Carlos Ruiz", 150.0, "Venta de frijol", "Completado"}
        };

        JTable tabla = new JTable(datos, columnas);
        JScrollPane scroll = new JScrollPane(tabla);

        centroPanel.add(scroll, BorderLayout.CENTER);

        // ---------- 5️⃣ PANEL DE RESUMEN ----------
        JPanel resumenPanel = new JPanel();
        resumenPanel.setLayout(new BoxLayout(resumenPanel, BoxLayout.Y_AXIS));
        resumenPanel.setBorder(BorderFactory.createTitledBorder("Resumen del mes"));
        resumenPanel.setPreferredSize(new Dimension(200, 0)); // ancho fijo

        // Datos de ejemplo
        String mes = "Octubre 2025";
        double total = 500.0;
        Map<String, Double> totalesPorTipo = new LinkedHashMap<>();
        totalesPorTipo.put("Ventas", 400.0);
        totalesPorTipo.put("Donaciones", 100.0);

        // Componentes del resumen
        resumenPanel.add(new JLabel("Mes: " + mes));
        resumenPanel.add(Box.createVerticalStrut(5));
        resumenPanel.add(new JLabel("Total general: $" + total));
        resumenPanel.add(Box.createVerticalStrut(10));
        resumenPanel.add(new JLabel("Totales por tipo:"));
        for (Map.Entry<String, Double> entry : totalesPorTipo.entrySet()) {
            resumenPanel.add(new JLabel("• " + entry.getKey() + ": $" + entry.getValue()));
        }

        // Agregar resumen a la derecha del centro
        centroPanel.add(resumenPanel, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonAgregar) {
            // ✅ Abre el formulario
            new IngresoSeleccionMenu().setVisible(true);
        }

    }
}
