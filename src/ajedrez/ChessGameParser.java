package src.ajedrez;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analizador sintáctico para juegos de ajedrez en notación algebraica estándar (SAN)
 */
public class ChessGameParser {
    // Expresiones regulares para los diferentes componentes de la notación SAN
    private static final String REGEX_CASILLA = "[a-h][1-8]";
    private static final String REGEX_PIEZA = "[KQRBN]";
    private static final String REGEX_ENROQUE_CORTO = "O-O";
    private static final String REGEX_ENROQUE_LARGO = "O-O-O";
    private static final String REGEX_JAQUE_MATE = "[+#]";
    private static final String REGEX_PROMOCION = "=[QRBN]";
    private static final String REGEX_LETRA = "[a-h]";
    private static final String REGEX_NUMERO = "[1-8]";
    private static final String REGEX_DESAMBIGUACION = "(" + REGEX_LETRA + "|" + REGEX_NUMERO + "|" + REGEX_LETRA + REGEX_NUMERO + ")";
    private static final String REGEX_CAPTURA = "x";

    // Patrones para los diferentes tipos de jugadas
    private static final Pattern PATTERN_ENROQUE =
            Pattern.compile("^(O-O|O-O-O)(" + REGEX_JAQUE_MATE + ")?$");

    private static final Pattern PATTERN_MOVIMIENTO_PIEZA =
            Pattern.compile("^" + REGEX_PIEZA + "(" + REGEX_DESAMBIGUACION + ")?(" + REGEX_CAPTURA + ")?(" +
                    REGEX_CASILLA + ")(" + REGEX_PROMOCION + ")?(" + REGEX_JAQUE_MATE + ")?$");

    private static final Pattern PATTERN_PEON_AVANCE =
            Pattern.compile("^(" + REGEX_CASILLA + ")(" + REGEX_PROMOCION + ")?(" + REGEX_JAQUE_MATE + ")?$");

    private static final Pattern PATTERN_PEON_CAPTURA =
            Pattern.compile("^(" + REGEX_LETRA + ")(" + REGEX_CAPTURA + ")(" + REGEX_CASILLA +
                    ")(" + REGEX_PROMOCION + ")?(" + REGEX_JAQUE_MATE + ")?$");

    // Patrón para identificar un turno completo
    private static final Pattern PATTERN_TURNO =
            Pattern.compile("(\\d+)\\.\\s+([^\\s]+)(\\s+([^\\s]+))?");

    private List<Turno> turnos;
    private String errorMessage;
    private ArbolBinario arbol;

    public ChessGameParser() {
        this.turnos = new ArrayList<>();
        this.errorMessage = "";
        this.arbol = new ArbolBinario();
    }

