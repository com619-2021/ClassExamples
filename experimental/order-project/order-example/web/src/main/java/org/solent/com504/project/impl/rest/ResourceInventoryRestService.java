/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.rest;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.party.service.PartyService;
import org.solent.com504.project.model.resource.dto.Resource;
import org.solent.com504.project.model.resource.service.ResourceCatalogService;
import org.solent.com504.project.model.resource.service.ResourceInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author cgallen
 */
@Component // component allows resource to be picked up
@Path("/solent-api/inventory/v1/")
public class ResourceInventoryRestService {

    final static Logger LOG = LogManager.getLogger(ResourceInventoryRestService.class);

    @Autowired
    ResourceCatalogService resourceCatalogService = null;

    @Autowired
    ResourceInventoryService resourceService = null;

    @Autowired
    private PartyService partyService;

    public Response getResourceByuuid(String uuid, @Context UriInfo uriInfo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Response deleteResourceByUuid(String uuid, @Context UriInfo uriInfo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Response postCreateResource(Resource resource, String ownerPartyUUID, @Context UriInfo uriInfo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Response putUpdateResource(Resource resource, @Context UriInfo uriInfo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Response getResourceByTemplate(Resource resourceSearchTemplate, Integer offset, Integer Limit, @Context UriInfo uriInfo){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
