package src.ajedrez;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;

/**
 * Visualizador de árbol binario para juegos de ajedrez - VERSIÓN FINAL CORREGIDA
 */
public class VisualizadorArbol extends JFrame {
    private JTextArea txtPartida;
    private JButton btnAnalizar;
    private JButton btnVisualizarArbol;
    private JButton btnEjemplo;
    private JPanel panelArbol;
    private JTextArea txtErrores;

    private ChessGameParser parser;
    private ArbolBinario arbol;

    public VisualizadorArbol() {
        parser = new ChessGameParser();
        setTitle("Analizador Sintáctico de Partidas de Ajedrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Panel Principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Entrada de Partida"));

        txtPartida = new JTextArea(10, 40);
        txtPartida.setLineWrap(true);
        txtPartida.setWrapStyleWord(true);
        txtPartida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPartida = new JScrollPane(txtPartida);

        JLabel labelInstrucciones = new JLabel("Ingrese la partida en notación algebraica estándar (SAN):");
        labelInstrucciones.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        inputPanel.add(labelInstrucciones, BorderLayout.NORTH);
        inputPanel.add(scrollPartida, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAnalizar = new JButton("Analizar Partida");
        btnVisualizarArbol = new JButton("Visualizar Árbol");
        btnEjemplo = new JButton("Cargar Ejemplo");

        btnVisualizarArbol.setEnabled(false);

        buttonPanel.add(btnAnalizar);
        buttonPanel.add(btnVisualizarArbol);
        buttonPanel.add(btnEjemplo);

        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Panel de errores
        JPanel errorPanel = new JPanel(new BorderLayout(5, 5));
        errorPanel.setBorder(BorderFactory.createTitledBorder("Resultados del Análisis"));

        txtErrores = new JTextArea(5, 40);
        txtErrores.setEditable(false);
        txtErrores.setLineWrap(true);
        txtErrores.setWrapStyleWord(true);
        txtErrores.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtErrores.setForeground(Color.RED);

        JScrollPane scrollErrores = new JScrollPane(txtErrores);
        errorPanel.add(scrollErrores, BorderLayout.CENTER);

        // Panel superior combinado
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(errorPanel, BorderLayout.SOUTH);

        // Panel del árbol
        panelArbol = new JPanel();
        panelArbol.setLayout(new BorderLayout());
        panelArbol.setBorder(BorderFactory.createTitledBorder("Visualización del Árbol"));

        // Añadir paneles al panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(panelArbol, BorderLayout.CENTER);

        // Añadir eventos
        btnAnalizar.addActionListener(e -> analizarPartida());
        btnVisualizarArbol.addActionListener(e -> visualizarArbol());
        btnEjemplo.addActionListener(e -> cargarEjemplo());

        // Añadir panel principal al frame
        getContentPane().add(mainPanel);
    }

    private void analizarPartida() {
        String partidaSAN = txtPartida.getText().trim();

        if (partidaSAN.isEmpty()) {
            txtErrores.setText("Debe ingresar una partida para analizar.");
            txtErrores.setForeground(Color.RED);
            btnVisualizarArbol.setEnabled(false);
            return;
        }

        boolean esValida = parser.parse(partidaSAN);

        if (esValida) {
            txtErrores.setText("La partida es sintácticamente válida. Se encontraron " +
                    parser.getTurnos().size() + " turnos.");
            txtErrores.setForeground(new Color(0, 128, 0)); // Verde
            btnVisualizarArbol.setEnabled(true);
            arbol = parser.obtenerArbol();
        } else {
            txtErrores.setText("Error en la partida: " + parser.getErrorMessage());
            txtErrores.setForeground(Color.RED);
            btnVisualizarArbol.setEnabled(false);
            arbol = null;
        }
    }

