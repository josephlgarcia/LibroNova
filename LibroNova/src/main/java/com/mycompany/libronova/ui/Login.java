/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.ui;

import com.mycompany.libronova.domain.User;
import com.mycompany.libronova.exception.AuthenticationException;
import com.mycompany.libronova.infra.config.ApplicationContext;
import com.mycompany.libronova.service.UserService;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Coder
 */
public class Login extends JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());
    
    private final UserService userService;
    
    // Componentes de la UI
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public Login() {
        this.userService = ApplicationContext.getInstance().getUserService();
        initComponents();
        setupListeners();
    }

    private void initComponents() {
        setTitle("LibroNova - Sistema de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Título
        JLabel titleLabel = new JLabel("LibroNova");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        mainPanel.add(Box.createVerticalStrut(10));
        
        JLabel subtitleLabel = new JLabel("Sistema de Gestión de Biblioteca");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Panel de login
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(120, 30));
        loginPanel.add(loginButton, gbc);
        
        mainPanel.add(loginPanel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setForeground(Color.RED);
        mainPanel.add(statusLabel);
        
        // Panel de ejemplo
        mainPanel.add(Box.createVerticalStrut(20));
        JPanel examplePanel = new JPanel();
        examplePanel.setBorder(BorderFactory.createTitledBorder("Usuarios de ejemplo:"));
        examplePanel.setLayout(new BoxLayout(examplePanel, BoxLayout.Y_AXIS));
        
        JLabel adminExample = new JLabel("Admin: admin / admin123");
        adminExample.setFont(new Font("Arial", Font.PLAIN, 12));
        examplePanel.add(adminExample);
        
        JLabel userExample = new JLabel("Usuario: juan.perez / password123");
        userExample.setFont(new Font("Arial", Font.PLAIN, 12));
        examplePanel.add(userExample);
        
        mainPanel.add(examplePanel);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        // Enter key en los campos
        usernameField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty()) {
            showStatus("Por favor ingrese su nombre de usuario", true);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showStatus("Por favor ingrese su contraseña", true);
            passwordField.requestFocus();
            return;
        }
        
        // Deshabilitar botón durante la autenticación
        loginButton.setEnabled(false);
        showStatus("Autenticando...", false);
        
        SwingUtilities.invokeLater(() -> {
            try {
                User user = userService.authenticate(username, password);
                
                // Login exitoso
                showStatus("Login exitoso! Redirigiendo...", false);
                
                SwingUtilities.invokeLater(() -> {
                    this.setVisible(false);
                    
                    if (user.isAdmin()) {
                        new DashboardAdmin(user).setVisible(true);
                    } else {
                        new DashboardUser(user).setVisible(true);
                    }
                    
                    this.dispose();
                });
                
            } catch (AuthenticationException ex) {
                showStatus(ex.getMessage(), true);
                loginButton.setEnabled(true);
                passwordField.setText("");
                passwordField.requestFocus();
            } catch (Exception ex) {
                showStatus("Error de conexión: " + ex.getMessage(), true);
                loginButton.setEnabled(true);
                logger.severe("Error during login: " + ex.getMessage());
            }
        });
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : Color.BLUE);
    }
    
}
