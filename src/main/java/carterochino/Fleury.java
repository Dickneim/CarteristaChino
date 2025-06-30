package carterochino;

import java.util.*;
import graphTDA.*;

public class Fleury {

    public void FleuryAlgorithm(Grafo grafo) {
        if (grafo.esUnidireccional()) {
            System.out.println("El grafo es unidireccional, y el algoritmo de Fleury requiere un grafo no dirigido.");
            return;
        }

        // Paso 1: Empezar desde cualquier vértice
        int v = grafo.getVertice(0).getId(); // Se toma cualquier vértice (el primero)

        Grafo camino = new Grafo();
        camino.agregarVertice(v);

        // Clonar el grafo para no modificar el original
        Grafo untraversed = grafo.clonar();

        while (!untraversed.vacio()) {
            // Paso 2: Obtener aristas adyacentes que no sean bucles
            List<Arista> adyacentes = untraversed.obtenerAristasDesde(v).stream()
                    .filter(a -> a.getOrigen() != a.getDestino()) // evitar bucles
                    .toList();
            System.out.println("Verificando adyacentes desde vertice " + v + ":");
            for (Arista a : untraversed.obtenerAristasDesde(v)) {
                System.out.println(" - " + a.getOrigen() + " -> " + a.getDestino());
            }
            if (adyacentes.isEmpty()) {
                System.out.println("Error: el vertice " + v + " no tiene aristas adyacentes.");
                return;
            }
            System.out.println("Adyacentes desde " + v + ":");
            for (Arista a : adyacentes) {
                System.out.println(" - " + a.getOrigen() + " -> " + a.getDestino());
            }
            // Paso 3: Elegir una arista según las reglas de Fleury
            Arista elegida;

            if (adyacentes.size() == 1) {
                // Si solo hay una, se debe tomar esa
                elegida = adyacentes.get(0);
            } else {
                // Buscar una arista que no sea puente
                elegida = null;
                for (Arista a : adyacentes) {
                    if (!untraversed.esPuente(v, a.getDestino())) {
                        elegida = a;
                        break;
                    }
                }
                // Si todas son puentes, tomamos cualquiera
                if (elegida == null) {
                    elegida = adyacentes.get(0);
                }
            }

            int u = (elegida.getOrigen() == v) ? elegida.getDestino() : elegida.getOrigen();

            // Agregar la arista al camino y eliminarla del grafo
            camino.agregarArista(v, u, elegida.getPeso());
            System.out.println(v + " -> " + u);
            
            System.out.println("Eliminando arista: " + elegida.getOrigen() + " -> " + elegida.getDestino());
            untraversed.borrarArista(elegida.getOrigen(), elegida.getDestino());

            // Continuar desde el nuevo vértice
            v = u;
        }

        System.out.println("Se completó el algoritmo de Fleury para el grafo.");
    }
}



