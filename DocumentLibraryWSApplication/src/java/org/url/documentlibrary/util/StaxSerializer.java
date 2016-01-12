
package org.url.documentlibrary.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.url.documentlibrary.model.Document;
import org.url.documentlibrary.model.Section;
import org.xml.sax.SAXException;

/**
 * StaxSerializer est la classe qui permet de valider et sérialiser/désérialiser du contenu XML
 * 
 * @author Quentin NAUD, Benjamin NEILZ
 * @version 1.0
 */
public class StaxSerializer {
    
    /**
     * Nom du schéma XSD présent sur le serveur
     */
    private static String SCHEMA_NAME = "schema.xsd";
    
    /**
     * Cette méthode permet de transformer (désérialiser) un contenu XML binarisé sous forme d'objet Document
     * Le fichier XML est tout d'abord validé via un schéma XSD
     * 
     * @param name Nom du fichier associé au contenu binaire
     * @param byteArray Contenu binaire à désérialiser
     * @return Objet résultat de la désérialisation de type Document
     * @throws XMLStreamException
     * @throws IOException 
     * @throws SAXException 
     */
    public static Document unserialize(String name, byte[] byteArray) throws XMLStreamException, IOException, SAXException{
        
        // Chargement du reader XML, flux de lecture du contenu XML binarisé
        InputStream bis = new ByteArrayInputStream(byteArray);
        Source source = new StreamSource(new InputStreamReader(bis, "UTF-8"));
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(source);

        // Construction de la fabrique
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        
        // Chargement du schéma XSD
        Schema schema = factory.newSchema(new File(StaxSerializer.SCHEMA_NAME));

        // Validation du fichier transmis par rapport au schéma
        // Si fichier valide = désérialisation
        // Si fichier non valide = exception levée
        Validator validator = schema.newValidator();
        validator.validate(new StAXSource(reader));
        
        // Initialisation des variables
        Document document = new Document(name);
        int eventType;
        boolean loop = reader.hasNext();
        
        // Parcours du XML
        while (loop) {
            eventType = reader.next();
            // Focus uniquement sur les éléments ouvrants
            if (eventType == XMLEvent.START_ELEMENT) {
                // Récupération du nom de l'élément
                switch(reader.getLocalName()){
                    case "titre" : 
                        // Modification du titre du document
                        reader.next();  
                        document.setTitre(reader.getText());
                    break;  
                    case "resume" : 
                        // Modification du résumé du document
                        reader.next();  
                        document.setResume(reader.getText());
                    break;    
                    case "motcle" : 
                        // Ajout de mots cles au document
                        reader.next();  
                        document.addMotCles(reader.getText());
                    break;  
                    case "section" : 
                        // Ajout de sections au document
                        if(reader.getAttributeLocalName(0).equals("titre")){
                            String attrValue = reader.getAttributeValue(0);
                            reader.next(); 
                            // Création d'un objet section avec l'attribut titre et le contenu texte
                            Section section = new Section(attrValue, reader.getText());
                            // Ajout de la section au document
                            document.addSection(section);
                        }
                    break; 
                }
            }
            // Fin du XML
            if (!reader.hasNext()) {
                loop = false;
            }
        }
        
        return document;
    }
}
