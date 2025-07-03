package carterochino;

import graphTDA.*;

public class ChinesePostmanExample {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // Crear vértices
        for (int i = 0; i <= 3; i++) {
            grafo.agregarVertice(i);
        }

        // Agregar aristas no dirigidas (cada una en ambas direcciones)
        agregarAristaNoDirigida(grafo, 0, 1, 1);
        agregarAristaNoDirigida(grafo, 1, 2, 2);
        agregarAristaNoDirigida(grafo, 2, 0, 1);
        agregarAristaNoDirigida(grafo, 2, 3, 1);

        System.out.println("Listado total de aristas:");
        for (Arista a : grafo.getAristas()) {
            System.out.println(" - " + a.getOrigen() + " -> " + a.getDestino() + " (peso " + a.getPeso() + ")");
        }

        System.out.println("\nRecorrido optimo para el cartero (post-Chinese Postman):");
        new ChinesePostman().ChinesePostmanTour(grafo);
    }

    public static void agregarAristaNoDirigida(Grafo grafo, int v1, int v2, int peso) {
        if (grafo.buscarAristaEntre(v1, v2) == null) {
            grafo.agregarArista(v1, v2, peso);
        }
        if (grafo.buscarAristaEntre(v2, v1) == null) {
            grafo.agregarArista(v2, v1, peso);
        }
    }
}




