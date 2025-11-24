package org.getfin.vistas.cultivos;
import org.getfin.controlador.CultivoController;
import org.getfin.modelos.Cultivo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CultivoVista extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final CultivoController controller = CultivoController.getInstance();

    private final JButton btnAgregar = crearBoton("Agregar", new Color(50, 150, 50));
    private final JButton btnEditar = crearBoton("Editar", new Color(33, 150, 243));
    private final JButton btnEliminar = crearBoton("Eliminar", new Color(244, 67, 54));

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nombre de Cultivo", "Fecha Siembra", "Area Sembrada", "Categoria", "Estado", "Stock", "Monto Inicial", "Fecha Cosecha"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Long.class;
            return super.getColumnClass(columnIndex);
        }
    };

    private final JTable tabla = new JTable(model);

    public CultivoVista() {
        super(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Gestión de Cultivo");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        headerPanel.add(btnAgregar, BorderLayout.LINE_END);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        botonesPanel.setBackground(Color.WHITE);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);

        // 3. ENSAMBLAJE DEL PANEL PRINCIPAL
        mainPanel.add(headerPanel, BorderLayout.NORTH);      // Cabecera arriba
        mainPanel.add(new JScrollPane(tabla), BorderLayout.CENTER); // Tabla en el centro (con scroll)
        mainPanel.add(botonesPanel, BorderLayout.SOUTH);     // Botones abajo

        add(mainPanel); // Añadir el panel principal al JPanel contenedor

        // --- INICIALIZACIÓN ---
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
            new FormularioCultivo(parent, this, null).setVisible(true);
        });

        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para editar", "Editar", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Long id = (Long) model.getValueAt(fila, 0);
            Cultivo cultivo = controller.getCultivo().stream()
                    .filter(cultivo1 -> cultivo1.getIdCultivo().equals(id))
                    .findFirst()
                    .orElse(null);
            if (cultivo == null) return;
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new FormularioCultivo(parent, this, cultivo).setVisible(true);
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Long id = (Long) model.getValueAt(fila, 0);
            Cultivo cultivo = controller.getCultivo().stream()
                    .filter(cultivo1 -> cultivo1.getIdCultivo().equals(id))
                    .findFirst()
                    .orElse(null);
            if (cultivo == null) return;

            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar '" + cultivo.getNombreCultivo() + "'?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminarCultivo(cultivo);
                    recargarTabla();
                    JOptionPane.showMessageDialog(this, "Producto eliminado con éxito.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void guardarOActualizar(Cultivo cultivo) {
        try {
            if (cultivo.getIdCultivo() == null) {
                controller.guardarCultivo(cultivo);
            } else {
                controller.editarCultivo(cultivo);
            }
            recargarTabla();
            JOptionPane.showMessageDialog(this, "Cultivo guardado con éxito.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void recargarTabla() {
        model.setRowCount(0);
        try {
            List<Cultivo> lista = controller.getCultivo();
            for (Cultivo cultivo : lista) {
                model.addRow(new Object[]{
                        cultivo.getIdCultivo(),
                        cultivo.getNombreCultivo(),
                        cultivo.getFechaSiembra() != null ? cultivo.getFechaSiembra().format(FMT) : "",
                        cultivo.getAreaSembrada(),
                        cultivo.getCategoria(),
                        cultivo.getEstado(),
                        cultivo.getStock(),
                        cultivo.getMontoInicial(),
                        cultivo.getFechaCosecha() != null ? cultivo.getFechaCosecha().format(FMT) : "",
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
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}