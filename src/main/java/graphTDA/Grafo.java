// Clase Grafo
package graphTDA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 *
 * @author mrMauro
 */
public class Grafo {
    private final List<Vertice> vertices;
    private final List<Arista> aristas;

    public Grafo() {
        vertices = new ArrayList<>();
        aristas = new ArrayList<>();
    }
    
    public Grafo(List<Vertice> vertices, List<Arista> aristas) {
        this.vertices = new ArrayList<>();
        for (Vertice v : vertices) {
            this.vertices.add(new Vertice(v.getId()));
        }

        this.aristas = new ArrayList<>();
        for (Arista a : aristas) {
            this.aristas.add(new Arista(a.getOrigen(), a.getDestino(), a.getPeso()));
        }
    }
    
    public boolean esUnidireccional() {
        for (Arista a : aristas) {
            Arista inversa = new Arista(a.getDestino(), a.getOrigen(), a.getPeso());
            if (!aristas.contains(inversa)) {
                System.out.println("No se encontró la inversa de: " + a.getOrigen() + " -> " + a.getDestino());
                return true; // grafo es unidireccional
            }
        }
        return false; // grafo es no dirigido
    }

    
    public void agregarVertice(int id) {
        vertices.add(new Vertice(id));
    }

    public void agregarArista(int origen, int destino, int peso) {
        aristas.add(new Arista(origen, destino, peso));
    }

    
    public void agregarAristaNoDirigida(int origen, int destino, int peso) {
        agregarArista(origen, destino, peso);
        agregarArista(destino, origen, peso);
    }


    public boolean borrarVertice(int id) {
        Vertice aRemover = null;
        for (Vertice v : vertices) {
            if (v.getId() == id) {
                aRemover = v;
                break;
            }
        }
        if (aRemover != null) {
            vertices.remove(aRemover);
            aristas.removeIf(a -> a.getOrigen() == id || a.getDestino() == id);
            return true;
        }
        return false;
    }

    public boolean borrarArista(Vertice origen, Vertice destino) {
        return aristas.removeIf(a ->
            (a.getOrigen() == origen.getId() && a.getDestino() == destino.getId()) ||
            (a.getOrigen() == destino.getId() && a.getDestino() == origen.getId())
        );
    }

    // Dentro de la clase Grafo
    public boolean borrarAristaDirigida(Arista arista) {
        return aristas.removeIf(a -> 
            a.getOrigen() == arista.getOrigen() && a.getDestino() == arista.getDestino()
        );
    }


    public List<Arista> getAristas() {
        return new ArrayList<>(aristas);
    }
    
    public List<Vertice> getVertice(){
        return vertices;
    }
    
    public Vertice getVertice(int id) {
        for (Vertice v : vertices) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }
    
    public Grafo clonar() {
        Grafo copia = new Grafo();
        for (Vertice v : this.vertices) {
            copia.agregarVertice(v.getId());
        }
        for (Arista a : this.aristas) {
            copia.agregarArista(a.getOrigen(), a.getDestino(),a.getPeso());
        }
        return copia;
    }
    
    public boolean vacio() {
        return aristas.isEmpty();
    }
    
    public boolean esPuente(Vertice v1, Vertice v2) {
        // Crear una copia del grafo
        Grafo copia = this.clonar();

        // Eliminar la arista (ambas direcciones)
        copia.borrarArista(v1, v2);

        // Verificar si siguen conectados
        return !copia.estanConectados(v1.getId(), v2.getId());
    }

    // búsqueda en profundidad (DFS) para verificar si hay un camino entre 
    // dos vértices en el grafo, es decir, si están en la misma componente conexa.
    
    public boolean estanConectados(int origen, int destino) {
        Set<Integer> visitados = new HashSet<>();
        dfs(origen, visitados);
        return visitados.contains(destino);
    }
    
    public boolean esConexo(){
        if (vertices.isEmpty()) return false;
        Set<Integer> visitados = new HashSet<>();
        dfs(vertices.get(0).getId(), visitados);
        return visitados.size() == vertices.size();
    }

