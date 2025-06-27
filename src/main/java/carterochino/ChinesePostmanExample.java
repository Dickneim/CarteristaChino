package carterochino;

import graphTDA.*;

import java.util.List;

public class ChinesePostmanExample {

    public static void main(String[] args) {
        // Crear un grafo no dirigido
        Graph<String> graph = new Graph<>(Graph.TYPE.UNDIRECTED);
        
        // Crear vértices
        Vertex<String> v1 = new Vertex<>("A");
        Vertex<String> v2 = new Vertex<>("B");
        Vertex<String> v3 = new Vertex<>("C");
        Vertex<String> v4 = new Vertex<>("D");

        // Agregar los vértices al grafo
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        // Crear aristas (con costos de ejemplo)
        Edge<String> e1 = new Edge<>(1, v1, v2);
        Edge<String> e2 = new Edge<>(1, v2, v3);
        Edge<String> e3 = new Edge<>(1, v3, v4);
        Edge<String> e4 = new Edge<>(1, v4, v1);
        Edge<String> e5 = new Edge<>(1, v1, v3);  // Una arista adicional para hacer el grafo euleriano

        // Agregar las aristas al grafo
        graph.addEdge(e1);
        graph.addEdge(e2);
        graph.addEdge(e3);
        graph.addEdge(e4);
        graph.addEdge(e5);

        // Crear una instancia del problema del cartero chino
        ChinesePostman<String> chinesePostman = new ChinesePostman<>();

        // Obtener el camino o ciclo euleriano
        List<Vertex<String>> eulerianPath = chinesePostman.findEulerianPath(graph);

        // Verificar que el camino euleriano no esté vacío
        if (eulerianPath.isEmpty()) {
            System.out.println("No se encontró un camino euleriano.");
        } else {
            // Imprimir el recorrido euleriano
            System.out.println("El camino euleriano encontrado es:");
            for (Vertex<String> vertex : eulerianPath) {
                System.out.print(vertex.getValue() + " -> ");
            }
            System.out.println();  // Para terminar la impresión del camino
        }
    }
}
