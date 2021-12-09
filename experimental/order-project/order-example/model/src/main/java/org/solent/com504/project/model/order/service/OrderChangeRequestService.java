package org.solent.com504.project.model.order.service;

import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.order.dto.OrderChangeRequest;

public interface OrderChangeRequestService {

    public ReplyMessage getOrderChangeRequestByUuid(String uuid);

    public ReplyMessage deleteOrderChangeRequestByUuid(String uuid);

    public ReplyMessage postCreateOrderChangeRequest(OrderChangeRequest orderChangeRequest, String changeRequestorPartyUUID);

    public ReplyMessage putUpdateOrderChangeRequest(OrderChangeRequest orderChangeRequest);

    public ReplyMessage getOrderChangeRequestByTemplate(OrderChangeRequest orderChangeRequestSearchTemplate, Integer offset, Integer limit);
    
    
}
