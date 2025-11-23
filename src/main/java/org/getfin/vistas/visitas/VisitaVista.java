package org.getfin.vistas.visitas;

import org.getfin.controlador.VisitaController;
import org.getfin.modelos.Visita;
import org.getfin.vistas.visitas.FormularioVisita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VisitaVista extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final VisitaController controller = VisitaController.getInstance();

    private final JButton btnAgregar = crearBoton("Agregar", new Color(50, 150, 50));
    private final JButton btnEditar = crearBoton("Editar", new Color(33, 150, 243));
    private final JButton btnEliminar = crearBoton("Eliminar", new Color(244, 67, 54));

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre Responsable", "Carrera", "Total", "Mujeres", "Hombres", "Fecha"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
        @Override public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Long.class; // El ID es Long
            return super.getColumnClass(columnIndex);
        }
    };
    private final JTable tabla = new JTable(modelo);

    public VisitaVista() {
        super(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Gestión de Visitas");
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
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void asignarEventos() {
        btnAgregar.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new FormularioVisita(parent, this, null).setVisible(true);
        });

        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para editar", "Editar", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Long id = (Long) modelo.getValueAt(fila, 0);
            Visita v = controller.getVisita()
                    .stream()
                    .filter(vis -> vis.getIdVisita().equals(id))
                    .findFirst()
                    .orElse(null);
            if (v == null) return;
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new FormularioVisita(parent, this, v).setVisible(true);
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Long id = (Long) modelo.getValueAt(fila, 0);
            Visita v = controller.getVisita()
                    .stream()
                    .filter(vis -> vis.getIdVisita().equals(id))
                    .findFirst()
                    .orElse(null);
            if (v == null) return;

            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar la visita de '" + v.getNombreResponsable() + "'?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminarVisita(v);
                    recargarTabla();
                    JOptionPane.showMessageDialog(this, "Visita eliminada con éxito.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void guardarOActualizar(Visita v) {
        try {
            if (v.getIdVisita() == null) {
                controller.guardarVisita(v);
            } else {
                controller.editarVisita(v);
            }
            recargarTabla();
            JOptionPane.showMessageDialog(this, "Visita guardada con éxito.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recargarTabla() {
        modelo.setRowCount(0);
        try {
            List<Visita> lista = controller.getVisita();
            for (Visita v : lista) {

                modelo.addRow(new Object[]{
                        v.getIdVisita(),
                        v.getNombreResponsable(),
                        v.getCarrera(),
                        v.getTotal(),
                        v.getMujeres(),
                        v.getHombres(),
                        v.getFecha() != null ? v.getFecha().format(FMT) : "N/A"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las visitas. Revise la consola.", "Error de Carga", JOptionPane.ERROR_MESSAGE);
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