    private void dfs(int actual, Set<Integer> visitados) {
        visitados.add(actual);
        for (Arista a : aristas) {
            if (a.getOrigen() == actual && !visitados.contains(a.getDestino())) {
                dfs(a.getDestino(), visitados);
            } else if (a.getDestino() == actual && !visitados.contains(a.getOrigen())) {
                dfs(a.getOrigen(), visitados);
            }
        }
    }
    
    // Este método te permite obtener todas las aristas conectadas a un vértice
    public List<Arista> obtenerAristasDesde(int v) {
        List<Arista> adyacentes = new ArrayList<>();
        for (Arista a : aristas) {
            if (a.getOrigen() == v || a.getDestino() == v) {
                adyacentes.add(a);
            }
        }
        return adyacentes;
    }

    
    // Calculamos el grado de un vertice en el grado
    public int grado(int idVertice) {
        int grado = 0;
        for (Arista a : aristas) {
            if (a.getOrigen() == idVertice || a.getDestino() == idVertice) {
                grado++;
            }
        }
        return grado;
    }
    
    public Map<Integer, Integer> dijkstra(int origen) {
        Map<Integer, Integer> distancias = new HashMap<>();
        for (Vertice v : vertices) {
            distancias.put(v.getId(), Integer.MAX_VALUE);
        }
        distancias.put(origen, 0);

        PriorityQueue<Arista> cola = new PriorityQueue<>(Comparator.comparingInt(Arista::getPeso));
        Set<Integer> visitados = new HashSet<>();

        // Agregar todas las aristas desde el origen
        for (Arista a : obtenerAristasDesde(origen)) {
            cola.add(new Arista(origen, a.getDestino(), a.getPeso()));
        }

        visitados.add(origen);

        while (!cola.isEmpty()) {
            Arista actual = cola.poll();
            int destino = actual.getDestino();
            int peso = actual.getPeso();

            if (!visitados.contains(destino)) {
                int nuevaDistancia = distancias.get(actual.getOrigen()) + peso;
                if (nuevaDistancia < distancias.get(destino)) {
                    distancias.put(destino, nuevaDistancia);
                    for (Arista siguiente : obtenerAristasDesde(destino)) {
                        if (!visitados.contains(siguiente.getDestino())) {
                            cola.add(new Arista(destino, siguiente.getDestino(), siguiente.getPeso()));
                        }
                    }
                }
                visitados.add(destino);
            }
        }

        return distancias;
    }
    
    public Map<Integer, Integer> dijkstraConPredecesores(int origen, Map<Integer, Integer> predecesores) {
        Map<Integer, Integer> distancias = new HashMap<>();
        for (Vertice v : vertices) {
            distancias.put(v.getId(), Integer.MAX_VALUE);
        }
        distancias.put(origen, 0);

        PriorityQueue<int[]> cola = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        cola.add(new int[]{origen, 0});
        Set<Integer> visitados = new HashSet<>();

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int nodo = actual[0];
            int dist = actual[1];

            if (!visitados.add(nodo)) continue; // evitar volver a visitar

            for (Arista a : obtenerAristasDesde(nodo)) {
                int vecino = (a.getOrigen() == nodo) ? a.getDestino() : a.getOrigen();
                int nuevaDist = distancias.get(nodo) + a.getPeso();

                if (nuevaDist < distancias.get(vecino)) {
                    distancias.put(vecino, nuevaDist);
                    predecesores.put(vecino, nodo); // asegura que vecino ≠ nodo
                    cola.add(new int[]{vecino, nuevaDist});
                }
            }
        }

        return distancias;
    }

    public Arista buscarAristaEntre(int origen, int destino) {
        for (Arista a : aristas) {
            if (a.getOrigen() == origen && a.getDestino() == destino) {
                return a;
            }
        }
        return null;
    }

    public boolean esEuleriano() {
        if (!esConexo()) return false;
        for (Vertice v : vertices) {
            if (grado(v.getId()) % 2 != 0) {
                return false;
            }
        }
        return true;
    }
    
}
    

