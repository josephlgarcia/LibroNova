/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.libronova;

import com.mycompany.libronova.ui.Login;

/**
 *
 * @author Coder
 */
public class LibroNova {

    public static void main(String[] args) {
        
        try {
            // Configurar Look and Feel
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicializar aplicación
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
        
    }
}
