import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

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
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                boolean inCD = false;
                String currentColumn = "";
                Set<String> columnNames = new LinkedHashSet<>();
                ArrayList<String> rowData = new ArrayList<>();

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if ("CD".equals(qName)) {
                        inCD = true;
                        rowData.clear();
                    } else if (inCD) {
                        currentColumn = qName;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if ("CD".equals(qName)) {
                        modeloTabla.addRow(rowData.toArray());
                        inCD = false;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (inCD && !currentColumn.isEmpty()) {
                        String value = new String(ch, start, length);
                        rowData.add(value);
                        if (!columnNames.contains(currentColumn)) {
                            columnNames.add(currentColumn);
                            modeloTabla.addColumn(currentColumn);
                        }
                    }
                }

                @Override
                public void startDocument() throws SAXException {
                    modeloTabla.setRowCount(0);
                    modeloTabla.setColumnCount(0);
                }

                @Override
                public void endDocument() throws SAXException {
                    ventana = new JFrame("Visor de XML");
                    ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    JButton botonSalir = new JButton("Salir");
                    botonSalir.addActionListener(e -> System.exit(0));
                    ventana.add(botonSalir, BorderLayout.SOUTH);

                    JScrollPane panelDesplazamiento = new JScrollPane(tabla);
                    ventana.add(panelDesplazamiento, BorderLayout.CENTER);

                    ventana.setSize(800, 600);
                    ventana.setLocationRelativeTo(null);
                    ventana.setVisible(true);
                }
            };

            saxParser.parse(new File(rutaArchivoXML), handler);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el archivo XML :( \n Asegúrate de que el nombre de la ruta sea correcto o que el DTD esté en el directorio :)", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VerXML app = new VerXML();
            app.cargarContenidoXML("src\\cd_catalog.xml");
        });
    }
}