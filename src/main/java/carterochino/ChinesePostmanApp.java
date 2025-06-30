/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package carterochino;
import graphTDA.*;
import panelgrafico.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class ChinesePostmanApp extends javax.swing.JPanel {
    private Grafo grafo;
    private int idVertice = 1;
    private boolean modoInsertarVertice = false;
    private boolean modoInsertarArista = false;
    private Vertice verticeSeleccionado1 = null;
    private Vertice verticeSeleccionado2 = null;
    private Map<Integer, Point> posicionesVertices;
    private PanelGrafico panelGrafico;
    private PanelGraficoFondo panelGraficoAp;
    private PanelGraficoAppInside panelGrafico2;
    /**
     * Creates new form ChinesePostmanApp
     */
    public ChinesePostmanApp() {
        initComponents();
        grafo = new Grafo();
        posicionesVertices = new HashMap<>();
        
        panelGrafico = new PanelGrafico(grafo, posicionesVertices);
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add(panelGrafico, BorderLayout.CENTER);
        
        // Specify the path to your background image
        String imagePath = "src\\test\\java\\Fondo1.png";
        panelGraficoAp = new PanelGraficoFondo(grafo, posicionesVertices, imagePath);
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(panelGraficoAp, BorderLayout.CENTER);
        
        panelGrafico2 = new PanelGraficoAppInside(grafo, posicionesVertices);
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(panelGrafico2, BorderLayout.CENTER);
        
        jPanel1.setVisible(false);
        jPanel3.setVisible(true);
        
        pack();
    }
    
    private void BorrarVerticeActionPerformed(java.awt.event.ActionEvent evt) {                                              
        Info.setText("Haga clic en un vértice para eliminarlo.");
        
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Vertice verticeCercano = obtenerVerticeCercano(evt.getX(), evt.getY());
                if (verticeCercano != null) {
                    int id = verticeCercano.getId();
                    if (grafo.borrarVertice(id)) {
                        posicionesVertices.remove(id); // Elimina la posición del vértice
                        panelGrafico.repaint();
                        panelGrafico2.repaint();
                        Info.setText("Vértice " + id + " borrado con éxito.");
                    } else {
                        Info.setText("No se pudo borrar el vértice " + id + ".");
                    }
                } else {
                    Info.setText("No se encontró ningún vértice cerca del clic.");
                }
                // Elimina el listener para evitar conflictos
                jPanel4.removeMouseListener(this);
            }
        });
       
    }                                             

    private void BorrarAristaActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
         String input = JOptionPane.showInputDialog(this, "Ingrese los IDs de los vértices de la arista a borrar (separados por coma):");
        try {
            String[] ids = input.split(",");
            int id1 = Integer.parseInt(ids[0].trim());
            int id2 = Integer.parseInt(ids[1].trim());
            if (grafo.borrarArista(id1, id2)) {
                actualizarGrafico();
                Info.setText("Arista entre " + id1 + " y " + id2 + " borrada con éxito.");
            } else {
                Info.setText("No se pudo borrar la arista entre " + id1 + " y " + id2 + ".");
            }
        } catch (Exception e) {
            Info.setText("Entrada inválida. Use el formato: id1, id2");
        }
    }                                            

    private void Salir2ActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // TODO add your handling code here:
        System.exit(0);
    }                                      

    private void ShowAplicativoActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
        jPanel3.setVisible(false); // Oculta jPanel3
        jPanel1.setVisible(true);  // Muestra jPanel1
        
    }                                              

    private void ShowTeoriaActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        jPanel1.setVisible(false); // Oculta jPanel1
        jPanel3.setVisible(true);  // Muestra jPanel3
    }                                          

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {                                      
        // TODO add your handling code here:
        System.exit(0);
    }                                     

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
        if (modoInsertarVertice) {
            agregarVerticeGraficamente(evt.getX(), evt.getY());
            modoInsertarVertice = false;
        } else if (modoInsertarArista) {
            Vertice v = obtenerVerticeCercano(evt.getX(), evt.getY());
            if (v != null) {
                if (verticeSeleccionado1 == null) {
                    verticeSeleccionado1 = v;
                    Info.setText("Seleccione el segundo vértice para la arista.");
                } else {
                    verticeSeleccionado2 = v;
                    if (verticeSeleccionado1.getId() == verticeSeleccionado2.getId()) {
                        Info.setText("No puedes agregar una arista entre un vértice y él mismo. Intente de nuevo");
                        verticeSeleccionado1 = null;
                        verticeSeleccionado2 = null;
                        return; // Detener aquí, no agregar la arista
                    }
                    String pesoStr = JOptionPane.showInputDialog(this, "Ingrese el peso de la arista:");
                    try {
                        int peso = Integer.parseInt(pesoStr);
                        grafo.agregarArista(verticeSeleccionado1.getId(), verticeSeleccionado2.getId(), peso);
                        actualizarGrafico();
                        Info.setText("Arista agregada con éxito.");
                    } catch (NumberFormatException e) {
                        Info.setText("Por favor, ingrese un número válido para el peso.");
                    }
                    verticeSeleccionado1 = null;
                    verticeSeleccionado2 = null;
                    modoInsertarArista = false;
                }
            }
        }
        
    }                                    

    private void InsertarAristaActionPerformed(java.awt.event.ActionEvent evt) {                                               
        if (posicionesVertices.isEmpty()) {
            Info.setText("No hay suficientes vértices para crear una arista.");
            return;
        }

        if (posicionesVertices.size() < 2) {
            Info.setText("Debe haber al menos 2 vértices para crear una arista.");
            return;
        }
           
        modoInsertarArista = true; // Define un modo para insertar aristas
        Info.setText("Seleccione el primer vértice para la arista.");
        jPanel4.repaint();
        jPanel2.repaint();
        
        
    }                                              

    private void InsertarVerticeActionPerformed(java.awt.event.ActionEvent evt) {                                                

        modoInsertarVertice = true;
        Info.setText("Haga clic en el panel para insertar un vértice.");

    }                                               

    private void KruskalActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
        List<Arista> arbolExpansionMinima = grafo.kruskal();
        if (arbolExpansionMinima.isEmpty()) {
            Info.setText("No se pudo generar el árbol de expansión mínima.");
        } else {
            actualizarGrafico(arbolExpansionMinima);
            Info.setText("Árbol de expansión mínima generado con éxito. \nEl costo minimo es: "+grafo.sumPeso);
            costoMin.setText("S/."+grafo.sumPeso*12+" de Cable para instalar.");
            grafo.sumPeso=0;
        }
    }                                       

    private void ReiniciarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        grafo = new Grafo();
        posicionesVertices.clear();
        idVertice = 1;

        panelGrafico.setAristasDestacadas(null);
        panelGrafico2.setAristasDestacadas(null);
  

        // Create new instances of the panels with the reset graph
        panelGrafico = new PanelGrafico(grafo, posicionesVertices);
        panelGrafico2 = new PanelGraficoAppInside(grafo, posicionesVertices);


        
        // Remove old panels and add new ones
        jPanel4.removeAll();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add(panelGrafico, BorderLayout.CENTER);
        jPanel2.removeAll();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(panelGrafico2, BorderLayout.CENTER);
 

        // Revalidate and repaint all panels
        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel2.revalidate();
        jPanel2.repaint();
  

        Info.setText("Grafo reiniciado.");
    }                                         

    private void Reiniciar2ActionPerformed(java.awt.event.ActionEvent evt) {                                           
             grafo = new Grafo();
        posicionesVertices.clear();
        idVertice = 1;

        panelGrafico.setAristasDestacadas(null);
        panelGrafico2.setAristasDestacadas(null);
  

        // Create new instances of the panels with the reset graph
        panelGrafico = new PanelGrafico(grafo, posicionesVertices);
        panelGrafico2 = new PanelGraficoAppInside(grafo, posicionesVertices);


        
        // Remove old panels and add new ones
        jPanel4.removeAll();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add(panelGrafico, BorderLayout.CENTER);
        jPanel2.removeAll();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(panelGrafico2, BorderLayout.CENTER);
 

        // Revalidate and repaint all panels
        jPanel4.revalidate();
        jPanel4.repaint();
        jPanel2.revalidate();
        jPanel2.repaint();
  

        Info.setText("Grafo reiniciado.");
    }                                          

    private void EliminarParaderoActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        Info.setText("Haga clic en un paradero para eliminarlo.");

        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Vertice verticeCercano = obtenerVerticeCercano(evt.getX(), evt.getY());
                if (verticeCercano != null) {
                    int id = verticeCercano.getId();
                    if (grafo.borrarVertice(id)) {
                        posicionesVertices.remove(id); // Elimina la posición del vértice
                        actualizarGrafico();
                        Info.setText("Paradero " + id + " eliminado con éxito.");
                    } else {
                        Info.setText("No se pudo eliminar el paradero " + id + ".");
                    }
                } else {
                    Info.setText("No se encontró ningún paradero cerca del clic.");
                }
                jPanel2.removeMouseListener(this); // Elimina el listener después de la acción
            
            }
        });
    }                                                

    private void AñadirParaderoActionPerformed(java.awt.event.ActionEvent evt) {                                               

        modoInsertarVertice = true;
        Info.setText("Haga clic en el panel para agregar un paradero.");
        jPanel4.repaint();
        jPanel2.repaint();
    }                                              

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
        if (modoInsertarVertice) {
            agregarVerticeGraficamente(evt.getX(), evt.getY());
            modoInsertarVertice = false;
        } else if (modoInsertarArista) {
            Vertice v = obtenerVerticeCercano(evt.getX(), evt.getY());
            if (v != null) {
                if (verticeSeleccionado1 == null) {
                    verticeSeleccionado1 = v;
                    Info.setText("Seleccione el segundo vértice para la arista.");
                } else {
                    verticeSeleccionado2 = v;
                    if (verticeSeleccionado1.getId() == verticeSeleccionado2.getId()) {
                        Info.setText("No puedes agregar una arista entre un vértice y él mismo. Intente de nuevo");
                        verticeSeleccionado1 = null;
                        verticeSeleccionado2 = null;
                        return; // Detener aquí, no agregar la arista
                    }
                    String pesoStr = JOptionPane.showInputDialog(this, "Ingrese el peso de la arista:");
                    try {
                        int peso = Integer.parseInt(pesoStr);
                        grafo.agregarArista(verticeSeleccionado1.getId(), verticeSeleccionado2.getId(), peso);
                        actualizarGrafico();
                        Info.setText("Arista agregada con éxito.");
                    } catch (NumberFormatException e) {
                        Info.setText("Por favor, ingrese un número válido para el peso.");
                    }
                    verticeSeleccionado1 = null;
                    verticeSeleccionado2 = null;
                    modoInsertarArista = false;
                }
            }
        }    }                                    

    private void AñadirAvenidaActionPerformed(java.awt.event.ActionEvent evt) {                                              
        modoInsertarArista = true; // Define un modo para insertar aristas
        Info.setText("Seleccione el primer vértice para la arista.");
    }                                             

    private void EliminarAvenidaActionPerformed(java.awt.event.ActionEvent evt) {                                                
         String input = JOptionPane.showInputDialog(this, "Ingrese los IDs de los paraderos de la avenida a borrar (separados por coma):");
        try {
            String[] ids = input.split(",");
            int id1 = Integer.parseInt(ids[0].trim());
            int id2 = Integer.parseInt(ids[1].trim());
            if (grafo.borrarArista(id1, id2)) {
                actualizarGrafico();
                Info.setText("Arista entre " + id1 + " y " + id2 + " borrada con éxito.");
            } else {
                Info.setText("No se pudo borrar la arista entre " + id1 + " y " + id2 + ".");
            }
        } catch (Exception e) {
            Info.setText("Entrada inválida. Use el formato: id1, id2");
        }
    }                                               

    private void CalcularCaminoEficienteActionPerformed(java.awt.event.ActionEvent evt) {                                                        
        List<Arista> arbolExpansionMinima = grafo.kruskal();
        if (arbolExpansionMinima == null || arbolExpansionMinima.isEmpty()) {
            Info.setText("No se pudo generar el árbol de expansión mínima.");
        } else {
            actualizarGrafico(arbolExpansionMinima);
            Info.setText("Árbol de expansión mínima generado con éxito. \nEl costo minimo es: "+grafo.sumPeso);
            costoMin.setText("S/."+grafo.sumPeso*12+" de Cable para instalar.");
            grafo.sumPeso=0;
        }        
    }                                                       
