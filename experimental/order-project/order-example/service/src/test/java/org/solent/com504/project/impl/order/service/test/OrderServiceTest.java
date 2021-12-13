/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.order.service.test;

import org.solent.com504.project.impl.resource.service.test.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import org.solent.com504.project.impl.service.test.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.order.dto.Order;
import org.solent.com504.project.model.order.dto.OrderStatus;
import org.solent.com504.project.model.order.service.OrderService;
import org.solent.com504.project.model.party.dto.Party;
import org.solent.com504.project.model.party.dto.PartyRole;
import org.solent.com504.project.model.party.service.PartyService;
import org.solent.com504.project.model.resource.dto.AbstractResourceMapper;
import org.solent.com504.project.model.resource.dto.Characteristic;
import org.solent.com504.project.model.resource.dto.Resource;
import org.solent.com504.project.model.resource.dto.ResourceAccess;
import org.solent.com504.project.model.resource.dto.ResourceHref;
import org.solent.com504.project.model.resource.service.ResourceCatalogService;
import org.solent.com504.project.model.resource.service.ResourceInventoryService;
import org.solent.com504.project.model.service.ServiceFacade;
import org.solent.com504.project.model.utilities.PrintOutJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author gallenc
 */
@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the OrderServiceConfig class
@ContextConfiguration(classes = ServiceTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class OrderServiceTest {

    final static Logger LOG = LogManager.getLogger(OrderServiceTest.class);

    @Autowired
    ServiceFacade serviceFacade = null;

    @Autowired
    ResourceCatalogService resourceCatalogService = null;

    @Autowired
    ResourceInventoryService resourceInventoryService = null;

    @Autowired
    PartyService partyService = null;

    @Autowired
    OrderService orderService = null;

    public void testFactory() {
        LOG.debug("start OrderServiceTest testFactory");
        assertNotNull(serviceFacade);
        assertNotNull(resourceInventoryService);
        assertNotNull(partyService);
        assertNotNull(orderService);
        LOG.debug("end OrderServiceTest testFactory");
    }

    @Test
    public void internalOrderTest() {
        // create a resource with a party
        Party party1 = new Party();
        party1.setFirstName("party1");
        party1.setPartyRole(PartyRole.SELLER);
        party1 = partyService.save(party1);
        LOG.debug("party1=" + party1);

        // create a mock resource
        Resource resourceIn = mockResource();
        String ownerPartyUUID = party1.getUuid();

        ReplyMessage replyMessage = resourceInventoryService.postCreateResource(resourceIn, ownerPartyUUID);
        List<Resource> resourceList = replyMessage.getResourceList();
        assertEquals(1, resourceList.size());

        Resource createdResource = resourceList.get(0);
        LOG.debug("created Resource:" + createdResource);

        // create an order
        Order order = new Order();
        order.setResourceAccess(ResourceAccess.INTERNAL);
        order.setDescription("order description");
        order.setName("PO1234");
        order.setEndDate(new Date());
        order.setOrderDate(new Date());
        order.setStartDate(new Date());
        order.setStatus(OrderStatus.ACKNOWLEGED);

        // add resource href
        ResourceHref resourceHref = AbstractResourceMapper.INSTANCE.abstractResourceToResourceHref(createdResource);
        List<ResourceHref> resourceOrService = new ArrayList();
        resourceOrService.add(resourceHref);

        order.setResourceOrService(new LinkedHashSet(resourceOrService));
        replyMessage = orderService.postCreateOrder(order, party1.getUuid());

        String jsonOut = PrintOutJson.getJson(replyMessage);

        LOG.debug("create order reply message " + jsonOut);

        // create another mock resource to add to order
        Resource resourceIn2 = mockResource();
        ReplyMessage replyMessage2 = resourceInventoryService.postCreateResource(resourceIn2, ownerPartyUUID);
        List<Resource> resourceList2 = replyMessage2.getResourceList();

        assertEquals(1, resourceList2.size());

        Resource createdResource2 = resourceList2.get(0);
        LOG.debug("created Resource2:" + createdResource2);
        ResourceHref resourceHref2 = AbstractResourceMapper.INSTANCE.abstractResourceToResourceHref(createdResource2);

        // update an order with put
        Order updateOrder = replyMessage.getOrderList().get(0);
        updateOrder.setDescription("updated order description");
        updateOrder.setEndDate(new Date(new Date().getTime() + 1000 * 60 * 24));
        updateOrder.setStatus(OrderStatus.IN_PROGRESS);
        Set resourceOrService2 = updateOrder.getResourceOrService();
        resourceOrService2.add(resourceHref2);
        updateOrder.setResourceOrService(resourceOrService2);

        jsonOut = PrintOutJson.getJson(updateOrder);
        LOG.debug("updated order  " + jsonOut);

        replyMessage = orderService.putUpdateOrder(updateOrder);
        jsonOut = PrintOutJson.getJson(replyMessage);
        LOG.debug("updated order reply message with 2 resources" + jsonOut);

        // get all orders
        replyMessage = orderService.getOrderByTemplate(null, null, null);
        jsonOut = PrintOutJson.getJson(replyMessage);
        LOG.debug("updated order reply message all orders" + jsonOut);
    }

    private Resource mockResource() {
        Resource resource = new Resource();
        String uuid = UUID.randomUUID().toString();
        List<Characteristic> characteristics = new ArrayList();
        characteristics.add(new Characteristic("characteristicName1", "characteristic value1", "characteristic description1"));
        characteristics.add(new Characteristic("characteristicName2", "characteristic value2", "characteristic description2"));
        resource.setCharacteristics(characteristics);
        String description = "temp description" + uuid;
        resource.setDescription(description);
        String href = "http://temp" + uuid;
        resource.setHref(href);
        //  resource.setId(1L);
        String name = "resource name";
        resource.setName(name);
        resource.setResourceController(ResourceAccess.INTERNAL);
        String resourceTypeName = "recource type name " + uuid;
        resource.setResourceTypeName(resourceTypeName);
        resource.setUuid(uuid);
        return resource;
    }
}
