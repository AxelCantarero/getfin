package org.getfin.vistas.Ingresos;

import javax.swing.*;
import java.awt.*;

public class IngresoSeleccionMenu extends JDialog {

    public IngresoSeleccionMenu(JFrame owner, ingresoVista vistaPadre) {
        super(owner, "Seleccionar tipo de ingreso", true);

        setLayout(new GridLayout());
        setSize(350, 200);
        setLocationRelativeTo(owner);

        JButton btnCosecha = new JButton("Registrar Cosecha");
        JButton btnAnimal = new JButton("Registrar Venta Animal");
        JButton btnLeche = new JButton("Registrar Venta Leche");
        btnCosecha.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAnimal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLeche.setFont(new Font("Segoe UI", Font.BOLD, 16));

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
