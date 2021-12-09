/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.rest;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author cgallen
 */
// see https://stackoverflow.com/questions/41821586/change-in-json-response-after-upgrade-to-json-2-25
// USE_WRAPPER_NAME_AS_PROPERTY_NAME
@Provider
public class JerseyMapperProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public JerseyMapperProvider() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure( MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true );
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
