package org.getfin.vistas.Ingresos;

import javax.swing.*;
import java.awt.*;

public class IngresoSeleccionMenu extends JDialog {

    public IngresoSeleccionMenu(JFrame owner, ingresoVista vistaPadre) {
        super(owner, "Seleccionar tipo de ingreso", true);

        setSize(420, 260);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnCosecha = new JButton("Registrar Cosecha");
        JButton btnAnimal = new JButton("Registrar Venta Animal");
        JButton btnLeche = new JButton("Registrar Venta Leche");

        btnCosecha.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAnimal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLeche.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnCosecha.setBackground(new Color(52, 152, 219));
        btnAnimal.setBackground(new Color(52, 152, 219));
        btnLeche.setBackground(new Color(52, 152, 219));
        btnCosecha.setForeground(Color.WHITE);
        btnAnimal.setForeground(Color.WHITE);
        btnLeche.setForeground(Color.WHITE);
        btnCosecha.setFocusPainted(false);
        btnAnimal.setFocusPainted(false);
        btnLeche.setFocusPainted(false);

        add(btnCosecha);
        add(btnAnimal);
        add(btnLeche);

        btnCosecha.addActionListener(e -> {
            new cosechaFormulario(owner, vistaPadre).setVisible(true);
            dispose();
        });

        btnAnimal.addActionListener(e -> {
            new ventaAnimalFormulario(owner, vistaPadre).setVisible(true);
            dispose();
        });
        btnLeche.addActionListener(e -> {
            new ventaLecheFormulario(owner, vistaPadre).setVisible(true);
            dispose();
        });

    }
}