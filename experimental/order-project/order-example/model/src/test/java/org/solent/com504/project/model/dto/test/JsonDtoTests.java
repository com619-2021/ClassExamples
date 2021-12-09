/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.model.dto.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.solent.com504.project.model.order.dto.Order;
import org.solent.com504.project.model.order.dto.OrderHref;


/**
 *
 * @author cgallen
 */
public class JsonDtoTests {

    private static final Logger LOG = LogManager.getLogger(JsonDtoTests.class);

    ObjectMapper objectMapper = null;

    @Before
    public void before() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.findAndRegisterModules()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        // this uses xmlelement wrapper as name
       objectMapper.configure( MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true );
    }

    @Test
    public void testJsonMessages() throws JsonProcessingException {
        Order order = new Order();
        OrderHref orderHref = new OrderHref();
        List<OrderHref> subOrders = Arrays.asList(orderHref);
        order.setSubOrders(subOrders);
        
        order.setParentOrder(orderHref);

        String tRequestString = objectMapper.writeValueAsString(order);

        LOG.debug("Json transactionRequest output:\n" + tRequestString);
        Order order2  = objectMapper.readValue(tRequestString, Order.class);
        
        assertTrue(order2.toString().equals(order.toString()));
    }
}
