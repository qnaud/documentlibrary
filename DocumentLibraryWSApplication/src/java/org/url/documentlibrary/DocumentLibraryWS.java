/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.url.documentlibrary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author Quentin
 */
@WebService(serviceName = "DocumentLibraryWS")
@Stateless()
public class DocumentLibraryWS {
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "depotDocument")
    public int depotDocument(@WebParam(name = "nom") String nom, @WebParam(name = "contenu") byte[] contenu) {
    
        String filePath = "files/" + nom;
         
        try {
            File folder = new File(System.getProperty( "user.home" ) + File.separator + "files");
            folder.mkdir();
            File file = new File(folder, nom);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream outputStream = new BufferedOutputStream(fos);
            outputStream.write(contenu);
            outputStream.close();
             
            System.out.println("Received file: " + filePath);
             
        } catch (IOException ex) {
            System.err.println(ex);
        }
        
        return 0;
    }
}
