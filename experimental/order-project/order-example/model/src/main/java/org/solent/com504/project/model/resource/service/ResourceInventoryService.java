package org.solent.com504.project.model.resource.service;

import org.solent.com504.project.model.resource.dto.Resource;

public interface ResourceInventoryService {

    public String getResourceByuuid(String uuid);

    public String deleteResourceByUuid(String uuid);

    public String postCreateResource(Resource resource, String ownerPartyUUID);

    public Resource putUpdateResource(Resource resource);

    public Resource getResourceByTemplate(Resource resourceSearchTemplate);
}
