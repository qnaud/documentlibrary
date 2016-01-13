
package org.url.documentlibrary.util;

import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * DocumentMap est la classe qui définie une collection de type HashMap de documents.
 * Cette collection est utilisée en guise de valeur de retour pour certaines opérations du WS.
 * La clé correspond à l'identifiant du document, et la valeur à son titre
 * 
 * @author Quentin NAUD, Benjamin NEILZ
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD) 
public class DocumentMap {
    
    /**
     * La collection cachée derrière cet objet
     */
    protected HashMap<Integer, String> realMap;  
    
    /**
     * Constructeur de DocumentMap
     */
    public DocumentMap(){
        realMap = new HashMap<>();
    }
  
    /**
     * Get the real collection
     * @return The real collection
     */
    public HashMap<Integer, String> getRealMap() {  
        return realMap;  
    }  
}