    /**
     * Analiza una partida completa en notación SAN y construye un árbol binario
     * @param partidaSAN La partida en formato SAN
     * @return true si la partida es válida, false en caso contrario
     */
    public boolean parse(String partidaSAN) {
        turnos.clear();
        errorMessage = "";
        arbol = new ArbolBinario();

        try {
            // Normalizar espacios y saltos de línea
            partidaSAN = partidaSAN.replaceAll("\\s+", " ").trim();

            // Dividir la partida en turnos
            Matcher turnoMatcher = PATTERN_TURNO.matcher(partidaSAN);

            int ultimoNumeroTurno = 0;
            while (turnoMatcher.find()) {
                String numeroTurnoStr = turnoMatcher.group(1);
                String jugadaBlanca = turnoMatcher.group(2);
                String jugadaNegra = turnoMatcher.group(4); // Puede ser null

                // Validar número de turno
                int numeroTurno;
                try {
                    numeroTurno = Integer.parseInt(numeroTurnoStr);
                } catch (NumberFormatException e) {
                    errorMessage = "Error en el formato del número de turno: " + numeroTurnoStr;
                    return false;
                }

                // Verificar secuencia de números de turno
                if (numeroTurno != ultimoNumeroTurno + 1) {
                    errorMessage = "Error en la secuencia de turnos. Se esperaba el turno " +
                            (ultimoNumeroTurno + 1) + " pero se encontró " + numeroTurno;
                    return false;
                }
                ultimoNumeroTurno = numeroTurno;

                // Validar jugada blanca
                if (!validarJugada(jugadaBlanca)) {
                    errorMessage = "Error en la jugada blanca del turno " + numeroTurno +
                            ": '" + jugadaBlanca + "' no es una jugada válida según la gramática BNF";
                    return false;
                }

                // Validar jugada negra (si existe)
                if (jugadaNegra != null && !jugadaNegra.isEmpty()) {
                    if (!validarJugada(jugadaNegra)) {
                        errorMessage = "Error en la jugada negra del turno " + numeroTurno +
                                ": '" + jugadaNegra + "' no es una jugada válida según la gramática BNF";
                        return false;
                    }
                }

                // Crear y añadir el turno
                Turno turno = new Turno(numeroTurno, jugadaBlanca, jugadaNegra);
                turnos.add(turno);
            }

            if (turnos.isEmpty()) {
                errorMessage = "No se encontraron turnos válidos en la partida";
                return false;
            }

            // Construir el árbol después de parsear todos los turnos
            construirArbol();

            return true;
        } catch (Exception e) {
            errorMessage = "Error inesperado al analizar la partida: " + e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Valida una jugada según la gramática BNF
     */
    private boolean validarJugada(String jugada) {
        if (PATTERN_ENROQUE.matcher(jugada).matches()) {
            return true;
        }
        if (PATTERN_MOVIMIENTO_PIEZA.matcher(jugada).matches()) {
            return true;
        }
        if (PATTERN_PEON_AVANCE.matcher(jugada).matches()) {
            return true;
        }
        if (PATTERN_PEON_CAPTURA.matcher(jugada).matches()) {
            return true;
        }
        return false;
    }

    /**
     * Construye el árbol binario a partir de los turnos parseados
     */
    private void construirArbol() {
        if (turnos.isEmpty()) {
            arbol = new ArbolBinario();
            return;
        }

        // Crear el árbol con "Partida" como raíz
        arbol = new ArbolBinario("Partida");
        Nodo raiz = arbol.getRaiz();

        // Si hay al menos un turno, crear el nodo para T1
        if (!turnos.isEmpty()) {
            Nodo nodoTurno = new Nodo("T1");
            raiz.setHijoIzquierdo(nodoTurno);

            // Añadir jugadas blanca y negra de T1
            Turno turno = turnos.get(0);
            Nodo nodoBlancoT1 = new Nodo(turno.getJugadaBlanca());
            nodoTurno.setHijoIzquierdo(nodoBlancoT1);
            if (turno.getJugadaNegra() != null && !turno.getJugadaNegra().isEmpty()) {
                Nodo nodoNegroT1 = new Nodo(turno.getJugadaNegra());
                nodoTurno.setHijoDerecho(nodoNegroT1);
            }

            // Procesar los turnos restantes
            Nodo nodoActual = nodoTurno;
            for (int i = 1; i < turnos.size(); i++) {
                turno = turnos.get(i);
                Nodo nuevoTurno = new Nodo("T" + turno.getNumeroTurno());

                // Alternar entre hijo izquierdo y derecho para balancear
                if (i % 2 == 1) {
                    raiz.setHijoDerecho(nuevoTurno);
                } else {
                    Nodo temp = raiz.getHijoIzquierdo();
                    while (temp.getHijoIzquierdo() != null && temp.getHijoIzquierdo().getValor().startsWith("T")) {
                        temp = temp.getHijoIzquierdo();
                    }
                    temp.setHijoIzquierdo(nuevoTurno);
                }

                // Añadir jugadas blanca y negra del turno actual
                Nodo nodoBlanco = new Nodo(turno.getJugadaBlanca());
                nuevoTurno.setHijoIzquierdo(nodoBlanco);
                if (turno.getJugadaNegra() != null && !turno.getJugadaNegra().isEmpty()) {
                    Nodo nodoNegro = new Nodo(turno.getJugadaNegra());
                    nuevoTurno.setHijoDerecho(nodoNegro);
                }

                nodoActual = nuevoTurno;
            }
        }
    }

    /**
     * Devuelve el árbol binario generado
     * @return El árbol binario
     */
    public ArbolBinario obtenerArbol() {
        return arbol;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