    private void visualizarArbol() {
        if (arbol == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay árbol disponible. Primero analice una partida válida.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Limpiar panel anterior
        panelArbol.removeAll();

        // Crear un nuevo panel para visualizar el árbol
        ArbolPanel arbolPanel = new ArbolPanel();
        arbolPanel.setArbol(arbol);

        // Añadir controles de zoom
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton zoomInButton = new JButton("+");
        JButton zoomOutButton = new JButton("-");
        JButton zoomResetButton = new JButton("Reset");

        zoomInButton.addActionListener(e -> {
            arbolPanel.setZoom(arbolPanel.getZoom() * 1.2);
            arbolPanel.repaint();
        });

        zoomOutButton.addActionListener(e -> {
            arbolPanel.setZoom(arbolPanel.getZoom() / 1.2);
            arbolPanel.repaint();
        });

        zoomResetButton.addActionListener(e -> {
            arbolPanel.setZoom(1.0);
            arbolPanel.repaint();
        });

        controlPanel.add(new JLabel("Zoom: "));
        controlPanel.add(zoomInButton);
        controlPanel.add(zoomOutButton);
        controlPanel.add(zoomResetButton);

        // Añadir leyenda
        JPanel leyendaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leyendaPanel.add(new JLabel("Leyenda:"));
        leyendaPanel.add(crearEtiquetaLeyenda("Raíz", Color.YELLOW));
        leyendaPanel.add(crearEtiquetaLeyenda("Turno", Color.WHITE));
        leyendaPanel.add(crearEtiquetaLeyenda("Jugada Blanca", new Color(255, 240, 200))); // Beige
        leyendaPanel.add(crearEtiquetaLeyenda("Jugada Negra", new Color(120, 120, 120))); // Gris

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(controlPanel, BorderLayout.NORTH);
        southPanel.add(leyendaPanel, BorderLayout.SOUTH);

        // Configurar JScrollPane con barras siempre visibles para desplazamiento
        JScrollPane scrollArbol = new JScrollPane(
                arbolPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        scrollArbol.setPreferredSize(new Dimension(1150, 550));

        // Incremento de unidad más grande para desplazamiento más rápido
        scrollArbol.getVerticalScrollBar().setUnitIncrement(20);
        scrollArbol.getHorizontalScrollBar().setUnitIncrement(20);

        panelArbol.add(scrollArbol, BorderLayout.CENTER);
        panelArbol.add(southPanel, BorderLayout.SOUTH);

        // Actualizar la interfaz
        panelArbol.revalidate();
        panelArbol.repaint();
    }

    private JLabel crearEtiquetaLeyenda(String texto, Color color) {
        JLabel label = new JLabel(texto);
        label.setOpaque(true);
        label.setBackground(color);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setPreferredSize(new Dimension(100, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        if (color.getRed() + color.getGreen() + color.getBlue() < 450) {
            label.setForeground(Color.WHITE);
        } else {
            label.setForeground(Color.BLACK);
        }
        return label;
    }

    private void cargarEjemplo() {
        String ejemploPartida = "1. d4 d5 2. Bf4 Nf6 3. e3 e6 4. c3 c5 5. Nd2 Nc6 6. Bd3 Bd6";
        txtPartida.setText(ejemploPartida);
        txtErrores.setText("Ejemplo cargado. Presione 'Analizar Partida' para procesar.");
        txtErrores.setForeground(Color.BLUE);
        btnVisualizarArbol.setEnabled(false);
    }

    /**
     * Panel para dibujar el árbol binario - VERSIÓN FINAL
     */
    private class ArbolPanel extends JPanel {
        private ArbolBinario arbol;
        private final int ANCHO_NODO = 35;
        private final int ALTO_NODO = 35;
        private final int MARGEN_VERTICAL = 60;
        private final int MARGEN_HORIZONTAL = 60;
        private final Color COLOR_NODO_RAIZ = Color.YELLOW;
        private final Color COLOR_NODO_TURNO = Color.WHITE;
        private final Color COLOR_NODO_BLANCO = new Color(255, 240, 200); // Beige
        private final Color COLOR_NODO_NEGRO = new Color(120, 120, 120); // Gris
        private final Color COLOR_BORDE = Color.BLACK;
        private double zoom = 1.0;

        public void setArbol(ArbolBinario arbol) {
            this.arbol = arbol;
            updatePreferredSize();
        }

        public double getZoom() {
            return zoom;
        }

        public void setZoom(double zoom) {
            this.zoom = zoom;
            updatePreferredSize();
        }

        /**
         * Ajusta el tamaño preferido del panel según el tamaño del árbol
         */
        private void updatePreferredSize() {
            if (arbol == null) return;

            // Obtener dimensiones del árbol
            int altura = calcularAltura(arbol.getRaiz());
            int numNodos = contarNodos(arbol.getRaiz());

            // Calcular tamaño requerido con margen abundante
            int anchoEstimado = Math.max(numNodos * 100, 3000); // Un ancho generoso
            int altoEstimado = altura * (ALTO_NODO + MARGEN_VERTICAL) * 2;

            // Aplicar zoom
            int prefWidth = (int) (anchoEstimado * zoom);
            int prefHeight = (int) (altoEstimado * zoom);

            // Establecer tamaño preferido para el panel
            setPreferredSize(new Dimension(prefWidth, prefHeight));

            revalidate();
        }

        /**
         * Calcula la altura del árbol
         */
        private int calcularAltura(Nodo nodo) {
            if (nodo == null) return 0;
            return 1 + Math.max(
                    calcularAltura(nodo.getHijoIzquierdo()),
                    calcularAltura(nodo.getHijoDerecho())
            );
        }

        /**
         * Cuenta el número total de nodos en el árbol
         */
        private int contarNodos(Nodo nodo) {
            if (nodo == null) return 0;
            return 1 + contarNodos(nodo.getHijoIzquierdo()) +
                    contarNodos(nodo.getHijoDerecho());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (arbol == null) {
                g.setColor(Color.RED);
                g.drawString("No hay árbol para visualizar", 10, 20);
                return;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Aplicar zoom manteniendo la transformación original
            AffineTransform originalTransform = g2d.getTransform();
            g2d.scale(zoom, zoom);

            // Coordenadas iniciales para la raíz
            int startX = (int) ((getWidth() / zoom) / 2);
            int startY = 50;

            try {
                // Calculamos el offset inicial para dejar espacio suficiente
                int offsetInicial = (int) (getWidth() / (zoom * 4));

                // Dibujar árbol desde la raíz
                dibujarArbolBinario(g2d, arbol.getRaiz(), startX, startY, offsetInicial, 0);
            } catch (Exception e) {
                g2d.setColor(Color.RED);
                g2d.drawString("Error al dibujar el árbol: " + e.getMessage(), 10, 20);
                e.printStackTrace();
            }

            // Restaurar transformación original
            g2d.setTransform(originalTransform);
        }

        /**
         * Dibuja el árbol binario recursivamente
         */
        private void dibujarArbolBinario(Graphics2D g, Nodo nodo, int x, int y, int offset, int nivel) {
            if (nodo == null) return;

            // Determinar el color según el tipo de nodo
            Color colorNodo;
            if (nivel == 0) {
                // Nodo raíz "Partida"
                colorNodo = COLOR_NODO_RAIZ;
            } else if (nodo.getValor().startsWith("T")) {
                // Nodos de turnos (T1, T2, etc.)
                colorNodo = COLOR_NODO_TURNO;
            } else {
                // Jugadas - blancas a la izquierda, negras a la derecha
                Nodo padre = encontrarPadre(arbol.getRaiz(), nodo);
                if (padre != null && padre.getHijoIzquierdo() == nodo) {
                    colorNodo = COLOR_NODO_BLANCO;
                } else {
                    colorNodo = COLOR_NODO_NEGRO;
                }
            }

            // Dibujar nodo
            g.setColor(colorNodo);
            g.fillOval(x - ANCHO_NODO/2, y - ALTO_NODO/2, ANCHO_NODO, ALTO_NODO);
            g.setColor(COLOR_BORDE);
            g.drawOval(x - ANCHO_NODO/2, y - ALTO_NODO/2, ANCHO_NODO, ALTO_NODO);

            // Dibujar texto del nodo
            String texto = nodo.getValor();
            if (texto.length() > 3) {
                texto = texto.substring(0, 3);
            }

            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            FontMetrics fm = g.getFontMetrics();
            int anchoTexto = fm.stringWidth(texto);
            int altoTexto = fm.getHeight();

            // Color del texto según el fondo
            if (colorNodo.equals(COLOR_NODO_NEGRO)) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }

            g.drawString(texto, x - anchoTexto/2, y + altoTexto/4);

            // Reducir offset para niveles más profundos, pero mantener un mínimo
            int siguienteOffset = Math.max(offset / 2, MARGEN_HORIZONTAL);

            // Posición y para hijos
            int siguienteY = y + MARGEN_VERTICAL;

            // Dibujar hijo izquierdo y conexión
            if (nodo.getHijoIzquierdo() != null) {
                int hijoX = x - offset;

                g.setColor(COLOR_BORDE);
                g.drawLine(x, y + ALTO_NODO/2, hijoX, siguienteY - ALTO_NODO/2);

                dibujarArbolBinario(g, nodo.getHijoIzquierdo(), hijoX, siguienteY, siguienteOffset, nivel + 1);
            }

            // Dibujar hijo derecho y conexión
            if (nodo.getHijoDerecho() != null) {
                int hijoX = x + offset;

                g.setColor(COLOR_BORDE);
                g.drawLine(x, y + ALTO_NODO/2, hijoX, siguienteY - ALTO_NODO/2);

                dibujarArbolBinario(g, nodo.getHijoDerecho(), hijoX, siguienteY, siguienteOffset, nivel + 1);
            }
        }

        /**
         * Encuentra el nodo padre para un nodo dado
         */
        private Nodo encontrarPadre(Nodo raiz, Nodo objetivo) {
            if (raiz == null || objetivo == null) return null;

            if ((raiz.getHijoIzquierdo() == objetivo) || (raiz.getHijoDerecho() == objetivo)) {
                return raiz;
            }

            Nodo encontradoIzq = encontrarPadre(raiz.getHijoIzquierdo(), objetivo);
            if (encontradoIzq != null) return encontradoIzq;

            return encontrarPadre(raiz.getHijoDerecho(), objetivo);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("No se pudo establecer el look and feel del sistema: " + e.getMessage());
            }
            new VisualizadorArbol().setVisible(true);
        });
    }
}
