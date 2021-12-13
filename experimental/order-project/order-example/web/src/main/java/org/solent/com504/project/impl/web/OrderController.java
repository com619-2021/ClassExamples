package org.solent.com504.project.impl.web;

import com.sun.tools.sjavac.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.project.impl.validator.UserValidator;
import org.solent.com504.project.model.dto.ReplyMessage;
import org.solent.com504.project.model.order.dto.ChangeStatus;
import org.solent.com504.project.model.order.dto.Order;
import org.solent.com504.project.model.order.dto.OrderChangeRequest;
import org.solent.com504.project.model.order.dto.OrderChangeRequestHref;
import org.solent.com504.project.model.order.dto.OrderHref;
import org.solent.com504.project.model.order.dto.OrderMapper;
import org.solent.com504.project.model.order.dto.OrderStatus;
import org.solent.com504.project.model.order.service.OrderChangeRequestService;
import org.solent.com504.project.model.order.service.OrderService;
import org.solent.com504.project.model.party.dto.Address;
import org.solent.com504.project.model.party.dto.Party;
import org.solent.com504.project.model.party.dto.PartyHref;
import org.solent.com504.project.model.party.dto.PartyMapper;
import org.solent.com504.project.model.party.dto.PartyRole;
import org.solent.com504.project.model.party.service.PartyService;
import org.solent.com504.project.model.resource.dto.AbstractResourceMapper;
import org.solent.com504.project.model.resource.dto.Characteristic;
import org.solent.com504.project.model.resource.dto.Resource;
import org.solent.com504.project.model.resource.dto.ResourceAccess;
import org.solent.com504.project.model.resource.dto.ResourceCatalog;
import org.solent.com504.project.model.resource.dto.ResourceHref;
import org.solent.com504.project.model.resource.service.ResourceCatalogService;
import org.solent.com504.project.model.resource.service.ResourceInventoryService;
import org.solent.com504.project.model.user.dto.Role;
import org.solent.com504.project.model.user.dto.User;
import org.solent.com504.project.model.user.dto.UserRoles;
import org.solent.com504.project.model.user.service.SecurityService;
import org.solent.com504.project.model.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Transactional
public class OrderController {

    final static Logger LOG = LogManager.getLogger(OrderController.class);

    final static String DATE_FORMAT = "dd/MM/yyyy HH:mm";  // 15/12/2021 17:16 matches date time picker
    final static DateFormat df = new SimpleDateFormat(DATE_FORMAT);

