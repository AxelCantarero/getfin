package org.getfin.vistas.componentes;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class menuOpciones extends JPanel {

    public menuOpciones() {
        cargarMenu();
    }

    private void cargarMenu() {
        setLayout(new GridBagLayout()); // usamos GridBagLayout
        setBackground(Color.GREEN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;   // siempre en la columna 0
        gbc.weightx = 1; // que se expanda en ancho
        gbc.fill = GridBagConstraints.HORIZONTAL; // llenar horizontalmente
        gbc.insets = new Insets(10, 10, 10, 10); // márgenes entre componentes

        int fila = 0; // control de filas

        // Dashboard
        JPanel panelDashboard = crearPanelConIcono("/dashboard.png", "Dashboard");
        gbc.gridy = fila++;
        add(panelDashboard, gbc);

        // Ventas
        JPanel panelVentas = crearPanelConIcono("/venta.png", "Ventas");
        gbc.gridy = fila++;
        add(panelVentas, gbc);

        // Compras
        JPanel panelCompras = crearPanelConIcono("/compras.png", "Compras");
        gbc.gridy = fila++;
        add(panelCompras, gbc);

        // Inventario
        JPanel panelInventario = crearPanelConIcono("/inventario.png", "Inventario");
        gbc.gridy = fila++;
        add(panelInventario, gbc);

        // Cultivo
        JPanel panelCultivo = crearPanelConIcono("/cultivo.png", "Cultivo");
        gbc.gridy = fila++;
        add(panelCultivo, gbc);

        // Visitas
        JPanel panelVisitas = crearPanelConIcono("/Visit.png", "Visitas");
        gbc.gridy = fila++;
        add(panelVisitas, gbc);

        // Salir
        JPanel panelSalir = crearPanelConIcono("/Close.png", "Salir");
        gbc.gridy = fila++;
        add(panelSalir, gbc);

        // Espaciador flexible al final para que "empuje" hacia arriba
        gbc.gridy = fila;
        gbc.weighty = 1; // este empuja hacia abajo
        gbc.fill = GridBagConstraints.BOTH;
        add(Box.createGlue(), gbc);


    }

    private JPanel crearPanelConIcono(String rutaIcono, String texto) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        URL url = getClass().getResource(rutaIcono);
        if (url != null) {
            JLabel imagen = new JLabel(new ImageIcon(url));
            imagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(imagen);
        }

        JLabel label = new JLabel(texto);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        return panel;
    }
}
