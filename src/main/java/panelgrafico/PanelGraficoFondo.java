package panelgrafico;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Map;
import java.util.List;
import graphTDA.*;

public class PanelGraficoFondo extends JPanel {
    private Grafo grafo;
    private Map<Integer, Point> posicionesVertices;
    private List<Arista> aristasDestacadas;
    private Image fondo;

    public PanelGraficoFondo(Grafo grafo, Map<Integer, Point> posicionesVertices, String imagePath) {
        this.grafo = grafo;
        this.posicionesVertices = posicionesVertices;

        try {
            // Cargar la imagen de fondo y escalarla
            Image originalImage = ImageIO.read(new File(imagePath));
            // Escalar la imagen a un tamaño más grande
            fondo = originalImage.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }

        // Aumentar el tamaño preferido del panel
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(800, 600));
        setOpaque(true); // Cambiar a true para asegurar que se pinte todo el fondo
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar la imagen de fondo para que llene todo el panel
        
        if (fondo != null) {
            // Activar antialiasing para mejor calidad
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Dibujar la imagen escalada para llenar todo el panel
            g2d.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
        
        g2d.setColor(new Color(200, 200, 200)); // Color gris claro para el grid
        int gridSize = 50; // Tamaño del espacio entre líneas del grid

    
        
        //crea antialiasing para aristas y vertices
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar las aristas
        for (Arista a : grafo.getAristas()) {
            Point p1 = posicionesVertices.get(a.getOrigen());
            Point p2 = posicionesVertices.get(a.getDestino());
            if (p1 != null && p2 != null) {
                if (aristasDestacadas != null && aristasDestacadas.contains(a)) {
                    g2d.setColor(Color.RED);
                    g2d.setStroke(new BasicStroke(3.0f)); // Línea más gruesa para aristas destacadas
                } else {
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2.0f));
                }
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);

                // Dibujar el peso de la arista con mejor visibilidad
                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.setColor(Color.BLUE);
                g2d.drawString(String.valueOf(a.getPeso()), midX, midY);
            }
        }

        // Dibujar los vértices con mejor visibilidad
        for (Map.Entry<Integer, Point> entry : posicionesVertices.entrySet()) {
            Point p = entry.getValue();
            // Dibujar círculo exterior
            g2d.setColor(Color.WHITE);
            g2d.fillOval(p.x - 12, p.y - 12, 24, 24);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawOval(p.x - 12, p.y - 12, 24, 24);
            
            // Dibujar número del vértice
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            String numeroVertice = String.valueOf(entry.getKey());
            int textoX = p.x - g2d.getFontMetrics().stringWidth(numeroVertice) / 2;
            int textoY = p.y + g2d.getFontMetrics().getAscent() / 2 - 2;
            g2d.drawString(numeroVertice, textoX, textoY);
        }
    }

    // Método para establecer aristas destacadas
    public void setAristasDestacadas(List<Arista> destacadas) {
        this.aristasDestacadas = destacadas;
        repaint();
    }
}