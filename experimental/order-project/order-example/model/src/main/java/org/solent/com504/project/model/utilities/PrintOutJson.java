/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.model.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author cgallen
 */
public class PrintOutJson {

    public static String getJson(Object object)  {
        ObjectMapper mapper = new ObjectMapper();
        
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(mapper.getTypeFactory());
        mapper.setAnnotationIntrospector(introspector);
        // see https://github.com/FasterXML/jackson-modules-base/issues/98 
        // Jaxb module not serializing XmlElementWrapper correctly
        mapper.enable( MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME );

        String result=null;
        try {
            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("problem converting object to json ",ex);
        }
        return result;
    }
}
