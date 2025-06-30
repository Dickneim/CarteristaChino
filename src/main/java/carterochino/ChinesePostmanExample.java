package carterochino;

import graphTDA.*;

public class ChinesePostmanExample {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // Agregar vértices
        for (int i = 0; i <= 3; i++) {
            grafo.agregarVertice(i);
        }

        // Agregar aristas no dirigidas con pesos
        agregarAristaNoDirigida(grafo, 0, 1, 1);
        agregarAristaNoDirigida(grafo, 1, 2, 2);
        agregarAristaNoDirigida(grafo, 2, 3, 1);
        agregarAristaNoDirigida(grafo, 3, 0, 2);
        agregarAristaNoDirigida(grafo, 1, 3, 1);

        // Ejecutar algoritmo del Cartero Chino
        System.out.println("Recorrido optimo para el cartero (post-Chinese Postman):");
        ChinesePostman algoritmo = new ChinesePostman();
        algoritmo.ChinesePostmanTour(grafo);
    }

    // Método auxiliar para simular aristas no dirigidas
    public static void agregarAristaNoDirigida(Grafo grafo, int v1, int v2, int peso) {
        grafo.agregarArista(v1, v2, peso);
        grafo.agregarArista(v2, v1, peso);
    }
}


