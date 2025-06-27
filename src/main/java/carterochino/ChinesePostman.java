package carterochino;

import graphTDA.*;

import java.util.*;

public class ChinesePostman<T extends Comparable<T>> {

    // Método para encontrar el camino o ciclo euleriano en un grafo
    public List<Vertex<T>> findEulerianPath(Graph<T> graph) {
        List<Vertex<T>> path = new ArrayList<>();
        
        // Verificar si el grafo es no dirigido
        if (graph.getType() != Graph.TYPE.UNDIRECTED) {
            System.out.println("El algoritmo solo funciona en grafos no dirigidos.");
            return path;
        }
        
        // Buscar vértices de grado impar
        List<Vertex<T>> oddVertices = new ArrayList<>();
        for (Vertex<T> v : graph.getVertices()) {
            if (v.getEdges().size() % 2 != 0) {
                oddVertices.add(v);
            }
        }

        // Si hay vértices de grado impar, se debe encontrar un camino euleriano
        if (oddVertices.size() == 0) {
            // Si todos los vértices tienen grado par, encontramos un ciclo euleriano
            Vertex<T> start = graph.getVertices().isEmpty() ? null : graph.getVertices().get(0);
            fleuryUtil(start, path, graph);
        } else if (oddVertices.size() == 2) {
            // Si hay exactamente dos vértices de grado impar, los conectamos para encontrar el camino euleriano
            Vertex<T> start = oddVertices.get(0); // Comenzamos desde el primer vértice de grado impar
            fleuryUtil(start, path, graph);
        } else {
            System.out.println("El grafo no tiene una solución euleriana válida.");
        }
        
        return path;
    }
    
    // Método auxiliar de Fleury para encontrar el ciclo o camino euleriano
    private void fleuryUtil(Vertex<T> current, List<Vertex<T>> path, Graph<T> graph) {
        while (current != null) {
            // Buscar una arista no visitada desde el vértice actual
            boolean hasNextEdge = false;
            for (Edge<T> edge : new ArrayList<>(current.getEdges())) {
                if (!isBridge(edge, graph)) {
                    // Si la arista no es un puente, la usamos
                    path.add(current);
                    current = edge.getToVertex();
                    graph.removeEdge(edge);  // Usamos removeEdge para eliminar la arista
                    hasNextEdge = true;
                    break;
                }
            }

            // Si no encontramos una arista no puente, usamos una arista puente (última opción)
            if (!hasNextEdge) {
                for (Edge<T> edge : current.getEdges()) {
                    path.add(current);
                    current = edge.getToVertex();
                    graph.removeEdge(edge);  // Usamos removeEdge para eliminar la arista
                    break;
                }
            }
        }
    }

    // Método para verificar si una arista es un puente
    private boolean isBridge(Edge<T> edge, Graph<T> graph) {
        // Simulamos la eliminación de la arista
        graph.removeEdge(edge);

        // Comprobamos si el grafo sigue siendo conexo
        boolean isConnected = isGraphConnected(graph);

        // Restauramos la arista eliminada
        graph.addEdge(edge);

        // Si el grafo no es conexo después de eliminar la arista, entonces es un puente
        return !isConnected;
    }

    // Método para verificar si el grafo es conexo
    private boolean isGraphConnected(Graph<T> graph) {
        Set<Vertex<T>> visited = new HashSet<>();
        if (graph.getVertices().isEmpty()) return true;
        
        // Usamos un recorrido DFS para verificar la conectividad
        dfs(graph.getVertices().get(0), visited);
        
        // Si hemos visitado todos los vértices, el grafo es conexo
        return visited.size() == graph.getVertices().size();
    }

    // Método de DFS para recorrer el grafo
    private void dfs(Vertex<T> vertex, Set<Vertex<T>> visited) {
        visited.add(vertex);
        for (Edge<T> edge : vertex.getEdges()) {
            Vertex<T> neighbor = edge.getToVertex();
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }
}
