/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.resource.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.project.impl.dao.party.springdata.PartyRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceCatalogRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceRepository;
import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.resource.dto.Resource;
import org.solent.com504.project.model.resource.service.ResourceInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author cgallen
 */
@Service
public class ResourceInventoryServiceImpl implements ResourceInventoryService {

    final static Logger LOG = LogManager.getLogger(ResourceInventoryServiceImpl.class);

    @Autowired
    private PartyRepository partyRepository = null;

    @Autowired
    private ResourceRepository resourceRepository = null;

    @Autowired
    private ResourceCatalogRepository resourceCatalogRepository = null;

    @Override
    public ReplyMessage getResourceByuuid(String uuid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReplyMessage deleteResourceByUuid(String uuid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReplyMessage postCreateResource(Resource resource, String ownerPartyUUID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReplyMessage putUpdateResource(Resource resource) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReplyMessage getResourceByTemplate(Resource resourceSearchTemplate, Integer offset, Integer Limit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
