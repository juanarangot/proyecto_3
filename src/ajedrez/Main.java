package src.ajedrez;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal para ejecutar la aplicación
 */
public class Main {

    public static void main(String[] args) {
        try {
            // Intentar usar el look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo establecer el look and feel del sistema: " + e.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VisualizadorArbol visualizador = new VisualizadorArbol();
                visualizador.setVisible(true);
            }
        });
    }
}
