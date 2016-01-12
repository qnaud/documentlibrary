
package org.url.documentlibrary.model;

/**
 * Section est la classe qui représente les sections d'un document
 * 
 * @author Quentin NAUD, Benjamin NEILZ
 * @version 1.0
 */
public class Section {
    
    /**
     * Intitulé d'une section
     */
    private String titre;
    
    /**
     * Contenu d'une section
     */
    private String contenu;

    /**
     * Construction de Section
     * 
     * @param titre
     * @param contenu 
     */
    public Section(String titre, String contenu) {
        this.titre = titre;
        this.contenu = contenu;
    }

    /**
     * Get the value of contenu
     *
     * @return the value of contenu
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Set the value of contenu
     *
     * @param contenu new value of contenu
     */
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    /**
     * Get the value of titre
     *
     * @return the value of titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Set the value of titre
     *
     * @param titre new value of titre
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

}
