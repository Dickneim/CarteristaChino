/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelgrafico;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Map;
import java.util.List;
import graphTDA.*;


public class PanelGraficoAppInside extends JPanel {
    private Grafo grafo;
    private Map<Integer, Point> posicionesPostes;
    private List<Arista> cablesDestacados;
    private Image fondoCiudad;

    public PanelGraficoAppInside(Grafo grafo, Map<Integer, Point> posicionesPostes) {
        this.grafo = grafo;
        this.posicionesPostes = posicionesPostes;
        setPreferredSize(new Dimension(800, 600));
        cargarFondoCiudad();
    }

    private void cargarFondoCiudad() {
        try {
            fondoCiudad = new ImageIcon(getClass().getResource("src\\test\\java\\Fondo2.png")).getImage();
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo de la ciudad
        if (fondoCiudad == null) {
        g2d.setColor(new Color(10, 10, 50)); // Cambia este color al que prefieras
        g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // Dibujar cables (aristas)
        for (Arista a : grafo.getAristas()) {
            Point inicio = posicionesPostes.get(a.getOrigen());
            Point fin = posicionesPostes.get(a.getDestino());
            if (inicio != null && fin != null) {
                if (cablesDestacados != null && cablesDestacados.contains(a)) {
                    g2d.setColor(new Color(255,204,0)); // Dorado semi-transparente para cables destacados
                    g2d.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                } else {
                    g2d.setColor(new Color(51,51,255)); // Gris oscuro semi-transparente para cables normales
                    g2d.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                }
                g2d.draw(new Line2D.Float(inicio, fin));

                // Dibujar longitud del cable
                int midX = (inicio.x + fin.x) / 2;
                int midY = (inicio.y + fin.y) / 2;
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.setColor(Color.WHITE);
                String longitud = a.getPeso() + " m";
                g2d.drawString(longitud, midX, midY);
            }
        }

        // Dibujar postes (vértices)
        int posteSize = 30;
        for (Map.Entry<Integer, Point> entry : posicionesPostes.entrySet()) {
            Point p = entry.getValue();
            // Dibujar poste
            g2d.setColor(new Color(139, 69, 19)); // Marrón para el poste
            g2d.fill(new Rectangle2D.Double(p.x - posteSize / 4, p.y - posteSize / 2, posteSize / 2, posteSize));
            // Dibujar transformador
            g2d.setColor(new Color(169, 169, 169)); // Gris para el transformador
            g2d.fill(new Ellipse2D.Double(p.x - posteSize / 2, p.y - posteSize, posteSize, posteSize / 2));

            // Dibujar número del poste
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            String numeroPoste = "P" + entry.getKey();
            FontMetrics fm = g2d.getFontMetrics();
            int textoX = p.x - fm.stringWidth(numeroPoste) / 2;
            int textoY = p.y + posteSize / 2 + fm.getAscent();
            g2d.drawString(numeroPoste, textoX, textoY);
        }
    }

    public void setAristasDestacadas(List<Arista> destacadas) {
        this.cablesDestacados = destacadas;
        repaint();
    }
}


