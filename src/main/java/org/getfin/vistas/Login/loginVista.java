package org.getfin.vistas.Login;
import org.getfin.controlador.UsuarioController;
import org.getfin.modelos.Usuario;
import org.getfin.vistas.contenidoPrincipal.ventanaPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class loginVista extends JFrame  {

    public loginVista() {

        mostrar();
    }

    public void mostrar() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Login");
        getContentPane().setBackground(new Color(83,102,119));

        // FlowLayout para centrar el panel
        setLayout(new FlowLayout(FlowLayout.CENTER,0,40));

        // Panel central cuadrado
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(new Color(255,255,252));
        panelCentro.setPreferredSize(new Dimension(550, 450));
        add(panelCentro);

        // Franja superior verde
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(new Color(12,55,15));
        panelNorte.setPreferredSize(new Dimension(500, 30));
        panelCentro.add(panelNorte, BorderLayout.NORTH);

        // Franja inferior verde
        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(12,55,15));
        panelSur.setPreferredSize(new Dimension(500, 30));
        panelCentro.add(panelSur, BorderLayout.SOUTH);

        // Panel central donde irá el título y formulario
        JPanel Centro = new JPanel();
        Centro.setBackground(new Color(255,255,252));
        Centro.setLayout(new BoxLayout(Centro, BoxLayout.Y_AXIS));
        Centro.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        panelCentro.add(Centro, BorderLayout.CENTER);

        // Título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(new Color(255,255,252));
        JLabel labelTitulo = new JLabel("Iniciar Sesión");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(new Color(83,102,119));
        panelTitulo.add(labelTitulo);
        Centro.add(panelTitulo);

        Centro.add(Box.createRigidArea(new Dimension(0, 0))); // espacio

        // Formulario
        JPanel formulario = new JPanel(new GridLayout(3,2,0,40));
        formulario.setBackground(new Color(255,255,252));

        JLabel labelUsuario = new JLabel("Usuario:");
        JTextField textUsuario = new JTextField();

        JLabel labelContrasena = new JLabel("Contraseña:");
        JPasswordField textContrasena = new JPasswordField();

        JButton botonLogin = new JButton("Iniciar Sesión");
        botonLogin.setBackground(new Color(83,102,119));
        botonLogin.setForeground(new Color(255,255,252));


        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.setBackground(new Color(83,102,119));
        botonCancelar.setForeground(new Color(255,255,252));

        formulario.add(labelUsuario);
        formulario.add(textUsuario);
        formulario.add(labelContrasena);
        formulario.add(textContrasena);
        formulario.add(botonLogin);
        formulario.add(botonCancelar);


        Centro.add(formulario);
        setVisible(true);

        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Usuario> usuarios = UsuarioController.getInstance().getClientes();
                for (Usuario usuario : usuarios) {
                    if(textUsuario.getText().equals(usuario.getNombreUsuario()) && textContrasena.getText().equals(usuario.getContrasena())){
                        new ventanaPrincipal();
                        dispose();
                    }
                }

            }
        });
        botonCancelar.addActionListener(e->dispose());
    }



}
