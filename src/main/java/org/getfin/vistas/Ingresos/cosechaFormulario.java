package org.getfin.vistas.Ingresos;

import org.getfin.controlador.CultivoController;
import org.getfin.controlador.TransaccionController;
import org.getfin.modelos.Cultivo;
import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.CategoriaCultivo;
import org.getfin.modelos.enums.TipoTransaccion;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class cosechaFormulario extends JFrame {

    private final ingresoVista parentVista;
    private List<Cultivo> listaCultivos;

    private JDateChooser campoFecha;
    private JComboBox<CategoriaCultivo> campoCategoria;
    private JComboBox<Cultivo> campoCultivo;
    private JTextField campoCliente, campoCantidad, campoPrecio, campoIVA, campoRetencion, campoTotal, campoFactura, campoSubTotal;
    private JTextArea areaDescripcion;

    private Transaccion transaccionActual = null;

    public cosechaFormulario(JFrame parent, ingresoVista vista) {
        super("Formulario Cosecha");
        this.parentVista = vista;
        initFormulario();
    }

    private void initFormulario() {
        setSize(750, 500);
        setLocationRelativeTo(null);
        setTitle("Venta de Cosecha - Formulario");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        JPanel panelIzq = new JPanel(new GridLayout(7,2,5,5));
        panelIzq.setBorder(BorderFactory.createTitledBorder("Datos principales"));

        panelIzq.add(new JLabel("Fecha:"));
        campoFecha = new JDateChooser();
        panelIzq.add(campoFecha);

        panelIzq.add(new JLabel("Categoría:"));
        campoCategoria = new JComboBox<>(CategoriaCultivo.values());
        panelIzq.add(campoCategoria);

        CultivoController cultivoController = new CultivoController();
        listaCultivos = cultivoController.getCultivo();

        panelIzq.add(new JLabel("Cultivo:"));
        campoCultivo = new JComboBox<>();
        panelIzq.add(campoCultivo);

        campoCultivo.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,Object value,int index,boolean isSelected,boolean cellHasFocus){
                super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                if(value instanceof Cultivo c) setText(c.getNombreCultivo());
                else setText("");
                return this;
            }
        });

        // FILTRAR cultivos por categoria y stock > 0
        campoCategoria.addActionListener(e -> filtrarCultivos());

        // Inicializar cultivos de la primera categoría
        if(campoCategoria.getItemCount() > 0){
            campoCategoria.setSelectedIndex(0);
            filtrarCultivos();
        }

        panelIzq.add(new JLabel("Cliente:"));
        campoCliente = new JTextField();
        panelIzq.add(campoCliente);

        panelIzq.add(new JLabel("Cantidad:"));
        campoCantidad = new JTextField();
        panelIzq.add(campoCantidad);

        panelIzq.add(new JLabel("Precio Unitario:"));
        campoPrecio = new JTextField();
        panelIzq.add(campoPrecio);

        panelIzq.add(new JLabel("IVA (%):"));
        campoIVA = new JTextField();
        panelIzq.add(campoIVA);

        JPanel panelDer = new JPanel(new BorderLayout());
        panelDer.setBorder(BorderFactory.createTitledBorder("Totales y descripción"));

        JPanel panelTotales = new JPanel(new GridLayout(4,2,5,5));
        panelTotales.add(new JLabel("Retención:"));
        campoRetencion = new JTextField();
        panelTotales.add(campoRetencion);

        panelTotales.add(new JLabel("Sub Total:"));
        campoSubTotal = new JTextField();
        campoSubTotal.setEditable(false);
        panelTotales.add(campoSubTotal);

        panelTotales.add(new JLabel("Total:"));
        campoTotal = new JTextField();
        campoTotal.setEditable(false);
        panelTotales.add(campoTotal);

        panelTotales.add(new JLabel("Número de Factura:"));
        campoFactura = new JTextField();
        panelTotales.add(campoFactura);

        areaDescripcion = new JTextArea(5,20);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDescripcion);

        JPanel panelDesc = new JPanel(new BorderLayout());
        panelDesc.setBorder(BorderFactory.createTitledBorder("Descripción"));
        panelDesc.add(scroll, BorderLayout.CENTER);

        panelDer.add(panelTotales, BorderLayout.NORTH);
        panelDer.add(panelDesc, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0.5; gbc.weighty=1;
        mainPanel.add(panelIzq, gbc);
        gbc.gridx=1; gbc.gridy=0; gbc.weightx=0.5; gbc.weighty=1;
        mainPanel.add(panelDer, gbc);
        gbc.gridx=0; gbc.gridy=1; gbc.gridwidth=2; gbc.weighty=0;
        mainPanel.add(panelBotones, gbc);

        add(mainPanel);

        // Listeners
        botonCancelar.addActionListener(e->dispose());
        botonGuardar.addActionListener(e -> guardarTransaccion());

        KeyAdapter recalcular = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                recalcularTotales();
            }
        };
        campoCantidad.addKeyListener(recalcular);
        campoPrecio.addKeyListener(recalcular);
        campoIVA.addKeyListener(recalcular);
        campoRetencion.addKeyListener(recalcular);

        // Inicializar números a 0
        campoIVA.setText("0");
        campoRetencion.setText("0");

        setVisible(true);
    }

    private void filtrarCultivos() {
        CategoriaCultivo cat = (CategoriaCultivo) campoCategoria.getSelectedItem();
        Cultivo seleccionado = (Cultivo) campoCultivo.getSelectedItem();
        campoCultivo.removeAllItems();
        if (listaCultivos != null) {
            for(Cultivo c : listaCultivos){
                BigDecimal stock = c.getStock();
                if (c.getCategoria() == cat && stock != null && stock.compareTo(BigDecimal.ZERO) > 0) {
                    campoCultivo.addItem(c);
                }
            }
        }
        // Mantener seleccionado anterior si aún está en la lista
        if (seleccionado != null) campoCultivo.setSelectedItem(seleccionado);
    }

    private void guardarTransaccion() {
        Date fecha = campoFecha.getDate();
        CategoriaCultivo categoria = (CategoriaCultivo) campoCategoria.getSelectedItem();
        Cultivo cultivo = (Cultivo) campoCultivo.getSelectedItem();
        String cliente = campoCliente.getText();

        String cantidadText = campoCantidad.getText().replace(",", ".").trim();
        String precioText = campoPrecio.getText().replace(",", ".").trim();
        String ivaText = campoIVA.getText().replace(",", ".").trim();
        String retText = campoRetencion.getText().replace(",", ".").trim();
        String totalText = campoTotal.getText().replace(",", ".").trim();

        if (fecha == null || categoria == null || cultivo == null ||
                cliente.isEmpty() || cantidadText.isEmpty() || precioText.isEmpty() || ivaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal cantidad, precio, iva, retencion, total;

        try {
            cantidad = new BigDecimal(cantidadText);
            precio = new BigDecimal(precioText);
            iva = new BigDecimal(ivaText);
            retencion = retText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(retText);
            total = totalText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato numérico inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal stock = cultivo.getStock();
        if (stock == null) {
            JOptionPane.showMessageDialog(this, "El cultivo seleccionado no tiene stock registrado.",
                    "Error de stock", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación cantidad máxima según stock
        BigDecimal cantidadMax = stock;
        if (transaccionActual != null) {
            // En edición, agregar cantidad previamente usada
            cantidadMax = stock.add(transaccionActual.getCantidad());
        }
        if (cantidad.compareTo(cantidadMax) > 0) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad ingresada (" + cantidad + ") supera el stock disponible (" + cantidadMax + ").",
                    "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fechaLocal = fecha.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        if(transaccionActual == null){
            Transaccion trans = new Transaccion(TipoTransaccion.INGRESO, "", cliente, cultivo, campoFactura.getText(),
                    total, retencion, cantidad, fechaLocal, precio, iva);
            TransaccionController.getInstance().guardarTransaccion(trans);

            // Restar stock
            modificarCultivo(cultivo, cantidad);
        } else {
            // Ajuste diferencial de stock
            BigDecimal cantidadAnterior = transaccionActual.getCantidad();
            if (!cantidad.equals(cantidadAnterior)) {
                BigDecimal diferencia = cantidad.subtract(cantidadAnterior);
                modificarCultivo(cultivo, diferencia);
            }

            transaccionActual.setFecha(fechaLocal);
            transaccionActual.setCultivo(cultivo);
            transaccionActual.setNombreCliente(cliente);
            transaccionActual.setCantidad(cantidad);
            transaccionActual.setPrecioUnitario(precio);
            transaccionActual.setIva(iva);
            transaccionActual.setRetencion(retencion);
            transaccionActual.setTotal(total);
            transaccionActual.setNumeroFactura(campoFactura.getText());
            transaccionActual.setDescripcion(areaDescripcion.getText());
            TransaccionController.getInstance().editarTransaccion(transaccionActual);
        }

        if (parentVista != null) parentVista.recargarTabla();
        dispose();
    }

    public void cargarTransaccion(Transaccion t){
        this.transaccionActual = t;
        campoFecha.setDate(java.sql.Date.valueOf(t.getFecha()));
        campoCategoria.setSelectedItem(t.getCultivo().getCategoria());

        // Asegurarse que el combo tenga ese cultivo
        boolean encontrado = false;
        for (int i = 0; i < campoCultivo.getItemCount(); i++) {
            Cultivo item = campoCultivo.getItemAt(i);
            if (item != null && item.getIdCultivo().equals(t.getCultivo().getIdCultivo())) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) campoCultivo.addItem(t.getCultivo());

        campoCultivo.setSelectedItem(t.getCultivo());
        campoCliente.setText(t.getNombreCliente());
        campoCantidad.setText(t.getCantidad().toString());
        campoPrecio.setText(t.getPrecioUnitario().toString());
        campoIVA.setText(t.getIva().toString());
        campoRetencion.setText(t.getRetencion().toString());
        campoTotal.setText(t.getTotal().toString());
        campoFactura.setText(t.getNumeroFactura());
        areaDescripcion.setText(t.getDescripcion());
    }

    private void recalcularTotales() {
        try {
            String cantidadText = campoCantidad.getText().replace(",", ".").trim();
            String precioText = campoPrecio.getText().replace(",", ".").trim();
            String ivaText = campoIVA.getText().replace(",", ".").trim();
            String retText = campoRetencion.getText().replace(",", ".").trim();

            double cantidad = cantidadText.isEmpty() ? 0 : Double.parseDouble(cantidadText);
            double precio = precioText.isEmpty() ? 0 : Double.parseDouble(precioText);
            double iva = ivaText.isEmpty() ? 0 : Double.parseDouble(ivaText);
            double retencion = retText.isEmpty() ? 0 : Double.parseDouble(retText);

            double subTotal = cantidad * precio;
            double total = subTotal - (subTotal * iva / 100.0) - retencion;

            campoSubTotal.setText(String.format("%.2f", subTotal));
            campoTotal.setText(String.format("%.2f", total));
        } catch (NumberFormatException ex) {
            campoSubTotal.setText("");
            campoTotal.setText("");
        }
    }

    private void modificarCultivo(Cultivo c, BigDecimal cantidad) {
        if (c == null || cantidad == null) return;

        BigDecimal stockActual = c.getStock();
        if (stockActual == null) stockActual = BigDecimal.ZERO;

        BigDecimal nuevo = stockActual.subtract(cantidad);
        if (nuevo.compareTo(BigDecimal.ZERO) < 0) nuevo = BigDecimal.ZERO;

        c.setStock(nuevo);
        CultivoController.getInstance().editarCultivo(c);
    }

}
