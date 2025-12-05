package org.getfin.vistas.Dashboard;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// JFreeChart imports
import org.getfin.controlador.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class dashboardVista extends JPanel {

    private BigDecimal montoEgreso;
    private BigDecimal montoIngreso;
    private BigDecimal montoCapital;
    private String mes;
    private BigDecimal variacionIngreso;
    private BigDecimal variacionEgreso;
    private BigDecimal variacionCapital;
    private int totalProducto;
    private int totalCultivo;
    private int totalAnimal;
    private int totalVisitas;
    public dashboardVista() {
        inicializarDatos();
        cargarVentana();
    }

    private void inicializarDatos() {
        montoEgreso = TransaccionController.getInstance().totalEgresoFecha(LocalDate.now().getMonthValue(),LocalDate.now().getYear());
        montoIngreso = TransaccionController.getInstance().totalIngresoFecha(LocalDate.now().getMonthValue(),LocalDate.now().getYear());
        montoCapital= TransaccionController.getInstance().totalCapital();
        mes = obtenerNombreMesActual();

        variacionIngreso = TransaccionController.getInstance().PorcentajeCrecimientoIngreso(LocalDate.now().getMonthValue(),LocalDate.now().getYear());
        variacionEgreso= TransaccionController.getInstance().PorcentajeCrecimientoEgreso(LocalDate.now().getMonthValue(),LocalDate.now().getYear());

        totalProducto= ProductoController.getInstance().totalPrododucto();
        totalCultivo= CultivoController.getInstance().totalCultivo();
        totalAnimal= AnimalController.getInstance().totalAnimal();
        totalVisitas = VisitaController.getInstance().totalVisitasMes(LocalDate.now().getMonthValue(),LocalDate.now().getYear());

    }

    private void cargarVentana() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // =============================
        // Fila 1
        // =============================
        // =============================

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.10;

        JPanel contenedorArriba = new JPanel(new GridLayout(1, 3, 20, 0));
        contenedorArriba.setBackground(Color.DARK_GRAY);

// Datos de ejemplo — luego los reemplazas por tus valores reales
        String[] titulos = {"Gastos", "Ingresos", "Capital"};
        String[] meses = {mes, mes, mes};
        String[] montos = {String.valueOf(montoEgreso), String.valueOf(montoIngreso), String.valueOf(montoCapital)};
        String[] variaciones = {String.valueOf(variacionEgreso.stripTrailingZeros().toPlainString() + "%"), String.valueOf(variacionIngreso.stripTrailingZeros().toPlainString() + "%"), ""};
        Color[] fondos = {
                new Color(190, 110, 110),  // Rojo suave
                new Color(110, 160, 130),  // Verde suave
                new Color(80, 130, 210)    // Azul suave
        };

        for (int i = 0; i < 3; i++) {

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(fondos[i]);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // ------- Panel superior (título + mes) -------
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);

            JLabel lblTitulo = new JLabel(titulos[i]);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel lblMes = new JLabel(meses[i]);
            lblMes.setFont(new Font("Arial", Font.PLAIN, 14));
            lblMes.setForeground(Color.BLUE);

            header.add(lblTitulo, BorderLayout.WEST);
            header.add(lblMes, BorderLayout.EAST);

            // ------- Monto al centro -------
            JLabel lblMonto = new JLabel(montos[i], SwingConstants.CENTER);
            lblMonto.setFont(new Font("Arial", Font.BOLD, 24));

            // ------- Variación abajo -------
            JLabel lblVariacion = new JLabel(variaciones[i], SwingConstants.CENTER);
            lblVariacion.setFont(new Font("Arial", Font.BOLD, 16));

            if (variaciones[i].contains("-"))
                lblVariacion.setForeground(Color.RED);
            else
                lblVariacion.setForeground(new Color(0, 150, 0));

            // Agregar al panel
            panel.add(header, BorderLayout.NORTH);
            panel.add(lblMonto, BorderLayout.CENTER);
            panel.add(lblVariacion, BorderLayout.SOUTH);

            contenedorArriba.add(panel);
        }

        add(contenedorArriba, gbc);


        // =============================
        // Fila 2 — Aquí van TUS GRÁFICAS
        // =============================
        gbc.gridy = 1;
        gbc.weighty = 1.4;

        JPanel contenedorMedio = new JPanel(new GridLayout(1, 2, 20, 0));
        contenedorMedio.setBackground(Color.GRAY);

        // PANEL GRAFICA (Bar Chart)
        JPanel panelGrafica = new JPanel(new BorderLayout());
        panelGrafica.setBackground(Color.BLUE);

        // -> Agregamos el gráfico de barras
        panelGrafica.add(crearGraficaBarras(), BorderLayout.CENTER);


        // PANEL DIAGRAMA (Pie Chart)
        JPanel panelDiagrama = new JPanel(new BorderLayout());
        panelDiagrama.setBackground(Color.CYAN);

        // -> Agregamos el gráfico de pastel
        panelDiagrama.add(crearGraficaPie(), BorderLayout.CENTER);


        contenedorMedio.add(panelGrafica);
        contenedorMedio.add(panelDiagrama);

        add(contenedorMedio, gbc);

        // =============================
