package org.getfin.vistas.Ingresos;

import com.toedter.calendar.JDateChooser;
import org.getfin.controlador.AnimalController;
import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Animal;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.EstadoAnimal;
import org.getfin.modelos.enums.TipoAnimal;
import org.getfin.modelos.enums.TipoTransaccion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Formulario para venta de animales.
 * - Filtra animales por tipo
 * - En edición muestra el animal original incluso si está vendido / sin stock
 * - Cálculo automático de subtotal / total (acepta ',' o '.')
 * - Al guardar: ajusta cantidades / estado del animal (PECES / ABEJAS: resta cantidad; otros: marca VENDIDO)
 *
 * Nota: usar TransaccionController.getInstance() tal como lo tienes en tu proyecto.
 */
public class ventaAnimalFormulario extends JFrame {

    private final ingresoVista parentVista;
    private List<Animal> listaAnimales;

    private JDateChooser campoFecha;
    private JComboBox<TipoAnimal> campoTipoAnimal;
    private JComboBox<Animal> campoAnimal;
    private JTextField campoCliente, campoCantidad, campoPrecio, campoIVA, campoRetencion, campoTotal, campoFactura, campoSubTotal;
    private JTextArea areaDescripcion;
    private JButton botonGuardar, botonCancelar;
    private Transaccion transaccionActual = null;

    // animal original cuando estamos editando (se mantiene visible aunque vendido)
    private Animal animalOriginal = null;

    // bandera para suspender listeners mientras cargamos datos programáticamente
    private boolean suspendEvents = false;

    public ventaAnimalFormulario(JFrame parent, ingresoVista vista) {
        super("Formulario Venta Animal");
        this.parentVista = vista;
        initFormulario();
    }

    private void initFormulario() {
        setTitle("Venta de Animal - Formulario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // =========================
        // PANEL IZQUIERDO (DATOS PRINCIPALES)
        // =========================
        JPanel panelIzq = new JPanel(new GridLayout(7,2,6,6));
        panelIzq.setBorder(BorderFactory.createTitledBorder("Datos principales"));

        // Fecha
        panelIzq.add(new JLabel("Fecha:"));
        campoFecha = new JDateChooser();
        panelIzq.add(campoFecha);

        // Tipo de animal (nuevo combo para filtrar)
        panelIzq.add(new JLabel("Tipo de Animal:"));
        campoTipoAnimal = new JComboBox<>(TipoAnimal.values());
        panelIzq.add(campoTipoAnimal);

        // Animal
        panelIzq.add(new JLabel("Animal:"));
        campoAnimal = new JComboBox<>();
        panelIzq.add(campoAnimal);

        // Cliente
        panelIzq.add(new JLabel("Cliente:"));
        campoCliente = new JTextField();
        panelIzq.add(campoCliente);

        // Cantidad
        panelIzq.add(new JLabel("Cantidad:"));
        campoCantidad = new JTextField();
        panelIzq.add(campoCantidad);

        // Precio unitario
        panelIzq.add(new JLabel("Precio Unitario:"));
        campoPrecio = new JTextField();
        panelIzq.add(campoPrecio);

        // IVA (aunque lo calculas, lo dejo para compatibilidad)
        panelIzq.add(new JLabel("IVA (%):"));
        campoIVA = new JTextField("0");
        panelIzq.add(campoIVA);

        // =========================
        // PANEL DERECHO (TOTALES Y DESCRIPCIÓN)
        // =========================
        JPanel panelDer = new JPanel(new BorderLayout());
        panelDer.setBorder(BorderFactory.createTitledBorder("Totales y descripción"));

        JPanel panelTotales = new JPanel(new GridLayout(4,2,6,6));
        panelTotales.add(new JLabel("Retención:"));
        campoRetencion = new JTextField("0");
        panelTotales.add(campoRetencion);

        panelTotales.add(new JLabel("Sub Total:"));
        campoSubTotal = new JTextField();
        campoSubTotal.setEditable(false);
        panelTotales.add(campoSubTotal);

        panelTotales.add(new JLabel("Total:"));
        campoTotal = new JTextField();
        campoTotal.setEditable(false);
        panelTotales.add(campoTotal);

        panelTotales.add(new JLabel("Número Factura:"));
        campoFactura = new JTextField();
        panelTotales.add(campoFactura);

        areaDescripcion = new JTextArea(5,20);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDescripcion);

        panelDer.add(panelTotales, BorderLayout.NORTH);
        panelDer.add(scroll, BorderLayout.CENTER);

        // =========================
        // BOTONES
        // =========================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonGuardar = new JButton("Guardar");
        botonCancelar = new JButton("Cancelar");
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);

