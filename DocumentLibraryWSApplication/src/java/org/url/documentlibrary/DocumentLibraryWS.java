
package org.url.documentlibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.xml.stream.XMLStreamException;
import org.url.documentlibrary.model.Document;
import org.url.documentlibrary.util.DocumentMap;
import org.url.documentlibrary.util.PDF;
import org.url.documentlibrary.util.StaxSerializer;
import org.xml.sax.SAXException;

/**
 * DocumentLibraryWS est la classe qui définie le service web homonyme :
 * - "demarrage" est exécuté au déploiement du WS et permet de désérialiser les fichiers XML stockés
 * - "depotDocument" stocke un fichier XML sur le serveur (opération)
 * - "rechercheDocument" recherche des documents par mots-clés (opération)
 * - "retourneDocument" recherche un document en fonction d'un identifiant (opération)
 * - "generePDF" recherche et retourne un document au format PDF (opération)
 * - "arret" est exécuté à l'arrêt du WS et permet de sérialiser la collection de documents
 * - "addDocument" permet d'ajouter un document à la collection de documents
 * 
 * @author Quentin NAUD, Benjamin NEILZ
 * @version 1.0
 */
@WebService(serviceName = "DocumentLibraryWS")
@Stateless()
@LocalBean()
public class DocumentLibraryWS {
    
    /**
     * Répertoire pour stocker les fichiers 
     */
    private static final String FILES_REPOSITORY = System.getProperty("catalina.base") + File.separator + "files";
    
    /**
     * Répertoire de stockage des fichiers
     */
    private File folder;
    
    /**
     * Collection de documents (après désérialisation des fichiers XML)
     */
    private Map<Integer, Document> documents;
    
    /**
     * Cette méthode permet, entre autres, d'initialiser la liste de documents, le répertoire d'uplaod et de désérialiser les fichiers XML déjà stockés.
     * Elle est appelée au démarrage du web service.
     */
    @PostConstruct
    public void demarrage() {
        try{
            // Initialisation de la liste de documents (dictionnaire chaîné)
            this.documents = new LinkedHashMap<>();
            
            // Création du répertoire "files" sur le serveur si il n'existe pas déjà
            // Ce répertoire permet de stocker les documents uploadés par le client
            this.folder = new File(DocumentLibraryWS.FILES_REPOSITORY);
            this.folder.mkdir();

            // Récupération des fichiers du répertoire
            File[] files = this.folder.listFiles();

            if (files != null) {
                // Parcours des fichiers
                for (File file : files) {
                    // Focus sur les fichiers uniquement (pas les répertoires)
                    if (!file.isDirectory()) {
                        String fichier = file.getName();
                        // Focus sur les fichiers de type XML uniquement
                        if (fichier.substring(fichier.length() - 3).equals("xml")) {
                            // Contenu du fichier courant
                            String fileStr = new Scanner(file).useDelimiter("\\Z").next();
                            // Désérialisation
                            Document document = StaxSerializer.unserialize(fichier, fileStr.getBytes());
                            // Il s'agit d'un document stocké sur le serveur
                            document.setStocke(true);
                            // Ajout du document à la liste
                            this.addDocument(document);
                        }
                    }
                }
            }
        }catch(XMLStreamException | IOException | SAXException ex){}
    }
    
    /**
     * Cette méthode permet d'uploader un fichier XML sur le serveur et de l'indexer.
     * Méthode du web service.
     * 
     * @param nom Nom du fichier (nom + extension)
     * @param contenu Contenu XML du fichier en binaire
     * @return Index du document stocké
     * @throws Exception 
     */
    @WebMethod(operationName = "depotDocument")
    public int depotDocument(@WebParam(name = "nom") String nom, @WebParam(name = "contenu") byte[] contenu) throws Exception {

        // Désérialisation du fichier XML
        Document doc = StaxSerializer.unserialize(nom, contenu);
        
        // Indexation du document
        return this.addDocument(doc);
    }

