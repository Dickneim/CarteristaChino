// Clase Arista
package graphTDA;
import java.util.Objects;

public class Arista {
    private int origen;
    private int destino;
    private int peso;

    public Arista(int origen, int destino, int peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public int getOrigen() { return origen; }
    public int getDestino() { return destino; }
    public int getPeso() { return peso; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Arista)) return false;
        Arista a = (Arista) obj;
        return origen == a.origen && destino == a.destino && peso == a.peso;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origen, destino, peso);
    }
}