    {
        LOG.debug("OrderController created");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private ResourceCatalogService resourceCatalogService = null;

    @Autowired
    private ResourceInventoryService resourceService = null;

    @Autowired
    private OrderService orderService = null;

    @Autowired
    private OrderChangeRequestService orderChangeRequestService = null;

    // ***************************
    // Methods to modify orders
    // ***************************
    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/order"}, method = RequestMethod.GET)
    public String catalog(Model model) {
        LOG.debug("order called:");

        // get parties
        List<Party> partyList = partyService.findAll();
        model.addAttribute("partyListSize", partyList.size());
        model.addAttribute("partyList", partyList);
        ReplyMessage replyMessage = orderService.getOrderByTemplate(null, null, null);
        List<Order> orderList = replyMessage.getOrderList();

        int orderListSize = orderList.size();
        model.addAttribute("orderListSize", orderListSize);
        model.addAttribute("orderList", orderList);
        model.addAttribute("dateFormat", df);
        model.addAttribute("resourceAccessValues", ResourceAccess.values());

        model.addAttribute("selectedPage", "order");
        model.addAttribute("DATE_FORMAT", DATE_FORMAT);
        return "orders";
    }

    @RequestMapping(value = {"/viewModifyOrder"}, method = RequestMethod.GET)
    public String vieworder(Model model,
            @RequestParam(value = "action", required = true) String action,
            @RequestParam(value = "orderUuid", required = false) String orderUuid,
            @RequestParam(value = "changeRequestUUID", required = false) String changeRequestUUID,
            @RequestParam(value = "ownerPartyUUID", required = false) String ownerPartyUUID,
            Authentication authentication) {

        LOG.debug("/viewModifyOrder: action:" + action);
        String errorMessage = "";
        String message = "";

        Order order = null;
        Order changeOrder = null;
        OrderChangeRequest orderChangeRequest = null;

        // find changerequestOrder change request and changerequestOrder from changerequestOrder change request
        if ("viewChangeRequestUUID".equals(action)) {
            ReplyMessage replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
            orderUuid = orderChangeRequest.getOrderUuid();
            changeOrder = orderChangeRequest.getChangeRequest();
            if (orderUuid != null) { // if null then this is an external changerequestOrder
                replyMessage = orderService.getOrderByUuid(orderUuid);
                List<Order> orderList = replyMessage.getOrderList();
                if (orderList.isEmpty()) {
                    throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
                }
                order = orderList.get(0);
            } else {
                order = dummyExternalOrder();
            }

        } else if ("viewOrderDetails".equals(action)) {
            // find changerequestOrder and changerequestOrder change request from changerequestOrder
            ReplyMessage replyMessage = orderService.getOrderByUuid(orderUuid);
            List<Order> orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = orderList.get(0);
            changeRequestUUID = order.getChangeRequests().get(0).getUuid();
            replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
            changeOrder = orderChangeRequest.getChangeRequest();

        } else {
            throw new IllegalArgumentException("unknown action for page action=" + action);
        }

        model.addAttribute("order", order);
        model.addAttribute("changeOrder", changeOrder);
        model.addAttribute("changeRequestUUID", changeRequestUUID);
        model.addAttribute("orderChangeRequest", orderChangeRequest);

        // add message if there are any 
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("message", message);

        model.addAttribute("resourceAccessValues", ResourceAccess.values());
        model.addAttribute("orderStatusValues", OrderStatus.values());
        if (ChangeStatus.APPROVED == orderChangeRequest.getStatus() || ChangeStatus.REJECTED == orderChangeRequest.getStatus()) {
            model.addAttribute("allowChangeButtons", false);
        } else {
            model.addAttribute("allowChangeButtons", true);
        }
        model.addAttribute("selectedPage", "order");
        model.addAttribute("DATE_FORMAT", DATE_FORMAT);
        return "viewModifyOrder";
    }

    private Order dummyExternalOrder() {
        Order order = new Order();
        order.setHref(null);
        order.setId(null);
        order.setUuid("EXTERNAL UNDEFINED");
        order.setHref("EXTERNAL UNDEFINED");
        order.setResourceOrService(new ArrayList());
        order.setOrderDate(new Date());
        order.setStartDate(new Date());
        order.setEndDate(new Date(order.getStartDate().getTime() + 24 * 60 * 60 * 1000)); // plus 24 hours
        order.setResourceAccess(ResourceAccess.EXTERNAL);
        order.setStatus(OrderStatus.PENDING_PLACEMENT);
        return order;
    }

    @RequestMapping(value = {"/viewModifyOrder"}, method = RequestMethod.POST)
    public String updateorder(Model model,
            @RequestParam(value = "action", required = true) String action,
            @RequestParam(value = "orderUuid", required = false) String orderUuid,
            @RequestParam(value = "orderResourceAccess", required = false) String orderResourceAccess,
            @RequestParam(value = "ownerPartyUUID", required = false) String ownerPartyUUID,
            @RequestParam(value = "changeRequestUUID", required = false) String changeRequestUUID,
            // fields for changing an changerequestOrder
            @RequestParam(value = "changeOrderName", required = false) String changeOrderName,
            @RequestParam(value = "changeOrderDescription", required = false) String changeOrderDescription,
            @RequestParam(value = "changeOrderOrderDate", required = false) String changeOrderOrderDate,
            @RequestParam(value = "changeOrderStartDate", required = false) String changeOrderStartDate,
            @RequestParam(value = "changeOrderEndDate", required = false) String changeOrderEndDate,
            // changing change request
            @RequestParam(value = "changeReason", required = false) String changeReason,
            @RequestParam(value = "responseDescription", required = false) String responseDescription,
            // fields for changing a service
            @RequestParam(value = "resourceOrServiceUuid", required = false) List<String> resourceOrServiceUuid,
            // fields for changing parent changerequestOrder
            @RequestParam(value = "changeOrderParentOrderUuid", required = false) String changeOrderParentOrderUuid,
            @RequestParam(value = "subOrderUuid", required = false) List<String> changeOrderSubOrderUuid,
            @RequestParam(value = "changeOrderResourceAccess", required = false) String changeOrderResourceAccess,
            @RequestParam(value = "removeChangeRequestResources", required = false) List<String> removeChangeRequestResources,
            Authentication authentication) {

        LOG.debug("/viewModifyOrder: action=" + action + " orderUuid:" + orderUuid + " changeRequestUUID:" + changeRequestUUID);
        String errorMessage = "";
        String message = "";
        ReplyMessage replyMessage = null;

        // populate change changerequestOrder
        Order changeOrder = new Order();
        try {
            if (changeOrderResourceAccess != null) {
                ResourceAccess resaccess = ResourceAccess.valueOf(changeOrderResourceAccess);
                changeOrder.setResourceAccess(resaccess);
            }
            if (changeOrderName != null) {
                changeOrder.setName(changeOrderName);
            }
            if (changeOrderDescription != null) {
                changeOrder.setDescription(changeOrderDescription);
            }
            if (changeOrderOrderDate != null) {
                Date orderDate = df.parse(changeOrderOrderDate);
                changeOrder.setOrderDate(orderDate);
            }
            if (changeOrderStartDate != null) {
                Date startDate = df.parse(changeOrderStartDate);
                changeOrder.setStartDate(startDate);
            }
            if (changeOrderEndDate != null) {
                Date endDate = df.parse(changeOrderEndDate);
                changeOrder.setEndDate(endDate);
            }
            LOG.debug("viewModifyOrder update change request with:" + changeOrder);
        } catch (Exception ex) {
            errorMessage = "problem reading order change request: " + ex.getMessage();
            LOG.error("problem reading order change request: ", ex);
        }

        Order order;
        OrderChangeRequest orderChangeRequest;

        if ("addNewOrder".equals(action)) {
            Party party = partyService.findByUuid(ownerPartyUUID);
            if (party == null) {
                throw new IllegalArgumentException("unknown party for ownerPartyUUID=" + ownerPartyUUID);
            }
            PartyHref orderOwner = PartyMapper.INSTANCE.partyToHref(party);

            // check if external or internal resource access
            ResourceAccess raccess = ResourceAccess.valueOf(orderResourceAccess);
            if (ResourceAccess.EXTERNAL == raccess) {
                // external changerequestOrder only for display
                order = dummyExternalOrder();
                order.setOrderOwner(orderOwner); // used to contact external machine
                String changeRequestorPartyUUID = orderOwner.getUuid(); // should be the initiator of this action 
                orderChangeRequest = new OrderChangeRequest();
                orderChangeRequest.setChangeRequest(order);
                orderChangeRequest.setChangeReason("new external order");
                // external changerequestOrder not placed until change requested
                orderChangeRequest.setStatus(ChangeStatus.PENDING_EXTERNAL);
                replyMessage = orderChangeRequestService.postCreateOrderChangeRequest(orderChangeRequest, changeRequestorPartyUUID);
                List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
                orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
                changeOrder = orderChangeRequest.getChangeRequest();
                message = "New external order change request created. You must now fill in the request and submit order";
            } else {
                // internal changerequestOrder
                order = new Order();
                order.setOrderDate(new Date());
                order.setStartDate(new Date());
                order.setEndDate(new Date(order.getStartDate().getTime() + 24 * 60 * 60 * 1000)); // plus 24 hours
                order.setResourceAccess(ResourceAccess.INTERNAL);
                order.setOrderOwner(orderOwner);
                replyMessage = orderService.postCreateOrder(order, ownerPartyUUID);
                order = replyMessage.getOrderList().get(0);
                changeRequestUUID = order.getChangeRequests().get(0).getUuid();
                replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
                List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
                if (orderChangeRequestList.isEmpty()) {
                    throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
                }
                orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
                changeOrder = orderChangeRequest.getChangeRequest();
                message = "new internal order created";
            }

        } else if ("synchroniseOrder".equals(action)) {
            // get updated changerequestOrder
            replyMessage = orderService.getOrderByUuid(orderUuid);
            List<Order> orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = replyMessage.getOrderList().get(0);

            replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
            changeOrder = orderChangeRequest.getChangeRequest();

            message = "order synchronised";

        } else if ("newChangeRequest".equals(action)) {

            // create new change request for changerequestOrder
            //find changerequestOrder
            replyMessage = orderService.getOrderByUuid(orderUuid);
            List<Order> orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = replyMessage.getOrderList().get(0);

            // create new chagne request object
            orderChangeRequest = new OrderChangeRequest();
            orderChangeRequest.setStatus(ChangeStatus.REQUESTED);
            orderChangeRequest.setChangeRequest(order);
            orderChangeRequest.setOrderUuid(orderUuid);

            // find changerequestOrder owner
            String changeRequestorPartyUUID = order.getOrderOwner().getUuid(); //todo change to session user

            // create a new change request
            replyMessage = orderChangeRequestService.postCreateOrderChangeRequest(orderChangeRequest, changeRequestorPartyUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("change request not created");
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
            changeRequestUUID = orderChangeRequest.getUuid();
            changeOrder = orderChangeRequest.getChangeRequest();

            // get updated changerequestOrder
            replyMessage = orderService.getOrderByUuid(orderUuid);
            orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = replyMessage.getOrderList().get(0);

            message = "new change request created";

        } else if ("updateChangeRequest".equals(action)) {
            // get the original change request
            replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);

            // update change request fields
            orderChangeRequest.setChangeRequest(changeOrder);
            orderChangeRequest.setResponseDescription(responseDescription);

            // get the original changeRequestOrder
            Order changeRequestOrder = orderChangeRequest.getChangeRequest();

            LOG.debug("************ changeRequestOrder=" + changeRequestOrder);
            LOG.debug("************ changeOrder=" + changeOrder);

            // update the changerequestOrder with the new date from the inputs
            Order updatedOrder = OrderMapper.INSTANCE.updateOrderFromOrder(changeOrder, changeRequestOrder);
            orderChangeRequest.setChangeRequest(updatedOrder);
            changeOrder = updatedOrder; // push this back to jsp model

            LOG.debug("************ updatedOrder=" + updatedOrder);

            // save the new change request
            orderChangeRequest.setRequestDate(new Date()); // update the request date on change
            replyMessage = orderChangeRequestService.putUpdateOrderChangeRequest(orderChangeRequest);
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);

            // change request for external changerequestOrder
            if (ResourceAccess.EXTERNAL == changeOrder.getResourceAccess()) {
                order = dummyExternalOrder();
            } else { // change request for internal changerequestOrder
                orderUuid = orderChangeRequest.getOrderUuid();
                replyMessage = orderService.getOrderByUuid(orderUuid);
                List<Order> orderList = replyMessage.getOrderList();
                if (orderList.isEmpty()) {
                    throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
                }
                order = orderList.get(0);
            }

            message = "change request updated";

        } else if ("submitChangeRequest".equals(action)) {

            replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);

            if (orderChangeRequest.getOrderUuid() == null || ResourceAccess.EXTERNAL == orderChangeRequest.getChangeRequest().getResourceAccess()) {
                // external
                order = this.dummyExternalOrder();

            } else { // internal
                replyMessage = orderService.getOrderByUuid(orderChangeRequest.getOrderUuid());
                List<Order> orderList = replyMessage.getOrderList();
                if (orderList.isEmpty()) {
                    throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
                }
                order = replyMessage.getOrderList().get(0);

                Order updatedOrder = OrderMapper.INSTANCE.updateOrderFromOrder(changeOrder, order);
                replyMessage = orderService.putUpdateOrder(updatedOrder);
                orderList = replyMessage.getOrderList();
                order = replyMessage.getOrderList().get(0);
            }

            changeOrder = orderChangeRequest.getChangeRequest();

            message = "change request submitted";

        } else if ("acceptChangeRequest".equals(action)) {
            message = "change request accepted";
            order = new Order();
            orderChangeRequest = new OrderChangeRequest();

        } else if ("rejectChangeRequest".equals(action)) {
            message = "change request rejected";
            order = new Order();
            orderChangeRequest = new OrderChangeRequest();
        } else if ("deleteChangeRequestResources".equals(action)) {

            replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
            changeOrder = orderChangeRequest.getChangeRequest();

            if (removeChangeRequestResources != null) {
                // iterate and remove change requests
                for (String removeResource : removeChangeRequestResources) {
                    List<ResourceHref> resourceOrService = changeOrder.getResourceOrService();
                    Iterator<ResourceHref> serviceIterator = resourceOrService.iterator();
                    while (serviceIterator.hasNext()) {
                        ResourceHref rHref = serviceIterator.next();
                        if (removeResource.equals(rHref.getUuid())) {
                            serviceIterator.remove();
                        }
                    }
                }
            }
            replyMessage = orderChangeRequestService.putUpdateOrderChangeRequest(orderChangeRequest);
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);

