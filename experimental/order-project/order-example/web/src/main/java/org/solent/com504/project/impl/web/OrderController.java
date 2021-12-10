package org.solent.com504.project.impl.web;

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
import org.solent.com504.project.model.resource.dto.Characteristic;
import org.solent.com504.project.model.resource.dto.Resource;
import org.solent.com504.project.model.resource.dto.ResourceAccess;
import org.solent.com504.project.model.resource.dto.ResourceCatalog;
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

    final static String DATE_FORMAT = "yyyy-MM-dd HH:mm a z";
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
        OrderChangeRequest orderChangeRequest = null;

        // find order change request and order from order change request
        if ("viewChangeRequestUUID".equals(action)) {
            ReplyMessage replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
            orderUuid = orderChangeRequest.getOrderUuid();
            replyMessage = orderService.getOrderByUuid(orderUuid);
            List<Order> orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = orderList.get(0);

        } else if ("viewOrderDetails".equals(action)) {
            // find order and order change request from order
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

        } else {
            throw new IllegalArgumentException("unknown action for page action=" + action);
        }

        model.addAttribute("order", order);
        model.addAttribute("changeOrder", order);
        model.addAttribute("changeRequestUUID", changeRequestUUID);
        model.addAttribute("orderChangeRequest", orderChangeRequest);

        // add message if there are any 
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("message", message);

        model.addAttribute("resourceAccessValues", ResourceAccess.values());
        model.addAttribute("orderStatusValues", OrderStatus.values());
        model.addAttribute("allowChangeButtons", true);
        model.addAttribute("selectedPage", "order");
        model.addAttribute("DATE_FORMAT", DATE_FORMAT);
        return "viewModifyOrder";
    }

    @RequestMapping(value = {"/viewModifyOrder"}, method = RequestMethod.POST)
    public String updateorder(Model model,
            @RequestParam(value = "action", required = true) String action,
            @RequestParam(value = "orderUuid", required = false) String orderUuid,
            @RequestParam(value = "orderResourceAccess", required = false) String orderResourceAccess,
            @RequestParam(value = "ownerPartyUUID", required = false) String ownerPartyUUID,
            @RequestParam(value = "changeRequestUUID", required = false) String changeRequestUUID,
            // fields for changing an order
            @RequestParam(value = "changeOrderName", required = false) String changeOrderName,
            @RequestParam(value = "changeOrderDescription", required = false) String changeOrderDescription,
            @RequestParam(value = "changeOrderOrderDate", required = false) String changeOrderOrderDate,
            @RequestParam(value = "changeOrderStartDate", required = false) String changeOrderStartDate,
            @RequestParam(value = "changeOrderEndDate", required = false) String changeOrderEndDate,
            // fields for changing a service
            @RequestParam(value = "resourceOrServiceUuid", required = false) List<String> resourceOrServiceUuid,
            // fields for changing parent order
            @RequestParam(value = "changeOrderParentOrderUuid", required = false) String changeOrderParentOrderUuid,
            @RequestParam(value = "subOrderUuid", required = false) List<String> changeOrderSubOrderUuid,
            Authentication authentication) {

        LOG.debug("/viewModifyOrder: action=" + action + " orderUuid:" + orderUuid + " changeRequestUUID:" + changeRequestUUID);
        String errorMessage = "";
        String message = "";
        ReplyMessage replyMessage = null;

        // populate change order
        Order changeOrder = new Order();
        try {
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
                changeOrder.setStartDate(endDate);
            }

        } catch (Exception ex) {
            errorMessage = "problem reading order change request: " + ex.getMessage();
            LOG.error("problem reading order change request: ", ex);
        }

        Order order = new Order();
        OrderChangeRequest orderChangeRequest = new OrderChangeRequest();

        if ("addNewOrder".equals(action)) {
            ResourceAccess raccess = ResourceAccess.valueOf(orderResourceAccess);
            order.setResourceAccess(raccess);
            if (ResourceAccess.EXTERNAL == raccess) {

                // external order not placed until change requested
                Party party = partyService.findByUuid(ownerPartyUUID);
                if (party == null) {
                    throw new IllegalArgumentException("unknown party for ownerPartyUUID=" + ownerPartyUUID);
                }
                PartyHref orderOwner = PartyMapper.INSTANCE.partyToHref(party);
                order = new Order();
                order.setOrderDate(new Date());
                order.setStartDate(new Date());
                order.setEndDate(new Date(order.getStartDate().getTime() + 24 * 60 * 60 * 1000)); // plus 24 hours
                order.setResourceAccess(ResourceAccess.EXTERNAL);
                order.setOrderOwner(orderOwner); //. new OrderChangeRequest()
                orderChangeRequest = new OrderChangeRequest();
                orderChangeRequest.setChangeRequest(order);
                orderChangeRequest.setChangeReason("new external order");
                orderChangeRequest.setStatus(ChangeStatus.PENDING_EXTERNAL);
                message = "new order external order template created";
            } else {
                // internal order
                order = new Order();
                order.setOrderDate(new Date());
                order.setStartDate(new Date());
                order.setEndDate(new Date(order.getStartDate().getTime() + 24 * 60 * 60 * 1000)); // plus 24 hours
                replyMessage = orderService.postCreateOrder(order, ownerPartyUUID);
                order = replyMessage.getOrderList().get(0);
                changeRequestUUID = order.getChangeRequests().get(0).getUuid();

                replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
                List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
                if (orderChangeRequestList.isEmpty()) {
                    throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
                }
                orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
                message = "new order internal order created";
            }

        } else if ("synchroniseOrder".equals(action)) {
            // get updated order
            replyMessage = orderService.getOrderByUuid(orderUuid);
            List<Order> orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = replyMessage.getOrderList().get(0);

            message = "order synchronised";

        } else if ("newChangeRequest".equals(action)) {

            replyMessage = orderService.getOrderByUuid(orderUuid);
            List<Order> orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = replyMessage.getOrderList().get(0);
            orderChangeRequest = new OrderChangeRequest();
            orderChangeRequest.setStatus(ChangeStatus.REQUESTED);
            orderChangeRequest.setChangeRequest(order);
            orderChangeRequest.setOrderUuid(orderUuid);
            String changeRequestorPartyUUID = order.getOrderOwner().getUuid(); //todo change to session user
            replyMessage = orderChangeRequestService.postCreateOrderChangeRequest(orderChangeRequest, changeRequestorPartyUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("change request not created");
            }
            changeRequestUUID = replyMessage.getOrderChangeRequestList().get(0).getUuid();

            // get updated order
            replyMessage = orderService.getOrderByUuid(orderUuid);
            orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = replyMessage.getOrderList().get(0);

            // get updated order change request
            replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
            message = "new change request created";

        } else if ("updateChangeRequest".equals(action)) {
            replyMessage = orderChangeRequestService.getOrderChangeRequestByUuid(changeRequestUUID);
            List<OrderChangeRequest> orderChangeRequestList = replyMessage.getOrderChangeRequestList();
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);
            Order changeRequest = orderChangeRequest.getChangeRequest();
            
            Order updatedOrder = OrderMapper.INSTANCE.updateOrderFromOrder(changeOrder, changeRequest);
            orderChangeRequest.setChangeRequest(updatedOrder);
            orderChangeRequest.setRequestDate(new Date()); // update the request date on change
            replyMessage = orderChangeRequestService.putUpdateOrderChangeRequest(orderChangeRequest);
            if (orderChangeRequestList.isEmpty()) {
                throw new IllegalArgumentException("cannot find orderChangeRequest for changeRequestUUID=" + changeRequestUUID);
            }
            orderChangeRequest = replyMessage.getOrderChangeRequestList().get(0);

            replyMessage = orderService.getOrderByUuid(orderUuid);
            List<Order> orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = orderList.get(0);

            message = "change request updated";

        } else if ("submitChangeRequest".equals(action)) {
            replyMessage = orderService.getOrderByUuid(orderUuid);
            List<Order> orderList = replyMessage.getOrderList();
            if (orderList.isEmpty()) {
                throw new IllegalArgumentException("cannot find order for orderUuid=" + orderUuid);
            }
            order = replyMessage.getOrderList().get(0);
            Order updatedOrder = OrderMapper.INSTANCE.updateOrderFromOrder(changeOrder, order);

            replyMessage = orderService.putUpdateOrder(updatedOrder);
            orderList = replyMessage.getOrderList();
            order = replyMessage.getOrderList().get(0);
            message = "change request submitted";

        } else if ("acceptChangeRequest".equals(action)) {
            message = "change request accepted";

        } else if ("rejectChangeRequest".equals(action)) {
            message = "change request rejected";

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
