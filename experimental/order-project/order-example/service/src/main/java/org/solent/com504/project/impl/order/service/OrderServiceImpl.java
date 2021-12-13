/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.project.impl.dao.order.springdata.OrderChangeRequestRepository;
import org.solent.com504.project.impl.dao.order.springdata.OrderRepository;
import org.solent.com504.project.impl.dao.party.springdata.PartyRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceCatalogRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceRepository;
import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.order.dto.ChangeStatus;
import org.solent.com504.project.model.order.dto.Order;
import org.solent.com504.project.model.order.dto.OrderChangeRequestEntity;
import org.solent.com504.project.model.order.dto.OrderEntity;
import org.solent.com504.project.model.order.dto.OrderMapper;
import org.solent.com504.project.model.order.dto.OrderStatus;
import org.solent.com504.project.model.order.service.OrderService;
import org.solent.com504.project.model.party.dto.Party;
import org.solent.com504.project.model.resource.dto.AbstractResourceMapper;
import org.solent.com504.project.model.resource.dto.Resource;
import org.solent.com504.project.model.resource.dto.ResourceAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cgallen
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    final static Logger LOG = LogManager.getLogger(OrderServiceImpl.class);
    
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
    public ReplyMessage getOrderByUuid(String uuid) {
        ReplyMessage replyMessage = new ReplyMessage();
        
        List<OrderEntity> orderList = orderRepository.findByUuid(uuid);
        if (orderList.isEmpty()) {
            throw new IllegalArgumentException("cannot find order uuid not found=" + uuid);
        }
        
        OrderEntity orderEntity = orderList.get(0);

        //create a detached order dto for reply message
        Order detachedOrder = orderEntityToOrder(orderEntity);
        
        replyMessage.setOrderList(Arrays.asList(detachedOrder));
        replyMessage.setOffset(0);
        replyMessage.setLimit(1);
        replyMessage.setTotalCount(1L);
        return replyMessage;
    }
    
    @Override
    @Transactional
    public ReplyMessage deleteOrderByUuid(String uuid) {
        List<OrderEntity> orderList = orderRepository.findByUuid(uuid);
        if (orderList.isEmpty()) {
            throw new IllegalArgumentException("cannot delete order uuid not found=" + uuid);
        }
        orderRepository.delete(orderList.get(0));
        return new ReplyMessage();
    }
    
    @Override
    @Transactional
    public ReplyMessage postCreateOrder(Order order, String ownerPartyUUID) {
        order.setId(null); // may be different db id

        List<Party> partyList = partyRepository.findByUuid(ownerPartyUUID);
        if (partyList.isEmpty()) {
            throw new IllegalArgumentException("cannot create order party not found ownerPartyUUID=" + ownerPartyUUID);
        }
        Party resourceOwner = partyList.get(0);
        
        OrderEntity orderEntity;

        if (ResourceAccess.EXTERNAL.equals(order.getResourceAccess())) {
            // external order creation only done AFTER order placed in foreign machine
            orderEntity = new OrderEntity();
            orderEntity.setResourceAccess(ResourceAccess.EXTERNAL);
            orderEntity.setExternalOrder(order);
            if (order.getUuid() == null || "".equals(order.getUuid().trim())) {
                throw new IllegalArgumentException("cannot create external order reference with null uuid external order=" + order);
            }
            // use external order uuid as uid of this order
            orderEntity.setUuid(order.getUuid());
        } else {
            // internal order
            order.setStatus(OrderStatus.ACKNOWLEGED); // first creation
            orderEntity = OrderMapper.INSTANCE.orderToOrderEntity(order);
            // create new uuid if this is an order on our machine
            orderEntity.setUuid(UUID.randomUUID().toString());
            orderEntity.setId(null); // may be differnt db id
            orderEntity.setResourceAccess(ResourceAccess.INTERNAL);
            orderEntity.setHref("/rest/solent-api/order/v1/order/"+orderEntity.getUuid());

            // create resource references
            // check if resources exists and inject into order if do
            Set<Resource> newResources = new LinkedHashSet();
            for (Resource interimResource : orderEntity.getResourceOrService()) {
                List<Resource> resourceList = resourceRepository.findByUuid(interimResource.getUuid());
                if (resourceList.isEmpty()) {
                    throw new IllegalArgumentException("you can only update internal order with internal resources. Trying to update internal order with non existant resource: " + interimResource);
                }
                newResources.add(resourceList.get(0));
            }
            orderEntity.setResourceOrService(newResources);
        }
        
        orderEntity.setOrderOwner(resourceOwner);
        orderEntity = orderRepository.saveAndFlush(orderEntity);

        //create a detached order dto for reply message
        Order detachedOrder = orderEntityToOrder(orderEntity);
        
        // now update order change requests
        OrderChangeRequestEntity orderChangeRequestEntity = new OrderChangeRequestEntity();
        orderChangeRequestEntity.setUuid(UUID.randomUUID().toString());
        orderChangeRequestEntity.setHref("/rest/solent-api/order/v1/orderChangeRequest/"+orderChangeRequestEntity.getUuid());
        orderChangeRequestEntity.setChangeRequest(detachedOrder);
        orderChangeRequestEntity.setStatus(ChangeStatus.APPROVED);
        orderChangeRequestEntity.setChangeReason("first order creation from api");
        orderChangeRequestEntity.setRequestDate(new Date());
        orderChangeRequestEntity.setApprovedDate(new Date());
        orderChangeRequestEntity.setOrderUuid(detachedOrder.getUuid());
        orderChangeRequestRepository.save(orderChangeRequestEntity);
        orderEntity.addOrderChangeRequest(orderChangeRequestEntity);
        orderEntity = orderRepository.saveAndFlush(orderEntity);
        
        detachedOrder = orderEntityToOrder(orderEntity);
        
        ReplyMessage replyMessage = new ReplyMessage();
        replyMessage.setOrderList(Arrays.asList(detachedOrder));
        replyMessage.setOffset(0);
        replyMessage.setLimit(1);
        replyMessage.setTotalCount(1L);
        return replyMessage;
    }
    
    @Override
    @Transactional
    public ReplyMessage putUpdateOrder(Order order) {
        if (order.getUuid() == null) {
            throw new IllegalArgumentException("order should not have null uuid order=" + order);
        }
        List<OrderEntity> orderEntityList = orderRepository.findByUuid(order.getUuid());
        if (orderEntityList.isEmpty()) {
            throw new IllegalArgumentException("order not found with uuid" + order.getUuid());
        }
        OrderEntity orderEntity = orderEntityList.get(0);

        // check matching resource access
        if (ResourceAccess.EXTERNAL.equals(order.getResourceAccess())) {
            if (!ResourceAccess.EXTERNAL.equals(orderEntity.getResourceAccess())) {
                throw new IllegalArgumentException("cannot update internal order " + orderEntity.getUuid()
                        + " with an external order" + order);
            }
            orderEntity.setExternalOrder(order);
        } else {
            if (!ResourceAccess.INTERNAL.equals(orderEntity.getResourceAccess())) {
                throw new IllegalArgumentException("cannot update external order " + orderEntity.getUuid()
                        + " with an internal order update" + order);
            }
            //try and create resource reference for detached order entity
            OrderEntity newOrderEntity = new OrderEntity();
            newOrderEntity = OrderMapper.INSTANCE.updateOrderEntity(order, newOrderEntity);

            // create resource references
            // check if resources exists and inject if do
            Set<Resource> newResources = new LinkedHashSet();
            for (Resource interimResource : newOrderEntity.getResourceOrService()) {
                List<Resource> resourceList = resourceRepository.findByUuid(interimResource.getUuid());
                if (resourceList.isEmpty()) {
                    throw new IllegalArgumentException("you can only update internal order with internal resources. Trying to update internal order with non existant resource: " + interimResource);
                }
                newResources.add(resourceList.get(0));
            }
            // replacing old resources list
            newOrderEntity.setResourceOrService(newResources);

            // check if sub orders exist and inject if do
            Set<OrderEntity> suborders = new HashSet();
            for (OrderEntity suborder : newOrderEntity.getSubOrders()) {
                List<OrderEntity> subOrderList = orderRepository.findByUuid(suborder.getUuid());
                if (subOrderList.isEmpty()) {
                    throw new IllegalArgumentException("trying to attach an unknown internal suborder to parent order : " + suborder);
                }
                suborders.add(subOrderList.get(0));
            }
            // remove old suborders
            Iterator<OrderEntity> suborderiterator = orderEntity.getSubOrders().iterator();
            while (suborderiterator.hasNext()) {
                orderEntity.removeSuborder(suborderiterator.next());
            }
            // add back new suborders
            for (OrderEntity suborder : suborders) {
                orderEntity.addSuborder(suborder);
            }
            
            orderEntity = OrderMapper.INSTANCE.updateOrderEntityFromOrderEntity(newOrderEntity, orderEntity);
        }
        orderEntity = orderRepository.saveAndFlush(orderEntity);

        //create a detached order dto for reply message
        Order detachedOrder = orderEntityToOrder(orderEntity);

        // now update order change requests
        OrderChangeRequestEntity orderChangeRequestEntity = new OrderChangeRequestEntity();
        orderChangeRequestEntity.setUuid(UUID.randomUUID().toString());
        orderChangeRequestEntity.setChangeRequest(detachedOrder);
        orderChangeRequestEntity.setStatus(ChangeStatus.APPROVED);
        orderChangeRequestEntity.setChangeReason("order updated directly from api");
        orderChangeRequestEntity.setRequestDate(new Date());
        orderChangeRequestEntity.setApprovedDate(new Date());
        orderChangeRequestEntity.setOrderUuid(detachedOrder.getUuid());
        orderChangeRequestRepository.save(orderChangeRequestEntity);

        orderEntity.addOrderChangeRequest(orderChangeRequestEntity);
        orderEntity = orderRepository.saveAndFlush(orderEntity);
        
        detachedOrder = orderEntityToOrder(orderEntity);
        
        ReplyMessage replyMessage = new ReplyMessage();
        replyMessage.setOrderList(Arrays.asList(detachedOrder));
        replyMessage.setOffset(0);
        replyMessage.setLimit(1);
        replyMessage.setTotalCount(1L);
        return replyMessage;
    }
    
    @Override
    public ReplyMessage getOrderByTemplate(Order orderSearchTemplate, Integer offset, Integer limit) {
        // currently just gets all orders with no template
        ReplyMessage replyMessage = new ReplyMessage();
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        
        List<Order> orderList = new ArrayList();
        for (OrderEntity orderEntity : orderEntityList) {
            orderList.add(orderEntityToOrder(orderEntity));
        }
        replyMessage.setOrderList(orderList);
        return replyMessage;
    }
    
    private Order orderEntityToOrder(OrderEntity orderEntity) {
        //create a detached order dto for reply message
        Order order;
        if (ResourceAccess.INTERNAL == orderEntity.getResourceAccess()) {
            order = OrderMapper.INSTANCE.orderEntityToOrder(orderEntity);
        } else { // external
            order = orderEntity.getExternalOrder();
        }
        return order;
    }
    
}
