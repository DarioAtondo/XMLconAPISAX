import javax.swing.*;
import javax.swing.table.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.awt.*;
import java.util.*;

public class VerXML {
    private JFrame ventana;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VerXML() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
    }

    private void cargarContenidoXML(String rutaArchivoXML) {
        try {
            File archivoXML = new File(rutaArchivoXML);

            DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
            DocumentBuilder constructor = fabrica.newDocumentBuilder();
            Document documento = constructor.parse(archivoXML);

            Element elementoRaiz = documento.getDocumentElement();
            NodeList nodos = elementoRaiz.getElementsByTagName("*");

            modeloTabla.setRowCount(0);
            modeloTabla.setColumnCount(0);

            Set<String> nombresColumnas = new LinkedHashSet<>();
            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    String nombreNodo = nodo.getNodeName();
                    nombresColumnas.add(nombreNodo);
                }
            }

            for (String nombreColumna : nombresColumnas) {
                modeloTabla.addColumn(nombreColumna);
            }

            Object[] filaDatos = new Object[nombresColumnas.size()];
            Arrays.fill(filaDatos, "");

            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    String nombreNodo = nodo.getNodeName();
                    String valorNodo = nodo.getTextContent();
                    int indiceColumna = getIndiceColumna(nombreNodo, modeloTabla);
                    if (indiceColumna != -1) {
                        filaDatos[indiceColumna] = valorNodo;
                    }
                }
                if (i > 0 && (i + 1) % nombresColumnas.size() == 0) {
                    modeloTabla.addRow(filaDatos);
                    Arrays.fill(filaDatos, "");
                }
            }

            ventana = new JFrame("Visor de XML");
            ventana.setUndecorated(true);
            ventana.setSize(800, 600);
            ventana.setLayout(new BorderLayout());

            JButton botonSalir = new JButton("Salir");
            botonSalir.addActionListener(e -> System.exit(0));
            ventana.add(botonSalir, BorderLayout.SOUTH);

            JScrollPane panelDesplazamiento = new JScrollPane(tabla);
            ventana.add(panelDesplazamiento, BorderLayout.CENTER);

            Dimension tamanoPantalla = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (tamanoPantalla.width - ventana.getWidth()) / 2;
            int y = (tamanoPantalla.height - ventana.getHeight()) / 2;
            ventana.setLocation(x, y);

            ventana.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el archivo XML :( \\n Asegúrate de que el nombre de la ruta sea correcto o que el DTD esté en el directorio :)", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int getIndiceColumna(String nombreColumna, DefaultTableModel modelo) {
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            if (modelo.getColumnName(i).equals(nombreColumna)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VerXML app = new VerXML();
            app.cargarContenidoXML("C:/Users/cecyh/Downloads/cd_catalog.xml");
        });
    }
}