private void agregarVerticeGraficamente(int x, int y) {
    for (Point p : posicionesVertices.values()) {
        if (Math.abs(p.x - x) <= 20 && Math.abs(p.y - y) <= 20) { // Distancia mínima para evitar solapamiento
            Info.setText("Ya existe un vértice cerca de esta posición. Intente en otro lugar.");
            return; // No agrega el vértice
        }
    }
    grafo.agregarVertice(idVertice);
    posicionesVertices.put(idVertice, new Point(x, y));
    
    Graphics g = jPanel4.getGraphics();
    Graphics g2 = jPanel2.getGraphics();
    
    g.setColor(Color.BLACK);
    g.fillOval(x - 10, y - 10, 20, 20);
    g.setColor(Color.WHITE);
    g2.setColor(Color.BLACK);
    g2.fillOval(x - 10, y - 10, 20, 20);
    g2.setColor(Color.WHITE);
    
    String numeroVertice = String.valueOf(idVertice);
    int textoX = x - g.getFontMetrics().stringWidth(numeroVertice) / 2;
    int textoY = y + g.getFontMetrics().getAscent() / 2 - 2;
    g.drawString(numeroVertice, textoX, textoY);
    g2.drawString(numeroVertice, textoX, textoY);

        
    grafo.agregarVertice(idVertice);
    posicionesVertices.put(idVertice, new Point(x, y));
    idVertice++;
    
    Info.setText("Vértice " + (idVertice - 1) + " agregado.");
    actualizarGrafico();
}
private void actualizarGrafico() {
        panelGrafico.repaint();
        panelGrafico2.repaint();
    }
