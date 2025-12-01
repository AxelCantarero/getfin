package org.getfin.vistas.egresos;

import javax.swing.*;
import java.awt.*;

public class EgresoSeleccionMenu extends JDialog {

    public EgresoSeleccionMenu(JFrame parent, egresoVista vista) {
        super(parent, "Nuevo Egreso", true);

        setSize(420, 260);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnProducto = crearBoton("Compra de PRODUCTO");
        JButton btnAnimal = crearBoton("Compra de ANIMAL");
        JButton btnCultivo = crearBoton("Compra de CULTIVO");

        add(btnProducto);
        add(btnAnimal);
        add(btnCultivo);

        // EVENTOS
        btnProducto.addActionListener(e -> {
            new EgresoProductoFormulario(parent, vista).setVisible(true);
            dispose();
        });

        btnAnimal.addActionListener(e -> {
            new EgresoAnimalFormulario(parent, vista).setVisible(true);
            dispose();
        });

        btnCultivo.addActionListener(e -> {
            new EgresoCultivoFormulario(parent, vista).setVisible(true);
            dispose();
        });
    }

    private JButton crearBoton(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 18));
        b.setBackground(new Color(52, 152, 219));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }
}
