package org.getfin.vistas.contenidoPrincipal;

import org.getfin.Componentes.Barraopciones;
import org.getfin.vistas.Dashboard.dashboardVista;
import org.getfin.vistas.Ingresos.ingresoVista;
import org.getfin.vistas.componentes.menuOpciones;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;

public class ventanaPrincipal extends JFrame {

    private final Barraopciones barraopciones = new Barraopciones();
    private final JPanel contenido = new JPanel(new BorderLayout());

    public ventanaPrincipal() {
        initComponents();
        configurarMenu();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1200, 700);
        setLocationRelativeTo(null);

        /** Barra superior */
        JPanel barraSuperior = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(41, 82, 128));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        barraSuperior.setLayout(new BorderLayout());
        barraSuperior.setPreferredSize(new Dimension(1200, 60));
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel lblTitulo = new JLabel("GETFIN");
        lblTitulo.setFont(new Font("Open Sans", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        barraSuperior.add(lblTitulo, BorderLayout.WEST);

        add(barraSuperior, BorderLayout.NORTH);

        /** Menú lateral */
        barraopciones.setPreferredSize(new Dimension(60, 100));
        add(barraopciones, BorderLayout.WEST);

        /** Panel central */
        contenido.setBackground(Color.WHITE);
        add(contenido, BorderLayout.CENTER);

        setVisible(true);
    }

    private void configurarMenu() {

        barraopciones.setOpcionSeleccionadaListener(opcion -> {

            switch (opcion) {

                case "Dashboard" -> cargarPanel(new dashboardVista());

                case "Ingreso" -> cargarPanel(new ingresoVista());

                case "Egreso" -> JOptionPane.showMessageDialog(this,
                        "Aún no has creado la vista EgresoVista");

                case "Animal" -> JOptionPane.showMessageDialog(this,
                        "Aún no has creado la vista AnimalVista");

                case "Visita" -> JOptionPane.showMessageDialog(this,
                        "Aún no has creado la vista Visitas");

                case "Inventario" -> JOptionPane.showMessageDialog(this,
                        "Aún no has creado la vista Invetario");

                case "Cultivo" -> JOptionPane.showMessageDialog(this,
                        "Aún no has creado la vista Cultivo");

                case "Salir" -> System.exit(0);

                default -> contenido.removeAll();
            }
        });
    }

    private void cargarPanel(JPanel panel) {
        contenido.removeAll();
        contenido.add(panel, BorderLayout.CENTER);
        contenido.revalidate();
        contenido.repaint();
    }

}
