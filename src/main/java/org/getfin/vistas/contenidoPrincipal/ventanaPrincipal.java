package org.getfin.vistas.contenidoPrincipal;

import org.getfin.vistas.Dashboard.dashboardVista;
import org.getfin.vistas.Ingresos.ingresoVista;
import org.getfin.vistas.componentes.menuOpciones;

import javax.swing.*;
import java.awt.*;

public class ventanaPrincipal extends JFrame {

    public ventanaPrincipal(){
        setTitle("Ventana Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Aquí puedes agregar más componentes a la ventana principal

        menuOpciones menu = new menuOpciones();
        menu.setPreferredSize(new Dimension(150,600));
        add(menu, BorderLayout.WEST);

        //
         /*
        dashboardVista dashboard = new dashboardVista();
        add(dashboard, BorderLayout.CENTER);
    */

        ingresoVista ingreso = new ingresoVista();
        add(ingreso, BorderLayout.CENTER);

        pack();

    }
}
