/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.order.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.project.impl.dao.order.springdata.OrderChangeRequestRepository;
import org.solent.com504.project.impl.dao.order.springdata.OrderRepository;
import org.solent.com504.project.impl.dao.party.springdata.PartyRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceCatalogRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceRepository;
import org.solent.com504.project.impl.resource.service.ResourceCatalogServiceImpl;
import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.order.dto.ChangeStatus;
import org.solent.com504.project.model.order.dto.Order;
import org.solent.com504.project.model.order.dto.OrderChangeRequest;
import org.solent.com504.project.model.order.dto.OrderChangeRequestEntity;
import org.solent.com504.project.model.order.dto.OrderChangeRequestMapper;
import org.solent.com504.project.model.order.dto.OrderEntity;
import org.solent.com504.project.model.order.service.OrderChangeRequestService;
import org.solent.com504.project.model.party.dto.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cgallen
 */
@Service
public class OrderChangeRequestServiceImpl implements OrderChangeRequestService {

    final static Logger LOG = LogManager.getLogger(OrderChangeRequestServiceImpl.class);

    @Autowired
    private PartyRepository partyRepository = null;

    @Autowired
    private ResourceRepository resourceRepository = null;

    @Autowired
    private ResourceCatalogRepository resourceCatalogRepository = null;

    @Autowired
    private OrderRepository orderRepository = null;

    @Autowired
    private OrderChangeRequestRepository orderChangeRequestRepository = null;

    @Override
    public ReplyMessage getOrderChangeRequestByUuid(String uuid) {
        ReplyMessage replyMessage = new ReplyMessage();
        List<OrderChangeRequestEntity> orderChangeRequestEntityList = orderChangeRequestRepository.findByUuid(uuid);
        if (orderChangeRequestEntityList.isEmpty()) {
            throw new IllegalArgumentException("cannot find orderChangeRequest uuid not found=" + uuid);
        }
        List<OrderChangeRequest> orderChangeRequestList = OrderChangeRequestMapper.INSTANCE.orderChangeRequestEntityListToOrderChangeRequestList(orderChangeRequestEntityList);
        replyMessage.setOrderChangeRequestList(orderChangeRequestList);
        return replyMessage;
    }

    @Override
    @Transactional
    public ReplyMessage deleteOrderChangeRequestByUuid(String uuid) {
        List<OrderChangeRequestEntity> orderChangeRequestList = orderChangeRequestRepository.findByUuid(uuid);
        if (orderChangeRequestList.isEmpty()) {
            throw new IllegalArgumentException("cannot delete orderChangeRequest uuid not found=" + uuid);
        }
        orderChangeRequestRepository.delete(orderChangeRequestList.get(0));
        return new ReplyMessage();
    }

    @Override
    @Transactional
    public ReplyMessage postCreateOrderChangeRequest(OrderChangeRequest orderChangeRequest, String changeRequestorPartyUUID) {
        ReplyMessage replyMessage = new ReplyMessage();

        List<Party> partyList = partyRepository.findByUuid(changeRequestorPartyUUID);
        if (partyList.isEmpty()) {
            throw new IllegalArgumentException("cannot create orderChangeRequest party not found ownerPartyUUID=" + changeRequestorPartyUUID);
        }
        Party resourceOwner = partyList.get(0);

        OrderChangeRequestEntity orderChangeRequestEntity = OrderChangeRequestMapper.INSTANCE.orderToOrderChangeRequestEntity(orderChangeRequest);
        orderChangeRequestEntity.setId(null); // creating new entity)
        orderChangeRequestEntity.setUuid(UUID.randomUUID().toString());
        orderChangeRequestEntity.setHref("/rest/solent-api/order/v1/orderChangeRequest/" + orderChangeRequestEntity.getUuid());
        orderChangeRequestEntity.setChangeRequestor(resourceOwner);
        orderChangeRequestEntity.setRequestDate(new Date());
        orderChangeRequestEntity.setStatus(ChangeStatus.REQUESTED);

        orderChangeRequestEntity = orderChangeRequestRepository.save(orderChangeRequestEntity);

        // if external order uuid may not be found
        if (orderChangeRequestEntity.getOrderUuid() != null) { //uninitialisedd external order
            List<OrderEntity> orderEntityList = orderRepository.findByUuid(orderChangeRequestEntity.getOrderUuid());
            if (orderEntityList.isEmpty()) {
                throw new IllegalArgumentException("order not found with uuid" + orderChangeRequestEntity.getOrderUuid());
            }
            OrderEntity orderEntity = orderEntityList.get(0);
            orderEntity.addOrderChangeRequest(orderChangeRequestEntity);
            orderRepository.save(orderEntity);
        }

        OrderChangeRequest orderChangeRequestReply = OrderChangeRequestMapper.INSTANCE.orderChangeRequestEntityToOrderChangeRequest(orderChangeRequestEntity);
        List<OrderChangeRequest> orderChangeRequestList = Arrays.asList(orderChangeRequestReply);

        replyMessage.setOrderChangeRequestList(orderChangeRequestList);
        return replyMessage;
    }

    @Override
    @Transactional
    public ReplyMessage putUpdateOrderChangeRequest(OrderChangeRequest orderChangeRequest) {
        orderChangeRequest.setId(null); // in case set
        ReplyMessage replyMessage = new ReplyMessage();
        List<OrderChangeRequestEntity> requestList = orderChangeRequestRepository.findByUuid(orderChangeRequest.getUuid());
        if (requestList.isEmpty()) {
            throw new IllegalArgumentException("cannot find orderChangeRequest uuid=" + orderChangeRequest.getUuid());
        }
        OrderChangeRequestEntity orderChangeRequestEntity = requestList.get(0);

        orderChangeRequestEntity = OrderChangeRequestMapper.INSTANCE.updateOrderChangeRequestEntity(orderChangeRequest, orderChangeRequestEntity);
        orderChangeRequestEntity = orderChangeRequestRepository.save(orderChangeRequestEntity);

        OrderChangeRequest orderChangeRequestReply = OrderChangeRequestMapper.INSTANCE.orderChangeRequestEntityToOrderChangeRequest(orderChangeRequestEntity);
        replyMessage.setOrderChangeRequestList(Arrays.asList(orderChangeRequestReply));
        return replyMessage;
    }

    @Override
    public ReplyMessage getOrderChangeRequestByTemplate(OrderChangeRequest orderSearchTemplate, Integer offset, Integer limit) {
        //TODO only gets all now
        ReplyMessage replyMessage = new ReplyMessage();
        List<OrderChangeRequestEntity> requestList = orderChangeRequestRepository.findAll();
        List<OrderChangeRequest> orderChangeRequestList = OrderChangeRequestMapper.INSTANCE.orderChangeRequestEntityListToOrderChangeRequestList(requestList);
        replyMessage.setOrderChangeRequestList(orderChangeRequestList);
        return replyMessage;
    }

}