        // =========================
        // AGREGAR TODO AL MAIN PANEL
        // =========================
        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0.5; gbc.weighty=1;
        mainPanel.add(panelIzq, gbc);
        gbc.gridx=1; gbc.gridy=0; gbc.weightx=0.5; gbc.weighty=1;
        mainPanel.add(panelDer, gbc);
        gbc.gridx=0; gbc.gridy=1; gbc.gridwidth=2; gbc.weighty=0;
        mainPanel.add(panelBotones, gbc);

        add(mainPanel);

        // listeners básicos
        botonCancelar.addActionListener(e -> dispose());
        botonGuardar.addActionListener(e -> guardarTransaccion());

        // Renderer para mostrar nombre en JComboBox<Animal>
        campoAnimal.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean isSelected,boolean cellHasFocus){
                super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                if(value instanceof Animal a) setText(a.getNombre());
                else setText("");
                return this;
            }
        });

        // Document listeners para recalcular dinámicamente y validar.
        // Si suspendEvents == true -> no ejecutan
        DocumentListener recalcularListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { if (!suspendEvents) { recalcularTotales(); validarFormulario(); } }
            public void removeUpdate(DocumentEvent e) { if (!suspendEvents) { recalcularTotales(); validarFormulario(); } }
            public void changedUpdate(DocumentEvent e) { if (!suspendEvents) { recalcularTotales(); validarFormulario(); } }
        };

        campoCantidad.getDocument().addDocumentListener(recalcularListener);
        campoPrecio.getDocument().addDocumentListener(recalcularListener);
        campoIVA.getDocument().addDocumentListener(recalcularListener);
        campoRetencion.getDocument().addDocumentListener(recalcularListener);

        // Al cambiar tipo de animal, recargar animales disponibles para ese tipo
        campoTipoAnimal.addActionListener(e -> cargarAnimalesDisponibles());

        // Inicializar lista desde controller y cargar inicialmente
        listaAnimales = AnimalController.getInstance().getAnimal();
        cargarAnimalesDisponibles();

        // Validación inicial
        validarFormulario();

        setVisible(true);
    }

    // -------------- CARGA DE ANIMALES ----------------
    private void cargarAnimalesDisponibles() {
        campoAnimal.removeAllItems();

        // Trae lista actualizada desde controller (por si cambió)
        listaAnimales = AnimalController.getInstance().getAnimal();

        TipoAnimal tipoSeleccionado = (TipoAnimal) campoTipoAnimal.getSelectedItem();
        if (tipoSeleccionado == null) return;

        for (Animal a : listaAnimales) {
            // mostrar solo animales del tipo seleccionado y con stock / no vendidos
            boolean tieneCantidad = a.getCantidad() != null && a.getCantidad() > 0;
            boolean noVendido = a.getEstado() == null || a.getEstado() != EstadoAnimal.VENDIDO;
            if (a.getTipo() == tipoSeleccionado && tieneCantidad && noVendido) {
                campoAnimal.addItem(a);
            }
        }

        // si estamos en edición y hay un animalOriginal, queremos que permanezca visible:
        if (animalOriginal != null) {
            boolean yaEnCombo = false;
            for (int i=0;i<campoAnimal.getItemCount();i++) {
                Animal it = campoAnimal.getItemAt(i);
                if (it.getIdAnimal().equals(animalOriginal.getIdAnimal())) {
                    yaEnCombo = true;
                    break;
                }
            }
            if (!yaEnCombo) {
                campoAnimal.addItem(animalOriginal);
            }
            campoAnimal.setSelectedItem(animalOriginal);
        }
    }

    /**
     * Carga animales para edición: incluye animales disponibles + el animal original (si no está en disponibles)
     */
    private void cargarAnimalesParaEdicion(Animal animalOriginalParam) {
        this.animalOriginal = animalOriginalParam;
        // seleccionar tipo del animal original (esto disparará cargarAnimalesDisponibles)
        campoTipoAnimal.setSelectedItem(animalOriginalParam.getTipo());
        cargarAnimalesDisponibles(); // esto ya añade animalOriginal si no aparece
        campoAnimal.setSelectedItem(animalOriginalParam);
    }

    // -------------- CÁLCULOS ----------------
    private void recalcularTotales() {
        try {
            // aceptar comas o puntos
            String cantText = campoCantidad.getText().trim().replace(",", ".");
            String precioText = campoPrecio.getText().trim().replace(",", ".");
            String ivaText = campoIVA.getText().trim().replace(",", ".");
            String retText = campoRetencion.getText().trim().replace(",", ".");

            double cantidad = cantText.isEmpty() ? 0 : Double.parseDouble(cantText);
            double precio = precioText.isEmpty() ? 0 : Double.parseDouble(precioText);
            double iva = ivaText.isEmpty() ? 0 : Double.parseDouble(ivaText);
            double retencion = retText.isEmpty() ? 0 : Double.parseDouble(retText);

            // Si el animal no es PECES o ABEJAS forzamos cantidad = 1 (si está vacío) - **no** escribimos en el campo para evitar mutaciones dentro de listeners
            Animal sel = (Animal) campoAnimal.getSelectedItem();
            if (sel != null && sel.getTipo() != TipoAnimal.PECES && sel.getTipo() != TipoAnimal.ABEJAS) {
                if (campoCantidad.getText().trim().isEmpty()) cantidad = 1;
            }

            double subTotal = cantidad * precio;
            // total = subTotal - IVA% sobre subtotal - retencion (retencion en unidades, no %)
            double total = subTotal - (subTotal * iva/100.0) - retencion;

            campoSubTotal.setText(String.format("%.2f", subTotal));
            campoTotal.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            campoSubTotal.setText("");
            campoTotal.setText("");
        }
    }

    // -------------- VALIDACIONES ----------------
    private void validarFormulario() {
        boolean valido = true;

        // fecha, cliente, animal
        if (campoFecha.getDate() == null) valido = false;
        if (campoCliente.getText().trim().isEmpty()) valido = false;
        if (campoAnimal.getSelectedItem() == null) valido = false;

        // precio requerido y válido
        try {
            String p = campoPrecio.getText().trim().replace(",", ".");
            if (p.isEmpty()) { valido = false; }
            else { Double.parseDouble(p); }
        } catch (NumberFormatException e) { valido = false; }

        // cantidad
        Animal sel = (Animal) campoAnimal.getSelectedItem();
        if (sel != null) {
            if (sel.getTipo() == TipoAnimal.PECES || sel.getTipo() == TipoAnimal.ABEJAS) {
                // validar entero positivo <= stock
                try {
                    String c = campoCantidad.getText().trim().replace(",", ".");
                    if (c.isEmpty()) { valido = false; }
                    else {
                        double d = Double.parseDouble(c);
                        if (d <= 0) valido = false;
                        Integer stock = sel.getCantidad();
                        if (stock == null) stock = 0;
                        if (d > stock) valido = false;
                    }
                } catch (NumberFormatException e) {
                    valido = false;
                }
            } else {
                // otros tipos: cantidad predeterminada = 1, no importa si el campo está vacío
                if (campoCantidad.getText().trim().isEmpty()) {
                    campoCantidad.setText("1"); // predeterminado
                }
            }
        }

        // habilitar/inhabilitar botón guardar
        botonGuardar.setEnabled(valido);
    }

    // -------------- GUARDAR / EDITAR ----------------
    private void guardarTransaccion() {
        // Validamos de nuevo antes de guardar
        validarFormulario();
        if (!botonGuardar.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Complete correctamente los campos obligatorios antes de guardar.");
            return;
        }

        try {
            // leer valores (aceptar comas)
            LocalDate fechaLocal = campoFecha.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            Animal seleccionado = (Animal) campoAnimal.getSelectedItem();
            String cliente = campoCliente.getText().trim();

            String cantidadText = campoCantidad.getText().trim().replace(",", ".");
            BigDecimal cantidadBD = cantidadText.isEmpty() ? BigDecimal.ONE : new BigDecimal(cantidadText);
            BigDecimal precioBD = new BigDecimal(campoPrecio.getText().trim().replace(",", "."));
            BigDecimal ivaBD = campoIVA.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(campoIVA.getText().trim().replace(",", "."));
            BigDecimal retencionBD = campoRetencion.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(campoRetencion.getText().trim().replace(",", "."));
            BigDecimal totalBD = campoTotal.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(campoTotal.getText().trim().replace(",", "."));
            String factura = campoFactura.getText().trim();
            String descripcion = areaDescripcion.getText().trim();

            // Si estamos creando una nueva transacción
            if (transaccionActual == null) {
                Transaccion trans = new Transaccion(
                        seleccionado,
                        factura,
                        totalBD,
                        retencionBD,
                        precioBD,
                        ivaBD,
                        cantidadBD,
                        fechaLocal,
                        cliente,
                        TipoTransaccion.INGRESO,
                        descripcion
                );
                trans.setAnimal(seleccionado);
                TransaccionController.getInstance().guardarTransaccion(trans);

                // ajustar stock/estado del animal seleccionado
                aplicarCambiosAnimalAlGuardar(seleccionado, cantidadBD);
            } else {
                // edición: manejar posible cambio de animal en la transacción
                Animal anterior = transaccionActual.getAnimal();

                // si cambió el animal, revertir efectos en el animal anterior
                if (anterior != null && seleccionado != null && !anterior.getIdAnimal().equals(seleccionado.getIdAnimal())) {
                    revertirCambiosAnimal(anterior, transaccionActual.getCantidad());
                }

                // actualizar la transacción actual
                transaccionActual.setFecha(fechaLocal);
                transaccionActual.setAnimal(seleccionado);
                transaccionActual.setNombreCliente(cliente);
                transaccionActual.setCantidad(cantidadBD);
                transaccionActual.setPrecioUnitario(precioBD);
                transaccionActual.setIva(ivaBD);
                transaccionActual.setRetencion(retencionBD);
                transaccionActual.setTotal(totalBD);
                transaccionActual.setNumeroFactura(factura);
                transaccionActual.setDescripcion(descripcion);

                TransaccionController.getInstance().editarTransaccion(transaccionActual);

                // aplicar efectos al animal seleccionado
                aplicarCambiosAnimalAlGuardar(seleccionado, cantidadBD);
            }

            parentVista.recargarTabla();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Aplica los cambios al animal al guardar una transacción (resta cantidad o marca VENDIDO)
    private void aplicarCambiosAnimalAlGuardar(Animal animal, BigDecimal cantidadBD) {
        try {
            if (animal == null) return;

            if (animal.getTipo() == TipoAnimal.PECES || animal.getTipo() == TipoAnimal.ABEJAS) {
                // animal.cantidad es Integer (unidades)
                int toSubtract = cantidadBD.intValue();
                Integer actual = animal.getCantidad() == null ? 0 : animal.getCantidad();
                int nueva = actual - toSubtract;
                if (nueva <= 0) {
                    animal.setCantidad(0);
                    animal.setEstado(EstadoAnimal.VENDIDO);
                } else {
                    animal.setCantidad(nueva);
                }
                AnimalController.getInstance().editarAnimal(animal);
            } else {
                // animales individuales -> marcar vendido
                animal.setEstado(EstadoAnimal.VENDIDO);
                AnimalController.getInstance().editarAnimal(animal);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Revertir efectos previos (cuando se edita y se cambia el animal)
    private void revertirCambiosAnimal(Animal animal, BigDecimal cantidadPrevBD) {
        try {
            if (animal == null) return;
            // si era PECES/ABEJAS, sumar la cantidad previa
            if (animal.getTipo() == TipoAnimal.PECES || animal.getTipo() == TipoAnimal.ABEJAS) {
                int sumar = cantidadPrevBD == null ? 0 : cantidadPrevBD.intValue();
                Integer actual = animal.getCantidad() == null ? 0 : animal.getCantidad();
                animal.setCantidad(actual + sumar);
                // si estaba VENDIDO y ahora vuelve >0, poner disponible
                if (animal.getCantidad() > 0 && animal.getEstado() == EstadoAnimal.VENDIDO) {
                    animal.setEstado(EstadoAnimal.DISPONIBLE); // ajusta según tu enum
                }
                AnimalController.getInstance().editarAnimal(animal);
            } else {
                // si era un animal marcado como VENDIDO por la transacción, revertimos a disponible
                if (animal.getEstado() == EstadoAnimal.VENDIDO) {
                    animal.setEstado(EstadoAnimal.DISPONIBLE); // ajustar según tu enum
                    AnimalController.getInstance().editarAnimal(animal);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // -------------- METODO PARA CARGAR TRANSACCION A EDITAR ----------------
    public void cargarTransaccion(Transaccion t) {
        this.transaccionActual = t;
        if (t == null) return;

        // suspendemos listeners mientras rellenamos (evita IllegalStateException)
        suspendEvents = true;

        try {
            if (t.getFecha() != null) campoFecha.setDate(java.sql.Date.valueOf(t.getFecha()));

            if (t.getAnimal() != null) {
                // cargar animales para edición (incluye el animal original aunque esté vendido)
                cargarAnimalesParaEdicion(t.getAnimal());
            } else {
                // si no hay animal (por seguridad), refrescar disponibles
                cargarAnimalesDisponibles();
            }

            campoCliente.setText(t.getNombreCliente() == null ? "" : t.getNombreCliente());
            campoCantidad.setText(t.getCantidad() == null ? "" : t.getCantidad().toString());
            campoPrecio.setText(t.getPrecioUnitario() == null ? "" : t.getPrecioUnitario().toString());
            campoIVA.setText(t.getIva() == null ? "0" : t.getIva().toString());
            campoRetencion.setText(t.getRetencion() == null ? "0" : t.getRetencion().toString());

            // subtotal: cantidad * precio (presentación)
            if (t.getCantidad() != null && t.getPrecioUnitario() != null) {
                double sub = t.getCantidad().multiply(t.getPrecioUnitario()).doubleValue();
                campoSubTotal.setText(String.format("%.2f", sub));
            } else {
                campoSubTotal.setText("");
            }

            campoTotal.setText(t.getTotal() == null ? "" : t.getTotal().toString());
            campoFactura.setText(t.getNumeroFactura() == null ? "" : t.getNumeroFactura());
            areaDescripcion.setText(t.getDescripcion() == null ? "" : t.getDescripcion());

            // mantener referencia al animal original para decisiones al guardar
            animalOriginal = t.getAnimal();
        } finally {
            // reactivar y forzar un cálculo/validación en el EDT después de terminar setText
            suspendEvents = false;
            SwingUtilities.invokeLater(() -> {
                recalcularTotales();
                validarFormulario();
            });
        }
    }
}
