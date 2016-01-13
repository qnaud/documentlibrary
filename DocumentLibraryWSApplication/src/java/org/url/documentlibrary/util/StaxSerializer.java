
package org.url.documentlibrary.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
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
     * Espace de noms par défaut
     */
    private final static String DEFAULT_NAMESPACE = "http://www.doc.org";
    
    /**
     * Nom du schéma XSD présent sur le serveur
     */
    private final static String SCHEMA_NAME = "schema.xsd";
            
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
        
        // Rechargement du reader
        bis = new ByteArrayInputStream(byteArray);
        source = new StreamSource(new InputStreamReader(bis, "UTF-8"));
        reader = XMLInputFactory.newInstance().createXMLStreamReader(source);
        
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
        
        // Retour de l'instance de Document 
        return document;
    }

    /**
     * Cette méthode permet de transformer (sérialiser) une instance de Document en XML
     * 
     * @param document Instance d'objet Document à sérialiser (transformer en fichier XML)
     * @return Contenu XML après sérialisation de l'instance de Document
     * @throws XMLStreamException
     */
    public static String serialize(Document document) throws XMLStreamException{
        
        // Initialisation du flux d'écriture du contenu XML
        StringWriter strw = new StringWriter();
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(strw);
        
        // Début du fichier XML
        writer.writeStartDocument();
        writer.writeStartElement("article");
        // <article>
            writer.writeDefaultNamespace(StaxSerializer.DEFAULT_NAMESPACE);
            writer.writeNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
            writer.writeAttribute("http://www.w3.org/2001/XMLSchema-instance","schemaLocation",StaxSerializer.DEFAULT_NAMESPACE+" "+StaxSerializer.SCHEMA_NAME);
            writer.writeStartElement("titre");
            // <titre>
                writer.writeCharacters(document.getTitre());
            // </titre>
            writer.writeEndElement();
            writer.writeStartElement("resume");
            // <resume>
                writer.writeCharacters(document.getResume());
            // </resume>
            writer.writeEndElement();
            writer.writeStartElement("motcles");
            // <motcles>
                for(String motCles : document.getMotCles()){
                    writer.writeStartElement("motcle");
                // <motcle>
                        writer.writeCharacters(motCles);
                // </motcle>
                    writer.writeEndElement();
                }
            // </motcles>
            writer.writeEndElement();
            for(Section section : document.getSections()){
                writer.writeStartElement("section");
            // <section>
                    writer.writeAttribute("titre",section.getTitre());
                    writer.writeCharacters(section.getContenu());
            // </section>
                writer.writeEndElement();
            }
        // </article>
        writer.writeEndElement();
        writer.flush();
        
        // Retour du contenu XML
        return strw.toString();
    }

}
