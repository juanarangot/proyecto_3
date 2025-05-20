package src.ajedrez;

public class Turno {
    private int numeroTurno;
    private String jugadaBlanca;
    private String jugadaNegra;

    public Turno(int numeroTurno, String jugadaBlanca, String jugadaNegra) {
        this.numeroTurno = numeroTurno;
        this.jugadaBlanca = jugadaBlanca;
        this.jugadaNegra = jugadaNegra;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public String getJugadaBlanca() {
        return jugadaBlanca;
    }

    public String getJugadaNegra() {
        return jugadaNegra;
    }
}
