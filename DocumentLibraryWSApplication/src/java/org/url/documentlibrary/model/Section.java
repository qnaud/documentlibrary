/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.url.documentlibrary.model;

/**
 *
 * @author Quentin
 */
public class Section {
    
    private String titre;
    private String contenu;

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
