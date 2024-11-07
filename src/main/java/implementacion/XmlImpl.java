
package implementacion;

import clases.Jugador;
import daos.XmlDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Vespertino
 */
public class XmlImpl implements XmlDAO {

    private String filePath = "./Configuracion/config.xml";

    private void crearArchivoSiNoExiste() {
        try {
            // Crear el directorio Datos si no existe
            File dir = new File("./Datos");
            if (!dir.exists()) {
                dir.mkdirs(); // Crear el directorio si no existe
                System.out.println("\nDirectorio 'Datos' creado.\n");
            }

            // Crear el archivo XML dentro del directorio Datos
            File file = new File(filePath); // Ajustar la ruta al archivo dentro del directorio
            if (!file.exists()) {
                System.out.println("\nArchivo de jugadores no encontrado. Creando archivo...\n");

                // Crear la estructura básica del XML
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                Element root = document.createElement("configuracion"); // Nodo raíz del XML
                document.appendChild(root);

                // Guardar la estructura inicial en el archivo
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Formato con sangrías
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);

                System.out.println("\nArchivo XML creado en: " + file.getPath() + "\n");
            }
        } catch (ParserConfigurationException | TransformerException e) {
            System.out.println("\n{Error} Error al crear la estructura XML: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("\n{Error} Error al crear el archivo: " + e.getMessage() + "\n");
        }
    }

    @Override
    public void saveConfig() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element raiz = document.createElement("configuracion");
            document.appendChild(raiz);

            Element serverElement = document.createElement("servidor");
            Element userElement = document.createElement("usuario");

            //Elemetos del servidor
            Element host = document.createElement("host");
            host.appendChild(document.createTextNode("127.0.0.1"));
            serverElement.appendChild(host);

            Element port = document.createElement("port");
            port.appendChild(document.createTextNode("3306"));
            serverElement.appendChild(port);

            Element user = document.createElement("user");
            user.appendChild(document.createTextNode("root"));
            serverElement.appendChild(user);

            Element pass = document.createElement("pass");
            pass.appendChild(document.createTextNode("root"));
            serverElement.appendChild(pass);

            //Elementos del usuario
            Element nick_name = document.createElement("nick_name");
            nick_name.appendChild(document.createTextNode("Kimura"));
            userElement.appendChild(nick_name);

            raiz.appendChild(serverElement);
            raiz.appendChild(userElement);

            // Guardar el documento XML actualizado
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source source = new DOMSource(document);

            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String [] getConfig() {
        String [] datosConfig = new String[5];
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null; // Archivo no encontrado
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodeList = document.getElementsByTagName("jugador");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    datosConfig[0] = element.getElementsByTagName("host").item(0).getTextContent();
                    datosConfig[1] = element.getElementsByTagName("port").item(0).getTextContent();
                    datosConfig[2] = element.getElementsByTagName("user").item(0).getTextContent();
                    datosConfig[3] = element.getElementsByTagName("pass").item(0).getTextContent();
                    datosConfig[4] = element.getElementsByTagName("nick_name").item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            System.out.println("\n{Error} No se encontró el archivo o hubo un error al cargar los jugadores.+\n");
        }
        return datosConfig;
    }

}
