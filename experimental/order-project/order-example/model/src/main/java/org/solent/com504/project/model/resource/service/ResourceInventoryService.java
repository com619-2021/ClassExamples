package org.solent.com504.project.model.resource.service;

import java.util.List;
import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.resource.dto.Resource;

public interface ResourceInventoryService {

    public ReplyMessage getResourceByuuid(String uuid);

    public ReplyMessage deleteResourceByUuid(String uuid);

    public ReplyMessage postCreateResource(Resource resource, String ownerPartyUUID);

    public ReplyMessage putUpdateResource(Resource resource);

    public ReplyMessage getResourceByTemplate(Resource resourceSearchTemplate,Integer offset, Integer limit);
}
