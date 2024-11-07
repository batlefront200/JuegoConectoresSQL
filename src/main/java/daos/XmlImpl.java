
package daos;

import java.io.File;
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

/**
 *
 * @author Vespertino
 */
public class XmlImpl implements XmlDAO{
    
    private String filePath = "./Datos/config.xml";
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

                System.out.println("\nArchivo XML creado en: " + file.getPath()+"\n");
            }
        } catch (ParserConfigurationException | TransformerException e) {
            System.out.println("\n{Error} Error al crear la estructura XML: " + e.getMessage()+"\n");
        } catch (Exception e) {
            System.out.println("\n{Error} Error al crear el archivo: " + e.getMessage()+"\n");
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
                nick_name.appendChild(document.createTextNode(String.valueOf(nick_name.getCoins())));
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
    public void getConfig() {
 
    }
    
}
