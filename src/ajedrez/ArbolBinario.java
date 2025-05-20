package src.ajedrez;

public class ArbolBinario {
    private Nodo raiz;

    public ArbolBinario() {
        this.raiz = null;
    }

    public ArbolBinario(String valorRaiz) {
        this.raiz = new Nodo(valorRaiz);
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }

    public Nodo getRaiz() {
        return raiz;
    }
}
