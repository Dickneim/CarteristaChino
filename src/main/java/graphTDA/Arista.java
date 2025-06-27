/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graphTDA;

/**
 *
 * @author mrMauro
 */
public class Arista {
    private int origen;
    private int destino;
    private int peso=0;

    public Arista(int origen, int destino) {
        this.origen = origen;
        this.destino = destino;
    }
    
    public void setPeso(int peso){
        this.peso=peso;
    }
    
    public int getPeso(){
        return peso;
    }
    
    public int getOrigen() {
        return origen;
    }

    public int getDestino() {
        return destino;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Arista)) return false;
        Arista other = (Arista) obj;
        return this.origen == other.origen && this.destino == other.destino;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(origen) * 31 + Integer.hashCode(destino);
    }

}