// Fila 3
// =============================
        gbc.gridy = 2;
        gbc.weighty = 0.10;

        JPanel contenedorAbajo = new JPanel(new GridLayout(1, 4, 20, 0));
        contenedorAbajo.setBackground(new Color(220, 220, 220));

        String[] titulos3 = {"Animales", "Cultivos", "Herramientas", "Visitas"};
        ImageIcon[] iconos = {
                cargarIcono("/vaca.png", 24, 30),
                cargarIcono("/Cultivo.png", 24, 30),
                cargarIcono("/herramientas.png", 24, 30),
                cargarIcono("/visitas.png", 24, 30)
        };
        int[] valores = {totalAnimal, totalCultivo, totalProducto, totalVisitas};

        for (int i = 0; i < 4; i++) {

            JPanel tarjeta = new JPanel();
            tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
            tarjeta.setBackground(Color.WHITE);
            tarjeta.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

            // Icono (emojis temporales)
            JLabel icono = new JLabel(iconos[i], SwingConstants.CENTER);
            icono.setFont(new Font("Arial", Font.PLAIN, 10));
            icono.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Título
            JLabel titulo = new JLabel(titulos3[i]);
            titulo.setFont(new Font("Arial", Font.BOLD, 18));
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Valor numérico
            JLabel valor = new JLabel(String.valueOf(valores[i]));
            valor.setFont(new Font("Arial", Font.BOLD, 22));
            valor.setAlignmentX(Component.CENTER_ALIGNMENT);

            tarjeta.add(Box.createVerticalStrut(10));
            tarjeta.add(icono);
            tarjeta.add(Box.createVerticalStrut(5));
            tarjeta.add(titulo);
            tarjeta.add(Box.createVerticalStrut(5));
            tarjeta.add(valor);

            contenedorAbajo.add(tarjeta);
        }

        add(contenedorAbajo, gbc);

    }
    // =========================================================
    //  GRÁFICO DE BARRAS (Ingresos vs Egresos)
    // =========================================================
    private ChartPanel crearGraficaBarras() {

        // Valores de ejemplo
        BigDecimal ingresos = montoIngreso;
        BigDecimal egresos  = montoEgreso;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(ingresos, "Ingresos", mes);
        dataset.addValue(egresos,  "Egresos", mes);

        JFreeChart chart = ChartFactory.createBarChart(
                "Ingresos vs Egresos",
                "",
                "Monto (C$)",
                dataset
        );

        // ================================
        // Mostrar valores encima de cada barra
        // ================================
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelsVisible(true);          // Activa etiquetas
        renderer.setDefaultItemLabelFont(new Font("Arial", Font.BOLD, 12));
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        return new ChartPanel(chart);
    }


    // =========================================================
    //  GRÁFICO DE PASTEL (Distribución)
    // =========================================================
    private ChartPanel crearGraficaPie() {

        BigDecimal ingresos = montoIngreso;
        BigDecimal egresos  = montoEgreso;

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Ingresos", ingresos);
        dataset.setValue("Egresos", egresos);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Distribución del Mes",
                dataset,
                true,
                true,
                false
        );

        // ================================
        //   MOSTRAR PORCENTAJES
        // ================================
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setLabelGenerator(
                new StandardPieSectionLabelGenerator(
                        "{0}: {2}",       // 0 = nombre, 1 = valor, 2 = porcentaje
                        NumberFormat.getNumberInstance(),
                        NumberFormat.getPercentInstance()
                )
        );

        // Mejoras visuales opcionales (no cambian tu layout)
        plot.setCircular(true);
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        plot.setLabelBackgroundPaint(Color.WHITE);

        return new ChartPanel(pieChart);
    }
    private ImageIcon cargarIcono(String ruta, int ancho, int alto) {
        ImageIcon iconOriginal = new ImageIcon(getClass().getResource(ruta));
        Image imgEscalada = iconOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imgEscalada);
    }
    public String obtenerNombreMesActual() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault());
        return hoy.format(formatter);
    }

}
