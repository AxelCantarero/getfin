package org.getfin.Componentes;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class Barraopciones extends JPanel {

    public interface OpcionSeleccionadaListener {
        void onOpcionSeleccionada(String opcion);
    }

    private static final Logger logger = Logger.getLogger(Barraopciones.class.getName());
    private OpcionSeleccionadaListener listener;
    private Color colorFondo = new Color(12, 55, 15);
    private Color colorHover = new Color(30, 80, 30);
    private Color colorTexto = Color.WHITE;
    private Font fuenteTexto = new Font("Bahnschrift", Font.BOLD, 12);
    private int alturaItem = 80;
    private int anchoItem = 90;

    // Variables para el menú desplegable
    private boolean menuExpandido = false;
    private int anchoColapsado = 50;
    private int anchoExpandido = 140;
    private JPanel panelOpciones;
    private int anchoActual;
    private java.util.List<JPanel> listaPanelesOpciones = new java.util.ArrayList<>();
    private JButton btnToggle;

    public Barraopciones() {
        initComponents();
        anchoActual = anchoColapsado;
    }

    public void setOpcionSeleccionadaListener(OpcionSeleccionadaListener listener) {
        this.listener = listener;
    }

    public OpcionSeleccionadaListener getOpcionSeleccionadaListener() {
        return this.listener;
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(colorFondo);
        setPreferredSize(new Dimension(anchoColapsado, 0)); // Alto dinámico
        setMinimumSize(new Dimension(anchoColapsado, 0));
        setMaximumSize(new Dimension(anchoExpandido, Integer.MAX_VALUE));


        // CINTA GRIS SUPERIOR CON GRADIENTE
        JPanel barraSuperior = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(50, 50, 50), getWidth(), getHeight(), new Color(120, 120, 120));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        barraSuperior.setLayout(new BorderLayout());
        barraSuperior.setPreferredSize(new Dimension(anchoColapsado, 50));
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Botón toggle de flecha (fondo igual a la barra lateral, icono blanco)
        btnToggle = new JButton("→");
        btnToggle.setFont(new Font("Arial", Font.BOLD, 18));
        btnToggle.setForeground(Color.WHITE);
        btnToggle.setBackground(colorFondo); // mismo color que la barra lateral
        btnToggle.setOpaque(true);
        btnToggle.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> alternarBarra());
        btnToggle.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnToggle.setBackground(colorFondo); // mismo color en hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnToggle.setBackground(colorFondo); // mismo color al salir
            }
        });
        barraSuperior.add(btnToggle, BorderLayout.CENTER);
        add(barraSuperior, BorderLayout.NORTH);

        panelOpciones = new JPanel();
        panelOpciones.setBackground(colorFondo);
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));

        listaPanelesOpciones.clear();
        agregarItemOpcion(panelOpciones, "/dashboard.png", "Dashboard");
        agregarItemOpcion(panelOpciones, "/Receive Euro.png", "Ingreso");
        agregarItemOpcion(panelOpciones, "/Receive Euro.png", "Egreso");
        agregarItemOpcion(panelOpciones, "/Animal.png", "Animal");
        agregarItemOpcion(panelOpciones, "/Visit (1).png", "Visita");
        agregarItemOpcion(panelOpciones, "/Wheat.png", "Cultivo");
        agregarItemOpcion(panelOpciones, "/Clipboard List.png", "Inventario");
        panelOpciones.add(Box.createVerticalGlue());
        agregarItemOpcion(panelOpciones, "/garden_exit-fill-12.png", "Salir");

        add(panelOpciones, BorderLayout.CENTER);
        mostrarSoloIconos();
    }

    // Alternar barra expandida/colapsada
    private void alternarBarra() {
        menuExpandido = !menuExpandido;
        btnToggle.setText(menuExpandido ? "←" : "→");
        btnToggle.setForeground(Color.WHITE); // Asegura que la flecha sea blanca
        btnToggle.setBackground(colorFondo); // mismo color que la barra lateral
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            if (menuExpandido && anchoActual < anchoExpandido) {
                anchoActual += 10;
                if (anchoActual > anchoExpandido) anchoActual = anchoExpandido;
                setPreferredSize(new Dimension(anchoActual, getHeight()));
                Container parent = getParent();
                if (parent != null) {
                    parent.revalidate();
                    parent.repaint();
                }
                if (anchoActual == anchoExpandido) {
                    timer.stop();
                    mostrarIconosYTexto();
                }
            } else if (!menuExpandido && anchoActual > anchoColapsado) {
                anchoActual -= 10;
                if (anchoActual < anchoColapsado) anchoActual = anchoColapsado;
                setPreferredSize(new Dimension(anchoActual, getHeight()));
                Container parent = getParent();
                if (parent != null) {
                    parent.revalidate();
                    parent.repaint();
                }
                if (anchoActual == anchoColapsado) {
                    timer.stop();
                    mostrarSoloIconos();
                }
            } else {
                timer.stop();
            }
        });
        timer.start();
    }

    // Mostrar solo iconos (texto oculto)
    private void mostrarSoloIconos() {
        for (JPanel panel : listaPanelesOpciones) {
            for (Component subComp : panel.getComponents()) {
                if (subComp instanceof JLabel label) {
                    if (label.getText() != null && !label.getText().isEmpty()) {
                        label.setVisible(false);
                    }
                }
            }
            // Tooltips activos cuando está colapsado
            for (Component subComp : panel.getComponents()) {
                if (subComp instanceof JLabel label) {
                    if (label.getText() != null && !label.getText().isEmpty()) {
                        panel.setToolTipText(label.getText());
                        break;
                    }
                }
            }
        }
    }

    // Mostrar iconos y texto (barra expandida)
    private void mostrarIconosYTexto() {
        for (JPanel panel : listaPanelesOpciones) {
            for (Component subComp : panel.getComponents()) {
                if (subComp instanceof JLabel label) {
                    if (label.getText() != null && !label.getText().isEmpty()) {
                        label.setVisible(true);
                    }
                }
            }
            panel.setToolTipText(null);
        }
    }

    private void agregarItemOpcion(JPanel panelContenedor, String iconPath, String texto) {
        JPanel panelOpcion = new JPanel();
        panelOpcion.setLayout(new BoxLayout(panelOpcion, BoxLayout.Y_AXIS));
        panelOpcion.setBackground(colorFondo);
        panelOpcion.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        panelOpcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelOpcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaItem));
        panelOpcion.setPreferredSize(new Dimension(anchoExpandido, alturaItem));

        JPanel panelIcono = new JPanel();
        panelIcono.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelIcono.setBackground(colorFondo);
        panelIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblIcono = new JLabel();
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(iconPath));
            Image img = icono.getImage();
            int iconSize = Math.min(35, alturaItem - 30);
            Image scaledImg = img.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(scaledImg));
            lblIcono.setHorizontalAlignment(JLabel.CENTER);
        } catch (Exception e) {
            logger.warning("No se encontró el recurso: " + iconPath);
            lblIcono.setText("X");
            lblIcono.setFont(fuenteTexto.deriveFont(Font.BOLD, 16f));
            lblIcono.setForeground(Color.RED);
        }
        panelIcono.add(lblIcono);
        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(fuenteTexto);
        lblTexto.setForeground(colorTexto);
        lblTexto.setHorizontalAlignment(JLabel.CENTER);
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTexto.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        lblTexto.setVisible(false); // Solo iconos al inicio
        panelOpcion.add(panelIcono);
        panelOpcion.add(lblTexto);
        panelOpcion.setToolTipText(texto);
        panelOpcion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) {
                    listener.onOpcionSeleccionada(texto);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                panelOpcion.setBackground(colorHover);
                panelIcono.setBackground(colorHover);
                lblTexto.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panelOpcion.setBackground(colorFondo);
                panelIcono.setBackground(colorFondo);
                lblTexto.setForeground(colorTexto);
            }
        });
        panelContenedor.add(panelOpcion);
        panelContenedor.add(Box.createVerticalStrut(0));
        listaPanelesOpciones.add(panelOpcion);
    }

    // Métodos para personalización
    public void setColorFondo(Color color) {
        this.colorFondo = color;
        setBackground(color);
    }

    public void setColorHover(Color color) {
        this.colorHover = color;
    }

    public void setColorTexto(Color color) {
        this.colorTexto = color;
    }

    public void setFuenteTexto(Font fuente) {
        this.fuenteTexto = fuente;
    }

    public void setAlturaItem(int altura) {
        this.alturaItem = altura;
    }

    public void setAnchoItem(int ancho) {
        this.anchoItem = ancho;
    }

    public boolean isMenuExpandido() {
        return menuExpandido;
    }
}