            if (orderChangeRequest.getOrderUuid() == null || ResourceAccess.EXTERNAL == orderChangeRequest.getChangeRequest().getResourceAccess()) {
                // external
                order = this.dummyExternalOrder();

            } else { // internal
                replyMessage = orderService.getOrderByUuid(orderChangeRequest.getOrderUuid());
                List<Order> orderList = replyMessage.getOrderList();
                if (orderList.isEmpty()) {
                    throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
                }
                order = replyMessage.getOrderList().get(0);
            }

            message = "removed change request resources";

        } else {
            throw new IllegalArgumentException("unknown action for page action=" + action);
        }

        model.addAttribute("order", order);
        model.addAttribute("changeOrder", changeOrder);
        model.addAttribute("changeRequestUUID", changeRequestUUID);
        model.addAttribute("orderChangeRequest", orderChangeRequest);

        // add message if there are any 
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("message", message);

        model.addAttribute("resourceAccessValues", ResourceAccess.values());
        model.addAttribute("orderStatusValues", OrderStatus.values());

        if (ChangeStatus.APPROVED == orderChangeRequest.getStatus() || ChangeStatus.REJECTED == orderChangeRequest.getStatus()) {
            model.addAttribute("allowChangeButtons", false);
        } else {
            model.addAttribute("allowChangeButtons", true);
        }

        model.addAttribute("selectedPage", "order");
        model.addAttribute("DATE_FORMAT", DATE_FORMAT);
        return "viewModifyOrder";
    }


    /* ******
    * methods to see changerequestOrder change requests
     */
    /**
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/orderchange"}, method = RequestMethod.GET)
    public String orderchange(Model model
    ) {
        LOG.debug("order change called:");

        // get parties
        List<Party> partyList = partyService.findAll();
        model.addAttribute("partyListSize", partyList.size());
        model.addAttribute("partyList", partyList);

        ReplyMessage replyMessage = orderService.getOrderByTemplate(null, null, null);
        List<Order> orderList = replyMessage.getOrderList();

        replyMessage = orderChangeRequestService.getOrderChangeRequestByTemplate(null, null, null);
        List<OrderChangeRequest> orderChangeRequesList = replyMessage.getOrderChangeRequestList();

        Integer orderListSize = orderList.size();
        model.addAttribute("orderListSize", orderListSize);
        model.addAttribute("orderList", orderList);

        Integer orderChangeRequestListSize = orderChangeRequesList.size();
        model.addAttribute("orderChangeRequestListSize", orderChangeRequestListSize);
        model.addAttribute("orderChangeRequestList", orderChangeRequesList);

        model.addAttribute("dateFormat", df); //TODO remove
        model.addAttribute("resourceAccessValues", ResourceAccess.values());

        model.addAttribute("selectedPage", "orderchange");
        model.addAttribute("DATE_FORMAT", DATE_FORMAT);
        return "orderchange";
    }

    // ***************************
    // Methods to add remove resources from changerequestOrder change request
    // ***************************
    @RequestMapping(value = {"/resourceselect"}, method = {RequestMethod.POST, RequestMethod.GET})
    @Transactional
    public String resourceselect(Model model,
            @RequestParam(value = "action", required = true) String action,
            @RequestParam(value = "orderResourceAccess", required = false) String orderResourceAccess,
            @RequestParam(value = "ownerPartyUUID", required = false) String ownerPartyUUID,
            @RequestParam(value = "changeRequestUUID", required = false) String changeRequestUUID,
            @RequestParam(value = "addResources", required = false) List<String> addResources,
            Authentication authentication
    ) {

        LOG.debug("resourceselect called: action=" + action);

        if (addResources == null) {
            LOG.debug("**** addResources==null");
            addResources = new ArrayList();
        }

        ReplyMessage replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
        List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
        if (orderChangeRequestList.isEmpty()) {
            throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
        }
        OrderChangeRequest orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
        Order changerequestOrder = orderChangeRequest.getChangeRequest();

        // check if external or internal resource access
        ResourceAccess raccess = changerequestOrder.getResourceAccess();
        if (ResourceAccess.EXTERNAL == raccess) {
            LOG.debug("resourceselect updateing External " + action);
        }

        if ("updateOrderChangeRequest".equals(action)) {
            LOG.debug("**** processing addResource.size " + addResources.size());
            for (String addResource : addResources) {
                LOG.debug("**** adding resource " + addResource);
                replyMessage = resourceService.getResourceByuuid(addResource);
                List<Resource> resourceList = replyMessage.getResourceList();
                if (resourceList.isEmpty()) {
                    LOG.debug("**** resource not found uuid=" + addResource);
                } else {
                    Resource resource = resourceList.get(0);
                    ResourceHref resourceHref = AbstractResourceMapper.INSTANCE.abstractResourceToResourceHref(resource);
                    LOG.debug("**** adding resourceHref " + resourceHref);
                    changerequestOrder.getResourceOrService().add(resourceHref);
                }
            }
            LOG.debug("**** saving changerequestorder " + changerequestOrder);
            LOG.debug("**** saving orderChangeRequest " + orderChangeRequest);
            replyMessage = orderChangeRequestService.putUpdateOrderChangeRequest(orderChangeRequest);
        }

        ReplyMessage reply = resourceService.getResourceByTemplate(null, 0, 20);
        List<Resource> resourceList = reply.getResourceList();

        model.addAttribute("abstractResourceListSize", resourceList.size());
        model.addAttribute("abstractResourceList", resourceList);

        model.addAttribute("orderResourceAccess", orderResourceAccess);
        model.addAttribute("ownerPartyUUID", ownerPartyUUID);
        model.addAttribute("changeRequestUUID", changeRequestUUID);
        model.addAttribute("addResources", addResources);

        model.addAttribute("DATE_FORMAT", DATE_FORMAT);
        return "resourceselect";
    }

    private Map<String, String> selectedRolesMap(User user) {

        List<String> availableRoles = userService.getAvailableUserRoleNames();

        List<String> selectedRoles = new ArrayList();
        for (Role role : user.getRoles()) {
            selectedRoles.add(role.getName());
            LOG.debug("user " + user.toString()
                    + "roles from database:" + role.getName());
        }

        Map<String, String> selectedRolesMap = new LinkedHashMap();
        for (String availableRole : availableRoles) {
            if (selectedRoles.contains(availableRole)) {
                selectedRolesMap.put(availableRole, "checked");
                LOG.debug("availableRole " + availableRole
                        + " user " + user.toString() + " available role:checked");
            } else {
                selectedRolesMap.put(availableRole, "");
                LOG.debug("availableRole " + availableRole
                        + " user " + user.toString() + " available role:not checked");
            }
        }

        return selectedRolesMap;

    }

    /**
     * returns true if the party has the role specified
     *
     * @param role
     * @return
     */
    private boolean hasRole(String role) {
        Collection<GrantedAuthority> authorities
                = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            hasRole = authority.getAuthority().equals(role);
            if (hasRole) {
                break;
            }
        }
        return hasRole;
    }

}
