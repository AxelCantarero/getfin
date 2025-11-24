package org.getfin.vistas.cultivos;

import com.github.lgooddatepicker.components.DatePicker;
import org.getfin.modelos.Cultivo;
import org.getfin.modelos.enums.CategoriaCultivo;
import org.getfin.modelos.enums.EstadoCultivo;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FormularioCultivo extends JDialog {

    private final JTextField txtNombreCultivo = crearCampo();
    private final DatePicker chooserFechaSiembra = new DatePicker();
    private final JTextField txtAreaSembrada = crearCampo();
    private final JComboBox<CategoriaCultivo> comboCategoria = new JComboBox<>(CategoriaCultivo.values());
    private final JComboBox<EstadoCultivo> comboEstado = new JComboBox<>(EstadoCultivo.values());
    private final JTextField txtStock = crearCampo();
    private final JTextField txtMontoInicial = crearCampo();
    private final DatePicker chooserFechaCosecha = new DatePicker();

    private final CultivoVista vista;
    private final Cultivo cultivoEditar;

    public FormularioCultivo(JFrame parent, CultivoVista vista, Cultivo cultivoEditar) {
        super(parent, cultivoEditar == null ? "Agregar Producto" : "Editar Producto", true);
        this.vista = vista;
        this.cultivoEditar = cultivoEditar;

        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        comboCategoria.setSelectedItem(CategoriaCultivo.GRANOS_BASICOS);
        comboEstado.setSelectedItem(EstadoCultivo.PREPARACION);

        JPanel camposPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        camposPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        camposPanel.setBackground(Color.WHITE);

        camposPanel.add(new JLabel("Nombre de Cultivo:"));
        camposPanel.add(txtNombreCultivo);
        camposPanel.add(new JLabel("Fecha de Siembra:"));
        camposPanel.add(chooserFechaSiembra);
        camposPanel.add(new JLabel("Area Sembrada:"));
        camposPanel.add(txtAreaSembrada);
        camposPanel.add(new JLabel("Categoría:"));
        camposPanel.add(comboCategoria);
        camposPanel.add(new JLabel("Estado:"));
        camposPanel.add(comboEstado);
        camposPanel.add(new JLabel("Stock:"));
        camposPanel.add(txtStock);
        camposPanel.add(new JLabel("Monto Inicial:"));
        camposPanel.add(txtMontoInicial);
        camposPanel.add(new JLabel("Fecha de Cosecha:"));
        camposPanel.add(chooserFechaCosecha);


        // CAMBIO 4: Cargar la fecha es ahora mucho más simple
        if (cultivoEditar != null) {
            txtNombreCultivo.setText(cultivoEditar.getNombreCultivo());
            // Asignamos el LocalDate directamente al DatePicker. ¡Sin conversiones!
            chooserFechaSiembra.setDate(cultivoEditar.getFechaSiembra());
            txtAreaSembrada.setText(cultivoEditar.getAreaSembrada());
            comboCategoria.setSelectedItem(cultivoEditar.getCategoria());
            comboEstado.setSelectedItem(cultivoEditar.getEstado());
            txtStock.setText(cultivoEditar.getStock() != null ? cultivoEditar.getStock().toString() : "");
            txtMontoInicial.setText(cultivoEditar.getMontoInicial() != null ? cultivoEditar.getMontoInicial().toString() : "");
            chooserFechaCosecha.setDate(cultivoEditar.getFechaCosecha());

        }

        JButton btnGuardar = crearBoton("Guardar", new Color(33, 150, 243));
        JButton btnCancelar = crearBoton("Cancelar", Color.GRAY);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            if (txtNombreCultivo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa los campos obligatorios.");
                return;
            }

            LocalDate fechaSiembra = chooserFechaSiembra.getDate();

            BigDecimal areaSembrada = txtAreaSembrada.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtAreaSembrada.getText());
            BigDecimal stock = txtStock.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtStock.getText());
            BigDecimal montoInicial = txtMontoInicial.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtMontoInicial.getText());

            LocalDate fechaCosecha = chooserFechaCosecha.getDate();

            Cultivo cultivo = cultivoEditar != null ? cultivoEditar : new Cultivo();
            cultivo.setNombreCultivo(txtNombreCultivo.getText().trim());
            cultivo.setFechaSiembra(fechaSiembra);
            cultivo.setAreaSembrada(txtAreaSembrada.getText().trim());
            cultivo.setCategoria((CategoriaCultivo) comboCategoria.getSelectedItem());
            cultivo.setEstado((EstadoCultivo) comboEstado.getSelectedItem());
            cultivo.setStock(stock);
            cultivo.setMontoInicial(montoInicial);
            cultivo.setFechaCosecha(fechaCosecha);

            vista.guardarOActualizar(cultivo);
            dispose();
        });

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        botonesPanel.setBackground(Color.WHITE);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnCancelar);

        add(camposPanel, BorderLayout.CENTER);
        add(botonesPanel, BorderLayout.SOUTH);
    }

    private static JTextField crearCampo() {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBackground(new Color(220, 220, 220));
        t.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        return t;
    }

    private static JButton crearBoton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(100, 35));
        return b;
    }
}