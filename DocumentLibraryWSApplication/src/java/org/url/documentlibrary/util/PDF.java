
package org.url.documentlibrary.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * PDF est la classe qui définie une structure pour créer des PDF.
 * Cette collection est utilisée en guise de valeur de retour pour certaines opérations du WS.
 * Un PDF possède un nom de fichier et un contenu binaire.
 * 
 * @author Quentin NAUD, Benjamin NEILZ
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD) 
public class PDF {
    
    /**
     * Nom du PDF
     */
    protected String nom;
    
    /**
     * Contenu binaire formaté
     */
    protected byte[] contenu;

    /**
     * Constructeur de PDF
     * 
     * @param nom
     * @param contenu 
     */
    public PDF(String nom, byte[] contenu) {
        this.nom = nom;
        this.contenu = contenu;
    }

    /**
     * Get nom
     * @return nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Set nom
     * @param nom Nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Get contenu
     * @return contenu
     */
    public byte[] getContenu() {
        return contenu;
    }

    /**
     * Set contenu
     * @param contenu Nouveau contenu
     */
    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }
}
