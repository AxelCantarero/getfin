package org.getfin.vistas.Ingresos;

import com.toedter.calendar.JDateChooser;
import org.getfin.controlador.AnimalController;
import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Animal;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.TipoAnimal;
import org.getfin.modelos.enums.TipoTransaccion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ventaAnimalFormulario extends JFrame {

    private List<Animal> listaAnimales;

    public ventaAnimalFormulario() {
        initFormulario();
    }

    private void initFormulario() {
        setTitle("Venta de Animales - Formulario de Registro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        AnimalController animalController = new AnimalController();
        listaAnimales = animalController.getAnimal();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // === PANEL IZQUIERDO ===
        JPanel panelIzquierdo = new JPanel(new GridLayout(8, 2, 10, 10));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Datos de la venta"));

        panelIzquierdo.add(new JLabel("Fecha:"));
        JDateChooser campoFecha = new JDateChooser();
        panelIzquierdo.add(campoFecha);

        panelIzquierdo.add(new JLabel("Tipo de Animal:"));
        JComboBox<TipoAnimal> campoTipoAnimal = new JComboBox<>(TipoAnimal.values());
        panelIzquierdo.add(campoTipoAnimal);

        panelIzquierdo.add(new JLabel("Animal:"));
        JComboBox<Animal> campoAnimal = new JComboBox<>();
        panelIzquierdo.add(campoAnimal);

        // Render solo nombre
        campoAnimal.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Animal a) setText(a.getNombre());
                return this;
            }
        });

        // Inicializar animales según tipo seleccionado
        campoTipoAnimal.addActionListener(e -> {
            campoAnimal.removeAllItems();
            TipoAnimal tipo = (TipoAnimal) campoTipoAnimal.getSelectedItem();

            for (Animal a : listaAnimales) {
                if (a.getTipo() == tipo) campoAnimal.addItem(a);
            }
        });

        // Cargar animales iniciales
        campoTipoAnimal.setSelectedIndex(0);

        panelIzquierdo.add(new JLabel("Cantidad vendida:"));
        JTextField campoCantidad = new JTextField();
        panelIzquierdo.add(campoCantidad);

        panelIzquierdo.add(new JLabel("Peso total (kg):"));
        JTextField campoPeso = new JTextField();
        panelIzquierdo.add(campoPeso);

        panelIzquierdo.add(new JLabel("Precio por kg:"));
        JTextField campoPrecioKg = new JTextField();
        panelIzquierdo.add(campoPrecioKg);

        panelIzquierdo.add(new JLabel("Cliente:"));
        JTextField campoCliente = new JTextField();
        panelIzquierdo.add(campoCliente);

        panelIzquierdo.add(new JLabel("Factura Nº:"));
        JTextField campoFactura = new JTextField();
        panelIzquierdo.add(campoFactura);

        // === PANEL DERECHO ===
        JPanel panelDerecho = new JPanel(new GridLayout(4, 2, 10, 10));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Totales"));

        panelDerecho.add(new JLabel("Retención:"));
        JTextField campoRetencion = new JTextField();
        panelDerecho.add(campoRetencion);

        panelDerecho.add(new JLabel("IVA (%):"));
        JTextField campoIVA = new JTextField();
        panelDerecho.add(campoIVA);

        panelDerecho.add(new JLabel("Total:"));
        JTextField campoTotal = new JTextField();
        campoTotal.setEditable(false);
        panelDerecho.add(campoTotal);

        // === DESCRIPCIÓN ===
        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        JTextArea areaDescripcion = new JTextArea();
        panelDescripcion.add(new JScrollPane(areaDescripcion), BorderLayout.CENTER);

        JPanel derecha = new JPanel(new BorderLayout(10, 10));
        derecha.add(panelDerecho, BorderLayout.NORTH);
        derecha.add(panelDescripcion, BorderLayout.CENTER);

        // === BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);

        botonCancelar.addActionListener(e -> dispose());

        // === CÁLCULO AUTOMÁTICO DEL TOTAL ===
        KeyAdapter recalcular = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTotal(campoPeso, campoPrecioKg, campoIVA, campoRetencion, campoTotal);
            }
        };

        campoPeso.addKeyListener(recalcular);
        campoPrecioKg.addKeyListener(recalcular);
        campoIVA.addKeyListener(recalcular);
        campoRetencion.addKeyListener(recalcular);


        botonGuardar.addActionListener(e -> {

            Date fecha = campoFecha.getDate();
            Animal animalSeleccionado = (Animal) campoAnimal.getSelectedItem();
            String numFactura = campoFactura.getText();
            BigDecimal total = new BigDecimal(campoTotal.getText());
            BigDecimal retencion = new BigDecimal(campoRetencion.getText());
            BigDecimal precioUnitario = new BigDecimal(campoPrecioKg.getText());
            BigDecimal iva = new BigDecimal(campoIVA.getText());
            BigDecimal cantidadObjeto = new BigDecimal(campoCantidad.getText());
            LocalDate fechaLocal = fecha.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            String cliente = campoCliente.getText();
            TipoTransaccion tipo= TipoTransaccion.INGRESO;
            String descripcion = areaDescripcion.getText();

            if (fecha == null || animalSeleccionado == null ||
                    campoCliente.getText().isEmpty() || campoCantidad.getText().isEmpty() ||
                    campoPeso.getText().isEmpty() || campoPrecioKg.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer cantidad;
            try {
                cantidad = Integer.parseInt(campoCantidad.getText());
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (animalSeleccionado.getCantidad() < cantidad) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad supera el stock disponible.",
                        "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (animalSeleccionado.getTipo() == TipoAnimal.PECES ||
                    animalSeleccionado.getTipo() == TipoAnimal.ABEJAS) {

                Integer nuevoStock = animalSeleccionado.getCantidad() - cantidad;

                animalSeleccionado.setCantidad(nuevoStock);
                animalController.editarAnimal(animalSeleccionado);
            }

            Transaccion transaccion = new Transaccion(
                    animalSeleccionado,
                    numFactura,
                    total,
                    retencion,
                    precioUnitario,
                    iva,
                    cantidadObjeto,
                    fechaLocal,
                    cliente,
                    tipo,
                    descripcion
            );
            TransaccionController.getInstance().guardarTransaccion(transaccion);
            JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");
            dispose();
        });

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(panelIzquierdo, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        mainPanel.add(derecha, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        mainPanel.add(panelBotones, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private void calcularTotal(JTextField campoPeso, JTextField campoPrecioKg,
                               JTextField campoIVA, JTextField campoRetencion, JTextField campoTotal) {
        try {
            BigDecimal peso = new BigDecimal(campoPeso.getText());
            BigDecimal precioKg = new BigDecimal(campoPrecioKg.getText());
            BigDecimal iva = new BigDecimal(campoIVA.getText());
            BigDecimal retencion = new BigDecimal(campoRetencion.getText());

            BigDecimal subtotal = peso.multiply(precioKg);
            BigDecimal ivaCalc = subtotal.multiply(iva).divide(new BigDecimal(100));

            BigDecimal total = subtotal.add(ivaCalc).subtract(retencion);

            campoTotal.setText(total.toString());
        } catch (Exception e) {
            campoTotal.setText("");
        }
    }
}
