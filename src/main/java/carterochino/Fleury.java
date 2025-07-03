// Fleury.java
package carterochino;

import java.util.*;
import graphTDA.*;

public class Fleury {
    public void FleuryAlgorithm(Grafo grafo) {
        if (grafo.esUnidireccional()) {
            System.out.println("El grafo es unidireccional, y el algoritmo de Fleury requiere un grafo no dirigido.");
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
                System.out.println("Error: el vertice " + v.getId() + " no tiene aristas adyacentes.");
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
            System.out.println(v.getId() + " -> " + u.getId());

            System.out.println("Eliminando arista: " + v.getId() + " -> " + u.getId());
            untraversed.borrarAristaDirigida(new Arista(v.getId(), u.getId(), 0));

            v = u;
        }

        System.out.println("Se completo el algoritmo de Fleury para el grafo.");
    }
}