private void actualizarGrafico(List<Arista> destacadas) {
    panelGrafico.setAristasDestacadas(destacadas);
    panelGrafico2.setAristasDestacadas(destacadas);
    panelGrafico.repaint();
    panelGrafico2.repaint();
}

private Vertice obtenerVerticeCercano(int x, int y) {
        for (Map.Entry<Integer, Point> entry : posicionesVertices.entrySet()) {
            Point p = entry.getValue();
            if (Math.abs(p.x - x) <= 10 && Math.abs(p.y - y) <= 10) {
                return grafo.getVertice(entry.getKey());
            }
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChinesePostmanApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new ChinesePostmanApp().setVisible(true);
        });
    }
    
    

    // Variables declaration - do not modify                     
    private javax.swing.JButton AñadirAvenida;
    private javax.swing.JButton AñadirParadero;
    private javax.swing.JButton BorrarArista;
    private javax.swing.JButton BorrarVertice;
    private javax.swing.JButton CalcularCaminoEficiente;
    private javax.swing.JButton EliminarAvenida;
    private javax.swing.JButton EliminarParadero;
    private javax.swing.JTextArea Info;
    private javax.swing.JButton InsertarArista;
    private javax.swing.JButton InsertarVertice;
    private javax.swing.JButton Kruskal;
    private javax.swing.JButton Reiniciar;
    private javax.swing.JButton Reiniciar2;
    private javax.swing.JButton Salir;
    private javax.swing.JButton Salir2;
    private javax.swing.JButton ShowAplicativo;
    private javax.swing.JButton ShowTeoria;
    private javax.swing.JLabel costoMin;
    private javax.swing.JColorChooser jColorChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration                   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 688, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(298, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        // TODO add your handling code here:
        if (modoInsertarVertice) {
            agregarVerticeGraficamente(evt.getX(), evt.getY());
            modoInsertarVertice = false;
        } else if (modoInsertarArista) {
            Vertice v = obtenerVerticeCercano(evt.getX(), evt.getY());
            if (v != null) {
                if (verticeSeleccionado1 == null) {
                    verticeSeleccionado1 = v;
                    Info.setText("Seleccione el segundo vértice para la arista.");
                } else {
                    verticeSeleccionado2 = v;
                    if (verticeSeleccionado1.getId() == verticeSeleccionado2.getId()) {
                        Info.setText("No puedes agregar una arista entre un vértice y él mismo. Intente de nuevo");
                        verticeSeleccionado1 = null;
                        verticeSeleccionado2 = null;
                        return; // Detener aquí, no agregar la arista
                    }
                    String pesoStr = JOptionPane.showInputDialog(this, "Ingrese el peso de la arista:");
                    try {
                        int peso = Integer.parseInt(pesoStr);
                        grafo.agregarArista(verticeSeleccionado1.getId(), verticeSeleccionado2.getId(), peso);
                        actualizarGrafico();
                        Info.setText("Arista agregada con éxito.");
                    } catch (NumberFormatException e) {
                        Info.setText("Por favor, ingrese un número válido para el peso.");
                    }
                    verticeSeleccionado1 = null;
                    verticeSeleccionado2 = null;
                    modoInsertarArista = false;
                }
            }
    }//GEN-LAST:event_jPanel2MouseClicked

    private void pack() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
