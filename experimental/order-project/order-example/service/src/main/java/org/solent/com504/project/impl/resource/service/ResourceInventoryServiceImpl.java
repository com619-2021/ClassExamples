/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.resource.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.project.impl.dao.party.springdata.PartyRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceCatalogRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceRepository;
import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.party.dto.Party;
import org.solent.com504.project.model.resource.dto.AbstractResourceMapper;
import org.solent.com504.project.model.resource.dto.Characteristic;
import org.solent.com504.project.model.resource.dto.Resource;
import org.solent.com504.project.model.resource.dto.ResourceAccess;
import org.solent.com504.project.model.resource.dto.ResourceCatalog;
import org.solent.com504.project.model.resource.service.ResourceInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public ReplyMessage getResourceByuuid(String uuid) {
        ReplyMessage replyMessage = new ReplyMessage();

        List<Resource> resourceList = resourceRepository.findByUuid(uuid);
        if (resourceList.isEmpty()) {
            throw new IllegalArgumentException("cannot find resource uuid not found=" + uuid);
        }
        Resource resourceEntity = resourceList.get(0);

        //create a detached resource dto for reply message
        Resource detachedResource = AbstractResourceMapper.INSTANCE.abstractResourceToResource(resourceEntity);
        replyMessage.setResourceList(Arrays.asList(detachedResource));
        replyMessage.setOffset(0);
        replyMessage.setLimit(1);
        replyMessage.setTotalCount(1L);
        return replyMessage;
    }

    @Override
    @Transactional
    public ReplyMessage deleteResourceByUuid(String uuid) {
        List<Resource> resourceList = resourceRepository.findByUuid(uuid);
        if (resourceList.isEmpty()) {
            throw new IllegalArgumentException("cannot delete resource uuid not found=" + uuid);
        }
        resourceRepository.delete(resourceList.get(0));
        return new ReplyMessage();
    }

    @Override
    @Transactional
    public ReplyMessage postCreateResource(Resource resource, String ownerPartyUUID) {
        resource.setId(null); // may be differnt db id
        List<Party> partyList = partyRepository.findByUuid(ownerPartyUUID);
        if (partyList.isEmpty()) {
            throw new IllegalArgumentException("cannot create resource party not found ownerPartyUUID=" + ownerPartyUUID);
        } else {
            Party resourceOwner = partyList.get(0);
            resource.setResourceOwner(resourceOwner);
            // note - will take given uuid or create a new one
            if (resource.getUuid()==null || resource.getUuid().isEmpty()){
                resource.setUuid(UUID.randomUUID().toString());
                resource.setResourceController(ResourceAccess.INTERNAL);
            }
            resource = resourceRepository.saveAndFlush(resource);
        }

        ReplyMessage replyMessage = new ReplyMessage();
        replyMessage.setResourceList(Arrays.asList(resource));
        return replyMessage;
    }

    @Override
    @Transactional
    public ReplyMessage putUpdateResource(Resource resource) {
        resource.setId(null); // may be differnt db id
        if (resource.getUuid() == null) {
            throw new IllegalArgumentException("cannot update resource uuid=null");
        }
        List<Resource> resourceList = resourceRepository.findByUuid(resource.getUuid());
        if (resourceList.isEmpty()) {
            throw new IllegalArgumentException("cannot update resource not found uuid" + resource.getUuid());
        }
        Resource resourceEntity = resourceList.get(0);

        // update found resource with new values
        resourceEntity = AbstractResourceMapper.INSTANCE.updateResource(resource, resourceEntity);
        resourceEntity = resourceRepository.saveAndFlush(resourceEntity);

        //create a detached resource dto for reply message
        Resource detachedResource = AbstractResourceMapper.INSTANCE.abstractResourceToResource(resourceEntity);
        ReplyMessage replyMessage = new ReplyMessage();
        replyMessage.setOffset(1);
        replyMessage.setLimit(1);
        replyMessage.setTotalCount(1L);
        replyMessage.setResourceList(Arrays.asList(detachedResource));
        return replyMessage;
    }

    @Override
    @Transactional
    public ReplyMessage getResourceByTemplate(Resource resourceSearchTemplate, Integer offset, Integer limit) {

        // TODO criteria search
        if (resourceSearchTemplate != null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        List<Resource> resourceList = resourceRepository.findAll();
        Long totalCount = resourceRepository.count();

        List<Resource> detachedResourceList = new ArrayList();

        for (Resource resourceEntity : resourceList) {
            Resource detachedResource = AbstractResourceMapper.INSTANCE.abstractResourceToResource(resourceEntity);
            detachedResourceList.add(detachedResource);
        }

        ReplyMessage replyMessage = new ReplyMessage();
        replyMessage.setOffset(offset);
        replyMessage.setLimit(limit);

        replyMessage.setResourceList(detachedResourceList);
        replyMessage.setTotalCount(totalCount);
        return replyMessage;

    }



}
