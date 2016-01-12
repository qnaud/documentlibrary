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
public class MissingParameterException extends Exception{
    
    public MissingParameterException() {
        super();
    }
    
    public MissingParameterException(String s) {
        super(s);
    }
}