    /**
     * Cette méthode permet à partir d'une liste de motsclés fournis, de retourner les identifiants et les titres des documents comportant ces mots-clés.
     * Méthode du web service.
     * 
     * @param motsClesFiltres Liste des mots clés à rechercher
     * @param condition Caractère indiquant si la condition est "ET" (défaut) ou "OU"
     * @return Liste des documents trouvés (HashMap<identifiant,titre>)
     */
    @WebMethod(operationName = "rechercheDocument")
    public DocumentMap rechercheDocument(@WebParam(name = "motsCles") String[] motsClesFiltres, @WebParam(name = "condition") String condition) {

        // Collection de documents trouvés (key : identifiant, value : titre)
        DocumentMap foundDocuments = new DocumentMap();
        Document document;
        
        // OU
        if(condition != null && condition.toUpperCase().equals("OU")){
            boolean isFind;
            // Parcourir tous les documents
            for(Entry<Integer, Document> documentEntry : this.documents.entrySet()){
                isFind = false;
                // Document courant
                document = documentEntry.getValue();
                // Boucle sur les mots clés recherchés
                for(String motCleFiltre : motsClesFiltres){
                    // Boucle sur les mots clés du document courant
                    for(String motCle : document.getMotCles()){
                        // Recherche de correspondance entre les mots clés
                        if(motCle.equals(motCleFiltre)){
                            // Si au moins 1 mot clé correspond, mapping du document et sortie de boucles
                            foundDocuments.getRealMap().put(documentEntry.getKey(), document.getTitre());
                            isFind = true;
                            break;
                        }
                    }
                    if(isFind) 
                        break;
                }
            }
        }
        // ET est la condition par défaut
        else{
            int nombreDeCorrespondance;
            for(Entry<Integer, Document> documentEntry : this.documents.entrySet()){
                nombreDeCorrespondance = 0;
                document = documentEntry.getValue();
                for(String motCleFiltre : motsClesFiltres){
                    for(String motCle : document.getMotCles()){
                        if(motCle.equals(motCleFiltre)){
                            // Si mot clé trouvé alors incrémentation du nombre de correspondances
                            nombreDeCorrespondance++;
                            break;
                        }
                    }
                }
                // Si le nombre de correspondance est au moins égal au nombre de mots clés recherchés 
                // alors mapping du document
                if(nombreDeCorrespondance != 0 && nombreDeCorrespondance >= motsClesFiltres.length) 
                    foundDocuments.getRealMap().put(documentEntry.getKey(), document.getTitre());
            }
        }
        
        // Retour de la liste des documents trouvés
        return foundDocuments;
    }
    
    /**
     * Cette méthode permet, à partir d'un identifiant fourni, de retourner le document XML correspondant si celui-ci est stocké sur le serveur.
     * Méthode du web service.
     * 
     * @param index Identifiant du document à retourner
     * @return Contenu XML du document en binaire
     * @throws Exception 
     */
    @WebMethod(operationName = "retourneDocument")
    public byte[] retourneDocument(@WebParam(name = "index") int index) throws Exception {
        
        // Récupération de l'instance de Document correspondante
        Document document = this.documents.get(index);
        
        // Un document possède bien cet identifiant et il est sur le serveur
        if(document != null && document.isStocke()){
            // Sérialisation du document
            String XMLContent = StaxSerializer.serialize(document);
            // Retour du XML sous forme de byte[]
            return XMLContent.getBytes();
        }
        
        return null;
    }
    
    /**
     * Cette méthode permet, à partir d'un identifiant fourni, de retourner le PDF correspondant.
     * Méthode du web service.
     * 
     * @param index Identifiant du document à retourner
     * @return Instance de PDF
     * @throws Exception 
     */
    @WebMethod(operationName = "generePDF")
    public PDF generePDF(@WebParam(name = "index") int index) throws Exception {
        
        // Récupération de l'instance de Document correspondante
        Document document = this.documents.get(index);
        
        if(document != null && document.isStocke()){
            StaxSerializer.transformXSL(DocumentLibraryWS.FILES_REPOSITORY + "/" + document.getNom());
            // Création et retour du PDF
            return new PDF(
                document.getNom().replace(".xml",".pdf"),
                StaxSerializer.transformXSL(DocumentLibraryWS.FILES_REPOSITORY + "/" + document.getNom())
            );
        }
        
        return null;
    }
    
    /**
     * Cette méthode permet de sérialiser la collection de documents (stockage sous forme de fichiers XML).
     * Elle est appelée juste avant l'arrêt du service web.
     */
    @PreDestroy
    public void arret() {
        try{
            // Parcours des documents
            for(Entry<Integer, Document> documentEntry : this.documents.entrySet()){
                Document document = documentEntry.getValue();
                // Sérialisation et stockage de chaque document
                PrintWriter w = new PrintWriter(DocumentLibraryWS.FILES_REPOSITORY + "/" + document.getNom(), "UTF-8");
                w.println(StaxSerializer.serialize(document));
                w.close();
            }
        }catch(XMLStreamException | UnsupportedEncodingException | FileNotFoundException ex){}
    }
    
    /**
     * Cette méthode permet d'ajouter une instance de Document au dictionnaire.
     * 
     * @param document Objet de type Document à ajouter au dictionnaire de documents pour indexation
     * @return Index du document ajouté
     */
    private int addDocument(Document document){
        // L'index correspond à la taille du dictionnaire
        int index = this.documents.size();
        // Ajout du document dans le dictionnaire
        this.documents.put(index, document);
        // Retour de l'index
        return index;
    }

}
