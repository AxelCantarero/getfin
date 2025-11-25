package org.getfin.vistas.Ingresos;

import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Transaccion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ingresoVista extends JPanel implements ActionListener {

    private JButton botonAgregar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

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

        // Botón agregar ingreso
        botonAgregar = new JButton("Agregar Ingreso");
        botonAgregar.addActionListener(this);
        superiorPanel.add(botonAgregar, BorderLayout.EAST);

        add(superiorPanel, BorderLayout.NORTH);

        // ---------- 2️⃣ PANEL CENTRAL ----------
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

        // ---------- 4️⃣ TABLA REAL CON TRANSACCIONES ----------
        String[] columnas = {
                "ID", "Tipo", "Fecha", "Cliente", "Descripción",
                "Cantidad", "Precio U.", "Total", "Factura", "Referencia"
        };

        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);
        centroPanel.add(scroll, BorderLayout.CENTER);

        // Cargar datos reales
        actualizarTabla();

        // ---------- 5️⃣ RESUMEN DEL MES ----------
        JPanel resumenPanel = new JPanel();
        resumenPanel.setLayout(new BoxLayout(resumenPanel, BoxLayout.Y_AXIS));
        resumenPanel.setBorder(BorderFactory.createTitledBorder("Resumen del mes"));
        resumenPanel.setPreferredSize(new Dimension(200, 0)); // ancho fijo

        // Datos simulados del resumen
        String mes = "Octubre 2025";
        double total = 500.0;
        Map<String, Double> totalesPorTipo = new LinkedHashMap<>();
        totalesPorTipo.put("Ventas", 400.0);
        totalesPorTipo.put("Donaciones", 100.0);

        resumenPanel.add(new JLabel("Mes: " + mes));
        resumenPanel.add(Box.createVerticalStrut(5));
        resumenPanel.add(new JLabel("Total general: $" + total));
        resumenPanel.add(Box.createVerticalStrut(10));
        resumenPanel.add(new JLabel("Totales por tipo:"));
        for (Map.Entry<String, Double> entry : totalesPorTipo.entrySet()) {
            resumenPanel.add(new JLabel("• " + entry.getKey() + ": $" + entry.getValue()));
        }

        centroPanel.add(resumenPanel, BorderLayout.EAST);
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);

        List<Transaccion> lista = TransaccionController.getInstance().getTransacciones();

        for (Transaccion t : lista) {
            String referencia = "";
            if (t.getProducto() != null) referencia = t.getProducto().getNombreProducto();
            if (t.getCultivo() != null) referencia = t.getCultivo().getNombreCultivo();
            if (t.getAnimal() != null) referencia = t.getAnimal().getNombre();

            modeloTabla.addRow(new Object[]{
                    t.getIdTransaccion(),
                    t.getTipo(),
                    t.getFecha(),
                    t.getNombreCliente(),
                    t.getDescripcion(),
                    t.getCantidad(),
                    t.getPrecioUnitario(),
                    t.getTotal(),
                    t.getNumeroFactura(),
                    referencia
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonAgregar) {

            // 🔥 Abrir menú para seleccionar tipo de ingreso
            IngresoSeleccionMenu menu = new IngresoSeleccionMenu();
            menu.setVisible(true);
        }
    }
}
