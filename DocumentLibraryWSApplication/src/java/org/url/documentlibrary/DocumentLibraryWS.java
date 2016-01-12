
package org.url.documentlibrary;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import org.url.documentlibrary.model.Document;
import org.url.documentlibrary.util.StaxSerializer;

/**
 * DocumentLibraryWS est la classe qui définie le service web homonyme.
 * 
 * @author Quentin NAUD, Benjamin NEILZ
 * @version 1.0
 */
@WebService(serviceName = "DocumentLibraryWS")
@Stateless()
public class DocumentLibraryWS {
    
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
            this.folder = new File(System.getProperty("catalina.base") + File.separator + "files");
            this.folder.mkdir();

            // Récupération des fichiers du répertoire
            File[] files = this.folder.listFiles();

            if (files != null) {
                // Parcours des fichiers
                for (int i = 0; i < files.length; i++) {
                    // Focus sur les fichiers uniquement (pas les répertoires)
                    if (!files[i].isDirectory()) {
                        String fichier = files[i].getName();
                        // Focus sur les fichiers de type XML uniquement
                        if(fichier.substring(fichier.length() - 3).equals("xml")){
                            // Contenu du fichier courant
                            String fileStr = new Scanner(files[i]).useDelimiter("\\Z").next();
                            // Désérialisation
                            Document document = StaxSerializer.unserialize(fichier, fileStr.getBytes());
                            // Ajout du document à la liste
                            this.addDocument(document);
                        }
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Cette méthode permet d'uploader un fichier XML sur le serveur et de l'indexer
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
     * Cette méthode permet d'ajouter une instance de Document au dictionnaire
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
