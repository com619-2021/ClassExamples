/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.user.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.project.impl.dao.order.springdata.OrderChangeRequestRepository;
import org.solent.com504.project.impl.dao.order.springdata.OrderRepository;
import org.solent.com504.project.impl.dao.party.springdata.PartyRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceCatalogRepository;
import org.solent.com504.project.impl.dao.resource.springdata.ResourceRepository;
import org.solent.com504.project.impl.dao.user.springdata.RoleRepository;
import org.solent.com504.project.impl.dao.user.springdata.UserRepository;
import org.solent.com504.project.model.order.dto.Order;
import org.solent.com504.project.model.order.dto.OrderChangeRequestHref;
import org.solent.com504.project.model.order.dto.OrderEntity;
import org.solent.com504.project.model.order.dto.OrderHref;
import org.solent.com504.project.model.order.dto.OrderMapper;
import org.solent.com504.project.model.order.dto.OrderStatus;

import org.solent.com504.project.model.party.dto.Party;
import org.solent.com504.project.model.resource.dto.ResourceAccess;
import org.solent.com504.project.model.user.dto.Role;
import org.solent.com504.project.model.user.dto.User;
import org.solent.com504.project.model.user.dto.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author cgallen
 */
@Component
public class DBInitialise {

    final static Logger LOG = LogManager.getLogger(DBInitialise.class);

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final long HOUR_IN_MS = 1000 * 60 * 60;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ResourceRepository resourceRepository = null;

    @Autowired
    ResourceCatalogRepository resourceCatalogRepository = null;

    @Autowired
    OrderChangeRequestRepository orderChangeRequestRepository = null;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void resetDatabase() {

        orderChangeRequestRepository.deleteAll();
        orderRepository.deleteAll();
        resourceRepository.deleteAll();
        resourceCatalogRepository.deleteAll();
        partyRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        init();

    }

    @PostConstruct
    public void init() {

        // add all roles in model to database
        if (roleRepository.findAll().isEmpty()) {
            UserRoles[] allRoles = UserRoles.values();
            for (int i = 0; i < allRoles.length; i++) {
                String roleName = allRoles[i].name();
                LOG.debug("initialising user role " + roleName + " to database");
                Role role = new Role();
                role.setName(roleName);
                roleRepository.saveAndFlush(role);
            }
        }

        // add admin and simple user to database by default
        if (userRepository.findAll().isEmpty()) {
            LOG.debug("new database initialising default globaladmin and basicuser");
            User adminUser = new User();
            adminUser.setFirstName("globaladmin");
            adminUser.setSecondName("globaladmin");
            adminUser.setUsername("globaladmin");
            adminUser.setPassword(bCryptPasswordEncoder.encode("globaladmin"));

            HashSet roles = new HashSet<>();
            roles.addAll(roleRepository.findByName(UserRoles.ROLE_USER.toString()));
            roles.addAll(roleRepository.findByName(UserRoles.ROLE_GLOBAL_ADMIN.toString()));

            adminUser.setRoles(roles);
            userRepository.saveAndFlush(adminUser);

            User basicUser = new User();
            basicUser.setFirstName("basicuser");
            basicUser.setSecondName("basicuser");
            basicUser.setUsername("basicuser");
            basicUser.setPassword(bCryptPasswordEncoder.encode("basicuser"));
            roles = new HashSet<>();
            roles.addAll(roleRepository.findByName(UserRoles.ROLE_USER.toString()));

            basicUser.setRoles(roles);
            userRepository.saveAndFlush(basicUser);

        }

        // create a first basic Party
        if (partyRepository.findAll().isEmpty()) {
            LOG.debug("new database initialising first party owned by basic user");

            Party party = new Party();
            party.setFirstName("default_party");
            party.setSecondName("default_party");
            party = partyRepository.saveAndFlush(party);

            User user = userRepository.findByUsername("basicuser");
            LOG.debug("adding to party user:" + user);
            Set<User> users = new HashSet();
            users.add(user);
            party.setUsers(users);

            party = partyRepository.saveAndFlush(party);
            LOG.debug("added party to database:" + party);

        }

//        if (orderRepository.findAll().isEmpty()) {
//            Order mockOrder = mockOrder();
//            OrderEntity orderEntity = OrderMapper.INSTANCE.orderToOrderEntity(mockOrder);
//            orderRepository.save(orderEntity);
//        }
    }

    //TODO REMOVE
    private Order mockOrder() {
        /*
    private Long id;                    
    private String href;
    private String uuid;
    private String name;
    private OrderStatus status;
    private ResourceAccess resourceAccess;                    
    private Date orderDate;
    private Date startDate;
    private Date endDate;
    private String description; 
     private List<OrderHref> subOrders;
   private PartyHref orderOwner;
    private List<OrderChangeRequestHref> changeRequests;
    private OrderHref parentOrder;
    private List<ResourceHref> resourceOrService;
         */
        Order tmporder = new Order();
        tmporder.setDescription("my order");
        tmporder.setStartDate(new Date());
        tmporder.setHref("http://tmp");
        tmporder.setId(1L);
        tmporder.setUuid(UUID.randomUUID().toString());
        tmporder.setStatus(OrderStatus.PLACED);
        tmporder.setResourceAccess(ResourceAccess.EXTERNAL);
        OrderHref parent = new OrderHref();
        tmporder.setParentOrder(parent);
        tmporder.setSubOrders(new LinkedHashSet(Arrays.asList(parent, parent)));

        OrderChangeRequestHref changehref1 = new OrderChangeRequestHref();
        changehref1.setUuid(UUID.randomUUID().toString());
        changehref1.setName("change 1");
        changehref1.setRequestDate(new Date());
        OrderChangeRequestHref changehref2 = new OrderChangeRequestHref();
        changehref2.setUuid(UUID.randomUUID().toString());
        changehref2.setName("change 2");
        changehref2.setRequestDate(new Date());

        List<OrderChangeRequestHref> changeRequests = Arrays.asList(changehref1, changehref2);

        tmporder.setChangeRequests(changeRequests);
        return tmporder;
    }
}
