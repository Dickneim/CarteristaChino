// ChinesePostman.java
package carterochino;

import graphTDA.*;
import java.util.*;

public class ChinesePostman {
    public void ChinesePostmanTour(Grafo grafo) {
        List<Vertice> ODD = new ArrayList<>();
        for (Vertice v : grafo.getVertice()) {
            if (grafo.grado(v.getId()) % 2 == 1) {
                ODD.add(v);
            }
        }

        Grafo grafo_copia;

        if (!ODD.isEmpty()) {
            List<Arista> aristas_copia = new ArrayList<>();
            for (Arista a : grafo.getAristas()) {
                aristas_copia.add(new Arista(a.getOrigen(), a.getDestino(), a.getPeso()));
            }

            grafo_copia = new Grafo(grafo.getVertice(), aristas_copia);

            Map<Integer, Map<Integer, Integer>> distanciasEntreODD = new HashMap<>();
            for (Vertice v1 : ODD) {
                Map<Integer, Integer> distanciasDesdeV1 = grafo.dijkstra(v1.getId());
                Map<Integer, Integer> parciales = new HashMap<>();
                for (Vertice v2 : ODD) {
                    if (v1.getId() != v2.getId()) {
                        parciales.put(v2.getId(), distanciasDesdeV1.get(v2.getId()));
                    }
                }
                distanciasEntreODD.put(v1.getId(), parciales);
            }

            List<Integer> idsODD = new ArrayList<>();
            for (Vertice v : ODD) {
                idsODD.add(v.getId());
            }

            List<List<List<Integer>>> todosLosMatchings = generarEmparejamientos(idsODD);
            List<List<Integer>> mejorMatching = encontrarMatchingMinimo(todosLosMatchings, distanciasEntreODD);

            for (List<Integer> par : mejorMatching) {
                int u = par.get(0);
                int v = par.get(1);

                Map<Integer, Integer> predecesores = new HashMap<>();
                grafo.dijkstraConPredecesores(u, predecesores);

                List<Integer> camino = new ArrayList<>();
                int actual = v;

                while (predecesores.containsKey(actual)) {
                    int anterior = predecesores.get(actual);
                    if (anterior != actual) {
                        camino.add(actual);
                        actual = anterior;
                    } else {
                        break;
                    }
                }
                if (!camino.contains(actual)) camino.add(actual);

                for (int i = camino.size() - 1; i > 0; i--) {
                    int origen = camino.get(i);
                    int destino = camino.get(i - 1);
                    Arista original = grafo.buscarAristaEntre(origen, destino);
                    if (original != null) {
                        grafo_copia.agregarArista(origen, destino, original.getPeso());
                        grafo_copia.agregarArista(destino, origen, original.getPeso());
                    }
                }
            }
        } else {
            grafo_copia = grafo;
        }

        for (Vertice v : grafo_copia.getVertice()) {
            System.out.println("Grado final del vertice " + v.getId() + ": " + grafo_copia.grado(v.getId()));
        }

        new Fleury().FleuryAlgorithm(grafo_copia);
    }

    public List<List<List<Integer>>> generarEmparejamientos(List<Integer> vertices) {
        List<List<List<Integer>>> resultados = new ArrayList<>();
        if (vertices.size() == 0) {
            resultados.add(new ArrayList<>());
            return resultados;
        }
        int primero = vertices.get(0);
        List<Integer> restantes = vertices.subList(1, vertices.size());

        for (int i = 0; i < restantes.size(); i++) {
            int emparejado = restantes.get(i);
            List<Integer> par = List.of(primero, emparejado);
            List<Integer> nuevosRestantes = new ArrayList<>(restantes);
            nuevosRestantes.remove(i);
            for (List<List<Integer>> subMatching : generarEmparejamientos(nuevosRestantes)) {
                List<List<Integer>> nuevoMatching = new ArrayList<>();
                nuevoMatching.add(par);
                nuevoMatching.addAll(subMatching);
                resultados.add(nuevoMatching);
            }
        }
        return resultados;
    }

    public List<List<Integer>> encontrarMatchingMinimo(List<List<List<Integer>>> matchings, Map<Integer, Map<Integer, Integer>> distancias) {
        int costoMinimo = Integer.MAX_VALUE;
        List<List<Integer>> mejorMatching = null;
        for (List<List<Integer>> matching : matchings) {
            int costoTotal = 0;
            for (List<Integer> par : matching) {
                int v1 = par.get(0);
                int v2 = par.get(1);
                costoTotal += distancias.get(v1).get(v2);
            }
            if (costoTotal < costoMinimo) {
                costoMinimo = costoTotal;
                mejorMatching = matching;
            }
        }
        return mejorMatching;
    }
}
