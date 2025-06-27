package carterochino;

import java.util.*;
import graphTDA.*;

public class Fleury {
    public void FleuryAlgorithm(Grafo grafo) {
        if (grafo.esUnidireccional()) {
            System.out.println("El grafo es unidireccional, y el algoritmo de Fleury requiere un grafo no dirigido.");
        }
        else{
            Vertice v = grafo.getVertice(0);    // cualquier vertice
            Grafo camino = new Grafo();
            camino.agregarVertice(v.getId());
            Grafo untraversed = grafo.clonar();
            
            while(!untraversed.vacio()){
                // Obtengo a todas las aristas adyacentes al vertice v sin recorrer
                List<Arista> adyacentes = untraversed.obtenerAristasDesde(v.getId());
                
                Arista elegida;
                
                if(adyacentes.size() == 1){ // Solo hay una arista adyacente
                    elegida = adyacentes.get(0);
                }
                else{ // buscar a una arista que no sea puente
                    elegida = null;
                    for(Arista a: adyacentes){
                        if(!untraversed.esPuente(v.getId(),a.getDestino())){
                            elegida = a;
                            break;
                        }
                    }
                }
                
                // Si todas son puentes, toma cualquiera
                if(elegida == null){
                    elegida = adyacentes.get(0);
                }
                int u = elegida.getDestino();
                camino.agregarArista(v.getId(),u);
                untraversed.borrarArista(elegida.getOrigen(),elegida.getDestino());
                
                v = untraversed.getVertice(u);  // avanzar al siguiente vértice
                System.out.println(v.getId() + " → " + u);
            }
            
            
            if (untraversed.vacio()) {
                System.out.println("Se completo el algoritmo de Fleury para el grafo.");
            } else {
                System.out.println("No pudo completarse el algoritmo de Fleury.");
            }
            
        }
    }
}

