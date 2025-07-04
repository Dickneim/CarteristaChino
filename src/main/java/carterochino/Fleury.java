// Fleury.java
package carterochino;

import java.util.*;
import graphTDA.*;
import javax.swing.JTextArea;

public class Fleury {
    private JTextArea areaPasos = null;

    public void setTextArea(JTextArea areaPasos) {
        this.areaPasos = areaPasos;

    }
    
    public void FleuryAlgorithm(Grafo grafo) {
        
        
        if (grafo.esUnidireccional()) {
            if (areaPasos != null) {
                areaPasos.append("\nEl grafo es unidireccional, y el algoritmo de Fleury requiere un grafo no dirigido.\n");
            } else {
                System.out.println("El grafo es unidireccional, y el algoritmo de Fleury requiere un grafo no dirigido.");
            }
            return;
        }

        Vertice v = grafo.getVertice(0);
        Grafo camino = new Grafo();
        camino.agregarVertice(v.getId());
        Grafo untraversed = grafo.clonar();

        while (!untraversed.vacio()) {
            List<Arista> adyacentes = untraversed.obtenerAristasDesde(v.getId()).stream()
                    .filter(a -> a.getOrigen() != a.getDestino())
                    .toList();

            if (adyacentes.isEmpty()) {
                if (areaPasos != null) {
                    areaPasos.append("Error: el vertice " + v.getId() + " no tiene aristas adyacentes.\n");
                } else {
                    System.out.println("Error: el vertice " + v.getId() + " no tiene aristas adyacentes.\n");
                }
                return;
            }

            Arista elegida = null;

            for (Arista a : adyacentes) {
                Vertice vecino = grafo.getVertice((a.getOrigen() == v.getId()) ? a.getDestino() : a.getOrigen());
                if (!untraversed.esPuente(v, vecino)) {
                    elegida = a;
                    break;
                }
            }

            if (elegida == null) {
                final int idActual = v.getId();
                elegida = adyacentes.stream()
                    .max(Comparator.comparingInt(a -> {
                        int vecinoId = (a.getOrigen() == idActual) ? a.getDestino() : a.getOrigen();
                        return untraversed.grado(vecinoId);
                    }))
                    .orElse(adyacentes.get(0));
            }

            Vertice u = grafo.getVertice((elegida.getOrigen() == v.getId()) ? elegida.getDestino() : elegida.getOrigen());

            camino.agregarArista(v.getId(), u.getId(), elegida.getPeso());
            if (areaPasos != null) {
                areaPasos.append(v.getId() + " -> " + u.getId());
            } else {
                System.out.println(v.getId() + " -> " + u.getId());
            }

            if (areaPasos != null) {
                areaPasos.append("Eliminando arista: " + v.getId() + " -> " + u.getId());
            } else {
                System.out.println("Eliminando arista: " + v.getId() + " -> " + u.getId());
            }
            untraversed.borrarAristaDirigida(new Arista(v.getId(), u.getId(), 0));

            v = u;
        }

        if (areaPasos != null) {
            areaPasos.append("Se completo el algoritmo de Fleury para el grafo.");
        } else {
            System.out.println("Se completo el algoritmo de Fleury para el grafo.");
        }
    }
}
