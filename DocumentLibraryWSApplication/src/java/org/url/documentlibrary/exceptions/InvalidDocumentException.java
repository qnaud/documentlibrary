/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.url.documentlibrary.exceptions;

/**
 *
 * @author Quentin
 */
public class InvalidDocumentException extends Exception{
    
    public InvalidDocumentException() {
        super();
    }
    
    public InvalidDocumentException(String s) {
        super(s);
    }
}
