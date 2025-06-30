package carterochino;

import graphTDA.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ChinesePostman {
    public void ChinesePostmanTour(Grafo grafo) {
        // Paso 1: Encontrar los vértices de grado impar
        List<Vertice> ODD = new ArrayList<>();
        for (Vertice v : grafo.getVertice()) {
            if (grafo.grado(v.getId()) % 2 == 1) {
                ODD.add(v);
            }
        }

        Grafo grafo_copia;

        if (!ODD.isEmpty()) {
            // Paso 2: Crear copia del conjunto de aristas -> E*
            List<Arista> aristas_copia = new ArrayList<>();
            for (Arista a : grafo.getAristas()) {
                aristas_copia.add(new Arista(a.getOrigen(), a.getDestino(), a.getPeso()));
            }

            // Paso 3: Construir G* = (V, E*) -> copia del grafo original
            grafo_copia = new Grafo(grafo.getVertice(), aristas_copia);

            // Paso 4: Calcular caminos mínimos entre todos los pares de vértices impares
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

            // Paso 5: Generar el grafo bipartito completo implícito entre vértices impares
            List<Integer> idsODD = new ArrayList<>();
            for (Vertice v : ODD) {
                idsODD.add(v.getId());
            }

            List<List<List<Integer>>> todosLosMatchings = generarEmparejamientos(idsODD);

            // Paso 6: Seleccionar el matching perfecto de menor costo total
            List<List<Integer>> mejorMatching = encontrarMatchingMinimo(todosLosMatchings, distanciasEntreODD);

            // Paso 7: Para cada par emparejado, duplicar el camino más corto entre ellos
            for (List<Integer> par : mejorMatching) {
                int u = par.get(0);
                int v = par.get(1);

                // Dijkstra modificado para obtener predecesores (para reconstruir camino)
                Map<Integer, Integer> predecesores = new HashMap<>();
                grafo.dijkstraConPredecesores(u, predecesores);

                // Reconstrucción segura del camino desde v hasta u
                List<Integer> camino = new ArrayList<>();
                int actual = v;
                camino.add(actual);

                while (predecesores.containsKey(actual)) {
                    int anterior = predecesores.get(actual);
                    if (anterior != actual) {
                        camino.add(anterior);
                        actual = anterior;
                    } else {
                        break; // evitar bucles
                    }
                }

                // Debug: mostrar camino reconstruido
                System.out.println("Camino reconstruido entre " + u + " y " + v + ": " + camino);

                // Paso 8: Agregar (duplicar) al grafo copia cada arista del camino mínimo
                for (int i = camino.size() - 1; i > 0; i--) {
                    int origen = camino.get(i);
                    int destino = camino.get(i - 1);
                    if (origen != destino) {
                        Arista original = grafo.buscarAristaEntre(origen, destino);
                        if (original != null) {
                            grafo_copia.agregarArista(origen, destino, original.getPeso());
                            System.out.println("Duplicando camino: " + origen + " -> " + destino);
                        } else {
                            System.out.println("Advertencia: no se encontró arista entre " + origen + " y " + destino);
                        }
                    } else {
                        System.out.println("⚠ Ignorado bucle: " + origen + " -> " + destino);
                    }
                }
            }
        } else {
            // El grafo ya es Euleriano: no se necesita modificar
            grafo_copia = grafo;
        }

        // Paso 9: Realizar un recorrido Euleriano en el grafo modificado (G*) usando Fleury
        new Fleury().FleuryAlgorithm(grafo_copia);
    }

    // Genera todos los emparejamientos posibles entre vértices impares (matching perfecto)
    public List<List<List<Integer>>> generarEmparejamientos(List<Integer> vertices) {
        List<List<List<Integer>>> resultados = new ArrayList<>();

        if (vertices.isEmpty()) {
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

    // Encuentra el emparejamiento con menor costo total entre los vértices impares
    public List<List<Integer>> encontrarMatchingMinimo(
        List<List<List<Integer>>> matchings,
        Map<Integer, Map<Integer, Integer>> distancias
    ) {
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

