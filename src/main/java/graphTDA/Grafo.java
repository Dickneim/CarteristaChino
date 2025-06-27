package graphTDA;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
    
    public boolean esUnidireccional() {
        for (Arista a : aristas) {
            Arista inversa = new Arista(a.getDestino(), a.getOrigen());
            if (aristas.contains(inversa)) {
                return false; // Hay al menos una arista opuesta: no es unidireccional
            }
        }
        return true; // No se encontró ninguna arista opuesta
    }

    public void agregarVertice(int id) {
        vertices.add(new Vertice(id));
    }

    public void agregarArista(int origen, int destino) {
        Arista arista = new Arista(origen, destino);
        aristas.add(arista);
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

    public boolean borrarArista(int origen, int destino) {
        return aristas.removeIf(a -> (a.getOrigen() == origen && a.getDestino() == destino) || 
                                     (a.getOrigen() == destino && a.getDestino() == origen));
    }

    public List<Arista> getAristas() {
        return aristas;
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
            copia.agregarArista(a.getOrigen(), a.getDestino());
        }
    return copia;
    }
    
    public boolean vacio() {
        return aristas.isEmpty();
    }
    
    public boolean esPuente(int origen, int destino) {
        // Clona el grafo y elimina la arista
        Grafo copia = this.clonar();
        copia.borrarArista(origen, destino);

        // Verifica si origen y destino siguen conectados
        return !copia.estanConectados(origen, destino);
    }
    
    // búsqueda en profundidad (DFS) para verificar si hay un camino entre 
    // dos vértices en el grafo, es decir, si están en la misma componente conexa.
    
    public boolean estanConectados(int origen, int destino) {
        Set<Integer> visitados = new HashSet<>();
        dfs(origen, visitados);
        return visitados.contains(destino);
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
}
    

