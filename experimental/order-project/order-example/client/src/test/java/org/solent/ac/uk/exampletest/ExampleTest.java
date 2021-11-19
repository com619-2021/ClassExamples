/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.ac.uk.exampletest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import solent.ac.uk.devops.traffic.client.swagger.api.ServicetestApi;
import solent.ac.uk.devops.traffic.client.swagger.invoker.ApiException;

/**
 *
 * @author cgallen
 */
// @Ignore // do not run
public class ExampleTest {

    final static Logger LOG = LogManager.getLogger(ExampleTest.class);

    private final ServicetestApi api = new ServicetestApi();
    @Test
    public void hello1() {
        LOG.debug("******************replace me");
    }
    
    //@Test
    public void hello() {

        /// see configuration and ApiClient for details of protected String basePath = "
        try {
            String response = api.message();
            LOG.debug("api reponse "+response);
        } catch (ApiException ex) {
            LOG.error("error in test:", ex);
        }

    }
}
