package org.getfin.vistas.Dashboard;

import javax.swing.*;
import java.awt.*;

public class dashboardVista extends JPanel {

    public dashboardVista() {
        cargarVentana();
    }

    private void cargarVentana() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // =============================
        // Fila 1: Panel contenedor con 3 paneles dinámicos
        // =============================
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.3; // ocupa 30% del alto
        gbc.gridwidth = 1;

        // Usamos GridLayout -> 3 columnas iguales
        JPanel contenedorArriba = new JPanel(new GridLayout(1, 3, 20, 0));
        contenedorArriba.setBackground(Color.DARK_GRAY);

        for (int i = 0; i < 3; i++) {
            JPanel panel = new JPanel();
            panel.setBackground(new Color(200, 200 - i * 50, 200 - i * 80));
            panel.add(new JLabel("Arriba-P" + (i + 1)));
            contenedorArriba.add(panel);
        }

        add(contenedorArriba, gbc);

        // =============================
        // Fila 2: 2 paneles grandes
        // =============================
        gbc.gridy = 1;
        gbc.weighty = 0.4;

        JPanel contenedorMedio = new JPanel(new GridLayout(1, 2, 20, 0));
        contenedorMedio.setBackground(Color.GRAY);

        JPanel panelGrafica = new JPanel();
        panelGrafica.setBackground(Color.BLUE);
        panelGrafica.add(new JLabel("Panel 4 (Grafica)"));

        JPanel panelDiagrama = new JPanel();
        panelDiagrama.setBackground(Color.CYAN);
        panelDiagrama.add(new JLabel("Panel 5 (Diagrama)"));

        contenedorMedio.add(panelGrafica);
        contenedorMedio.add(panelDiagrama);

        add(contenedorMedio, gbc);

        // =============================
        // Fila 3: 4 paneles medianos
        // =============================
        gbc.gridy = 2;
        gbc.weighty = 0.3;

        JPanel contenedorAbajo = new JPanel(new GridLayout(1, 4, 20, 0));
        contenedorAbajo.setBackground(Color.LIGHT_GRAY);

        for (int i = 0; i < 4; i++) {
            JPanel panel = new JPanel();
            panel.setBackground(new Color(150 + i * 20, 100, 200 - i * 30));
            panel.add(new JLabel("Abajo-P" + (i + 6)));
            contenedorAbajo.add(panel);
        }

        add(contenedorAbajo, gbc);
    }
}
