/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelgrafico;

import java.awt.*;
import java.util.Map;
import javax.swing.*;
import java.util.List;
import graphTDA.*;
import java.util.HashMap;

/**
 *
 * @author Walter
 */
public class PanelGrafico extends JPanel {
    private Grafo grafo; // El grafo que vas a dibujar
    private Map<Integer, Point> posicionesVertices; // Posiciones de los vértices
    private List<Arista> aristasDestacadas; // Opcional: aristas a destacar
    private Map<Arista, Color> coloresAristas;

    public PanelGrafico(Grafo grafo, Map<Integer, Point> posicionesVertices) {
        this.grafo = grafo;
        this.posicionesVertices = posicionesVertices;
        setPreferredSize(new Dimension(400, 300)); // Dimensiones por defecto
        setBackground(Color.WHITE); // Fondo claro para el panel
        setOpaque(true);
    }

    // Método para establecer aristas destacadas (opcional)
    public void setAristasDestacadas(List<Arista> destacadas, Map<Arista, Color> color) {
        this.aristasDestacadas = destacadas;
        this.coloresAristas = (color!=null) ? color:new HashMap<>();
        repaint(); // Redibuja el panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibuja las aristas
        for (Arista a : grafo.getAristas()) {
            Point p1 = posicionesVertices.get(a.getOrigen());
            Point p2 = posicionesVertices.get(a.getDestino());
            if (p1 != null && p2 != null) {
                // Determinar el color de la arista
                Color colorArista;
                if (aristasDestacadas != null && aristasDestacadas.contains(a)) {
                    // Usar color personalizado si existe, o rojo por defecto
                    colorArista = coloresAristas.getOrDefault(a, Color.RED);
                    g2d.setStroke(new BasicStroke(3.0f)); // Más gruesa para aristas destacadas
                } else {
                    colorArista = Color.BLACK;
                    g2d.setStroke(new BasicStroke(2.0f));
                }
                
                g2d.setColor(colorArista);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);

                // Dibujar el peso de la arista
                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;
                g2d.setColor(Color.BLUE);
                g2d.drawString(String.valueOf(a.getPeso()), midX, midY);
            }
        }

        // Dibuja los vértices (igual que antes)
        for (Map.Entry<Integer, Point> entry : posicionesVertices.entrySet()) {
            Point p = entry.getValue();
            g2d.setColor(Color.BLACK);
            g2d.fillOval(p.x - 10, p.y - 10, 20, 20);
            g2d.setColor(Color.WHITE);
            String numeroVertice = String.valueOf(entry.getKey());
            int textoX = p.x - g2d.getFontMetrics().stringWidth(numeroVertice) / 2;
            int textoY = p.y + g2d.getFontMetrics().getAscent() / 2 - 2;
            g2d.drawString(numeroVertice, textoX, textoY);
        }
    }
    
    public void setGrafo(Grafo nuevoGrafo) {
        this.grafo = nuevoGrafo;
        repaint();
    }
}
