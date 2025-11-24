package org.getfin.vistas.productos;

import org.getfin.controlador.ProductoController;
import org.getfin.modelos.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Aqui la clase se crea como un panel */
public class ProductoVista extends JPanel {
    /** Aqui usamos private final por una ves asignada lla no se puede cambiar */
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final ProductoController controller = ProductoController.getInstance();

    private final JButton btnAgregar = crearBoton("Agregar", new Color(50, 150, 50));
    private final JButton btnEditar = crearBoton("Editar", new Color(33, 150, 243));
    private final JButton btnEliminar = crearBoton("Eliminar", new Color(244, 67, 54));

    /** Aqui viene la creacion del campo con las tablas para cada variables */
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Descripción", "Unidad", "Categoría", "Fecha Caducidad", "Stock", "Monto Inicial", "Estado"}, 0) {
        /** Aqui con el @Ovarride indica que estamos subrecriviendo el metodo */
        @Override
        public boolean isCellEditable(int row, int column) { /** Row este contiene datos en una filas */
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Long.class;
            return super.getColumnClass(columnIndex);
        }
    };

    private final JTable tabla = new JTable(model);

    public ProductoVista() {
        super(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Gestión de Productos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        headerPanel.add(btnAgregar, BorderLayout.LINE_END);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        botonesPanel.setBackground(Color.WHITE);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        mainPanel.add(botonesPanel, BorderLayout.SOUTH);

        add(mainPanel);

        configurarTabla();
        asignarEventos();
        recargarTabla();
    }

    private void configurarTabla() {
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(230, 230, 230));
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permite seleccionar solo una fila a la vez.
    }

    private void asignarEventos() {
        btnAgregar.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new FormularioProducto(parent, this, null).setVisible(true);
        });

        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para editar", "Editar", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Long id = (Long) model.getValueAt(fila, 0);
            Producto producto = controller.getProducto().stream()
                    .filter(p -> p.getIdProducto().equals(id))
                    .findFirst()
                    .orElse(null);
            if (producto == null) return;
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new FormularioProducto(parent, this, producto).setVisible(true);
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Long id = (Long) model.getValueAt(fila, 0);
            Producto producto = controller.getProducto().stream()
                    .filter(p -> p.getIdProducto().equals(id))
                    .findFirst()
                    .orElse(null);
            if (producto == null) return;

            /** Este pide cuadro de dialogo y lo confimamos */
            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar '" + producto.getNombreProducto() + "'?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminarProducto(producto);
                    recargarTabla();
                    JOptionPane.showMessageDialog(this, "Producto eliminado con éxito.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void guardarOActualizar(Producto producto) {
        try {
            if (producto.getIdProducto() == null) {
                controller.guardarProducto(producto);
            } else {
                controller.editarProducto(producto);
            }
            recargarTabla();
            JOptionPane.showMessageDialog(this, "Producto guardado con éxito.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recargarTabla() {
        model.setRowCount(0);
        try {
            List<Producto> lista = controller.getProducto();
            for (Producto p : lista) {
                model.addRow(new Object[]{
                        p.getIdProducto(),
                        p.getNombreProducto(),
                        p.getDescripcion(),
                        p.getUnidadMedida(),
                        p.getCategoria(),
                        p.getFechaCaducidad() != null ? p.getFechaCaducidad().format(FMT) : "",
                        p.getStock(),
                        p.getMontoInicial(),
                        p.getEstado()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los productos. Revise la consola.", "Error de Carga", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static JButton crearBoton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(120, 35));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Pequeño detalle visual
        return b;
    }
}