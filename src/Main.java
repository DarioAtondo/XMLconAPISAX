import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            // Crear un analizador SAX
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Clase para manejar los eventos SAX
            DefaultHandler handler = new DefaultHandler() {
                boolean inCD = false;
                boolean inPrice = false;
                ArrayList<Double> precios = new ArrayList<>();
                double total = 0;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("CD")) {
                        inCD = true;
                    } else if (qName.equalsIgnoreCase("PRICE")) {
                        inPrice = true;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("CD")) {
                        inCD = false;
                    } else if (qName.equalsIgnoreCase("PRICE")) {
                        inPrice = false;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (inCD && inPrice) {
                        String precioString = new String(ch, start, length);
                        double price = Double.parseDouble(precioString);
                        precios.add(price);
                        total += price;
                    }
                }

                @Override
                public void endDocument() throws SAXException {
                    double promedio = total / precios.size();
                    System.out.println("Promedio de los precios: " + promedio);

                    double sumaDifCuadrados = 0;
                    for (Double price : precios) {
                        double diferencia = price - promedio;
                        sumaDifCuadrados += diferencia * diferencia;
                    }
                    double varianza = sumaDifCuadrados / precios.size();
                    double desvEstandar = Math.sqrt(varianza);
                    System.out.println("Desviación estándar de los precios: " + desvEstandar);
                }
            };

            // Parsear el archivo XML
            saxParser.parse(new File("src\\cd_catalog.xml"), handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}