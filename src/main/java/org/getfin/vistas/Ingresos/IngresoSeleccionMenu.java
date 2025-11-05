package org.getfin.vistas.Ingresos;

import javax.swing.*;
import java.awt.*;

public class IngresoSeleccionMenu extends JFrame {

    public IngresoSeleccionMenu() {
        setTitle("Seleccionar tipo de ingreso");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Botones
        JButton btnCosecha = new JButton("Registro de Cosecha");
        JButton btnVentaAnimales = new JButton("Venta de Animales");

        btnCosecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVentaAnimales.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Acciones
        btnCosecha.addActionListener(e -> {
            new cosechaFormulario();
            dispose(); // cierra el menú
        });

        btnVentaAnimales.addActionListener(e -> {
            new ventaAnimalFormulario(); // esta clase la crearás igual que cosechaFormulario
            dispose();
        });

        // Agregar botones al panel
        panel.add(btnCosecha);
        panel.add(btnVentaAnimales);

        add(panel);
        setVisible(true);
    }
}
