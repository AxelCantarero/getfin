package org.getfin.vistas.productos;
import com.github.lgooddatepicker.components.DatePicker;
import org.getfin.modelos.Producto;
import org.getfin.modelos.enums.CategoriaProducto;
import org.getfin.modelos.enums.EstadoProducto;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FormularioProducto extends JDialog {

    /** Las variable privada y con su campo de accion */
    private final JTextField txtNombre = crearCampo();
    private final JTextField txtDescripcion = crearCampo();
    private final JTextField txtUnidadMedida = crearCampo();

    /** Esta variable utiliza combo para el menucito despegable */
    private final JComboBox<CategoriaProducto> comboCategoria = new JComboBox<>(CategoriaProducto.values());

    /** En esta variable fecha se utiliza el DatePicker para uel claendario mas modrno */
    private final DatePicker chooserFechaCaducidad = new DatePicker();

    private final JTextField txtStock = crearCampo();
    private final JTextField txtMontoInicial = crearCampo();
    private final JComboBox<EstadoProducto> comboEstado = new JComboBox<>(EstadoProducto.values());

    /** Acemos las accion de la ProductoVista y editarc*/
    private final ProductoVista vista;
    private final Producto productoEditar;

    /** Aqui se hace la acion en agregar el producto y de editar el producto a clase base o super clase */
    public FormularioProducto(JFrame parent, ProductoVista vista, Producto productoEditar) {
        super(parent, productoEditar == null ? "Agregar Producto" : "Editar Producto", true);
        this.vista = vista;
        this.productoEditar = productoEditar;

        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        comboCategoria.setSelectedItem(CategoriaProducto.INSUMOS);
        comboEstado.setSelectedItem(EstadoProducto.DISPONIBLE);

        JPanel camposPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        camposPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        camposPanel.setBackground(Color.WHITE);

        /** Aqui se crea y se agregan los elemento en forma visual en el fomulario */
        camposPanel.add(new JLabel("Nombre:"));
        camposPanel.add(txtNombre);
        camposPanel.add(new JLabel("Descripción:"));
        camposPanel.add(txtDescripcion);
        camposPanel.add(new JLabel("Unidad de Medida:"));
        camposPanel.add(txtUnidadMedida);
        camposPanel.add(new JLabel("Categoría:"));
        camposPanel.add(comboCategoria);

        camposPanel.add(new JLabel("Fecha de Caducidad:"));

        camposPanel.add(chooserFechaCaducidad);

        camposPanel.add(new JLabel("Stock:"));
        camposPanel.add(txtStock);
        camposPanel.add(new JLabel("Monto Inicial:"));
        camposPanel.add(txtMontoInicial);
        camposPanel.add(new JLabel("Estado:"));
        camposPanel.add(comboEstado);

        /** En esta parte es donde se crea la edicion de todas las variable o se nula */
        if (productoEditar != null) {
            txtNombre.setText(productoEditar.getNombreProducto());
            txtDescripcion.setText(productoEditar.getDescripcion());
            txtUnidadMedida.setText(productoEditar.getUnidadMedida());
            comboCategoria.setSelectedItem(productoEditar.getCategoria());

            chooserFechaCaducidad.setDate(productoEditar.getFechaCaducidad());

            txtStock.setText(productoEditar.getStock() != null ? productoEditar.getStock().toString() : "");
            txtMontoInicial.setText(productoEditar.getMontoInicial() != null ? productoEditar.getMontoInicial().toString() : "");
            comboEstado.setSelectedItem(productoEditar.getEstado());
        }

        /** Aqui le doy color al boton guardar y cancelarv */
        JButton btnGuardar = crearBoton("Guardar", new Color(33, 150, 243));
        JButton btnCancelar = crearBoton("Cancelar", Color.GRAY);

        /** Aqui voy hacer la accion de los dos botones para que guarde y cancele a como queremos */
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty() || txtDescripcion.getText().trim().isEmpty() || txtUnidadMedida.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa los campos obligatorios.");
                return;
            }

            LocalDate fechaCaducidad = chooserFechaCaducidad.getDate();

            /** Aqui utiliamos solo la variable en decimal como la de stoc y montofinal */
            BigDecimal stock = txtStock.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtStock.getText());
            BigDecimal montoInicial = txtMontoInicial.getText().trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtMontoInicial.getText());

            /** Y aqui se utiliza el trim para la eliminacion de los espacion en blanco */
            Producto p = productoEditar != null ? productoEditar : new Producto();
            p.setNombreProducto(txtNombre.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setUnidadMedida(txtUnidadMedida.getText().trim());
            p.setCategoria((CategoriaProducto) comboCategoria.getSelectedItem());  /** SelectedItem devuelve el objeto que esta selecionado */
            p.setFechaCaducidad(fechaCaducidad); /** Aqui le asignamos el LocalDate o fecha directamente */
            p.setStock(stock);
            p.setMontoInicial(montoInicial);
            p.setEstado((EstadoProducto) comboEstado.getSelectedItem());

            vista.guardarOActualizar(p);
            dispose();
        });

        /** Wsra es la perte del panel en los botones y con su colores */
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