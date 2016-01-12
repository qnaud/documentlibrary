/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.url.documentlibrary.model;

import java.util.ArrayList;

/**
 *
 * @author Quentin
 */
public class Document {
    
    private String titre;
    private String resume;
    private ArrayList<Section> sections;
    private ArrayList<String> motCles;

    public Document() {
    }

    public Document(String titre, String resume) {
        this.titre = titre;
        this.resume = resume;
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
    
    public void addSection(Section section) {
        this.sections.add(section);
    }
}
