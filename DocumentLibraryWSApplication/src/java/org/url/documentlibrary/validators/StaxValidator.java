/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.url.documentlibrary.validators;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 *
 * @author Quentin
 */
public class StaxValidator {
    
    public static int validate(File XMLFile) throws SAXException, IOException, XMLStreamException{
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(XMLFile));

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File("schema.xsd"));

        Validator validator = schema.newValidator();
        validator.validate(new StAXSource(reader));
        
        return 1;
    }
}
