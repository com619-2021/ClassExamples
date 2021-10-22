package org.solent.com504.project.model.resource.service;

import java.util.List;
import org.solent.com504.project.model.resource.dto.Resource;

public interface ResourceCatalogueService {

    public String getResourceByuuid(String uuid);

    public String deleteResourceByUuid(String uuid);

    public String postCreateResource(Resource resource, String ownerPartyUUID);

    public Resource putUpdateResource(Resource resource);

    public List<Resource> getResourceByTemplate(Resource resourceSearchTemplate);
}
