package carterochino;

import graphTDA.*;

public class ChinesePostmanExample {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // Crear vertices
        for (int i = 0; i <= 3; i++) {
            grafo.agregarVertice(i);
        }

        // Agregar aristas no dirigidas (dobles)
        grafo.agregarArista(0, 1, 1);
        grafo.agregarArista(1, 0, 1);
        grafo.agregarArista(1, 2, 2);
        grafo.agregarArista(2, 1, 2);
        grafo.agregarArista(2, 0, 1);
        grafo.agregarArista(0, 2, 1);
        grafo.agregarArista(2, 3, 1);
        grafo.agregarArista(3, 2, 1);

        System.out.println("Listado inicial de aristas:");
        for (Arista a : grafo.getAristas()) {
            System.out.println(" - " + a.getOrigen() + " -> " + a.getDestino() + " (peso " + a.getPeso() + ")");
        }

        System.out.println("\nResolviendo el problema del Cartero Chino...");
        ChinesePostman algoritmo = new ChinesePostman();
        algoritmo.setTextArea(null); // Para que imprima en consola
        algoritmo.ChinesePostmanTour(grafo);
    }
}




