
package org.url.documentlibrary.model;

import java.util.ArrayList;

/**
 * Document est la classe qui représente les documents stockés dans la librairie
 * 
 * @author Quentin NAUD, Benjamin NEILZ
 * @version 1.0
 */
public class Document {
    
    /**
     * Nom du document (nom du fichier correspondant)
     */
    private String nom;
    
    /**
     * Titre du document
     */
    private String titre;
    
    /**
     * Résumé du document
     */
    private String resume;
    
    /**
     * Liste des sections contenues dans le document
     */
    private ArrayList<Section> sections;
    
    /**
     * Liste des mots clés représentatifs du contenu du document
     */
    private ArrayList<String> motCles;

    /**
     * Constructeur de Document
     */
    public Document() {
        this.sections = new ArrayList<Section>();
        this.motCles = new ArrayList<String>();
    }
    
    /**
     * Constructeur de Document
     * 
     * @param nom 
     */
    public Document(String nom) {
        this.nom = nom;
        this.sections = new ArrayList<Section>();
        this.motCles = new ArrayList<String>();
    }

    /**
     * Constructeur de Document
     * 
     * @param titre
     * @param resume 
     */
    public Document(String titre, String resume) {
        this.titre = titre;
        this.resume = resume;
        this.sections = new ArrayList<Section>();
        this.motCles = new ArrayList<String>();
    }
    
    /**
     * Affiche les informations du document sur la sortie standard
     */
    public void afficher(){
        System.out.println("Titre du document : " + this.titre);
        System.out.println("Résumé du document : " + this.resume);
        System.out.print("Mots clés du document : ");
        for(String mc : this.motCles){
            System.out.print(mc+" ");
        }
        System.out.println();
        System.out.println("Sections du document : ");
        for(Section s : this.sections){
            System.out.print("Section \"");
            System.out.print(s.getTitre());
            System.out.print("\" : " + s.getContenu());
            System.out.println();
        }
    }

    /**
     * Get the value of resume
     *
     * @return the value of resume
     */
    public String getResume() {
        return resume;
    }

    /**
     * Set the value of resume
     *
     * @param resume new value of resume
     */
    public void setResume(String resume) {
        this.resume = resume;
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
    
    /**
     * Get the value of motCles
     *
     * @return the value of motCles
     */
    public ArrayList<String> getMotCles() {
        return motCles;
    }

    /**
     * Set the value of motCles
     *
     * @param motCles new value of motCles
     */
    public void setMotCles(ArrayList<String> motCles) {
        this.motCles = motCles;
    }
    
    /**
     * Add a keyword to the list
     * 
     * @param motCle new keyword
     */    
    public void addMotCles(String motCle) {
        this.motCles.add(motCle);
    }
    
    /**
     * Get the value of sections
     *
     * @return the value of sections
     */
    public ArrayList<Section> getSections() {
        return sections;
    }

    /**
     * Set the value of sections
     *
     * @param sections new value of sections
     */
    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }
    
    /**
     * Add a section to the list
     * 
     * @param section new section
     */
    public void addSection(Section section) {
        this.sections.add(section);
    }
    
    /**
     * Get the value of nom
     *
     * @return the value of nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Set the value of nom
     *
     * @param nom new value of nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
}
