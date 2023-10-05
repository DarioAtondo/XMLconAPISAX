import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Main {
    public static void main(String[] args) {
        try {
            // Cargar el archivo XML
            File archivo = new File("src\\cd_catalog.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivo);

            // Obtener la lista de nodos CD
            NodeList cdList = doc.getElementsByTagName("CD");

            // Crear un ArrayList para almacenar los precios
            ArrayList<Double> precios = new ArrayList<>();

            // For en los nodos CD y extraer los precios
            for (int i = 0; i < cdList.getLength(); i++) {
                Element cdElement = (Element) cdList.item(i);
                String precioString = cdElement.getElementsByTagName("PRICE").item(0).getTextContent();
                double price = Double.parseDouble(precioString);
                precios.add(price);
            }

            // Calcular la media de los precios
            double total = 0;
            for (Double price : precios) {
                total += price;
            }
            double promedio = total / precios.size();
            System.out.println("Promedio de los precios: " + promedio);

            // Calcular la desviación estándar de los precios
            double sumaDifCuadrados = 0;
            for (Double price : precios) {
                double diferencia = price - promedio;
                sumaDifCuadrados += diferencia * diferencia;
            }
            double varianza = sumaDifCuadrados / precios.size();
            double desvEstandar = Math.sqrt(varianza);
            System.out.println("Desviación estándar de los precios: " + desvEstandar);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

