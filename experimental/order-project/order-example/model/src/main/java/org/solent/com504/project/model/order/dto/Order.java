package org.solent.com504.project.model.order.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.solent.com504.project.model.party.dto.Party;
import org.solent.com504.project.model.party.dto.PartyHref;
import org.solent.com504.project.model.resource.dto.Resource;
import org.solent.com504.project.model.resource.dto.ResourceAccess;
import org.solent.com504.project.model.resource.dto.ResourceHref;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {

    private String href;

    private String uuid;

    private Long id;

    private String name;

    @XmlElementWrapper(name = "subOrders")
    @XmlElement(name = "orderHref")
    private List<OrderHref> subOrders;

    private String description;

    private Date orderDate;

    private Date startDate;

    private Date endDate;

    private PartyHref orderOwner;

    @XmlElementWrapper(name = "changeRequests")
    @XmlElement(name = "orderChangeRequestHref")
    private List<OrderChangeRequestHref> changeRequests;

    private OrderHref parentOrder;

    @XmlElementWrapper(name = "resourceOrService")
    @XmlElement(name = "resourceHref")
    private List<ResourceHref> resourceOrService;

    private OrderStatus status;

    // default internal
    private ResourceAccess resourceAccess = ResourceAccess.INTERNAL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//TODO real problem here - swagger does a recursive list and fails to load this data type
// see https://stackoverflow.com/questions/59598383/swagger-recursively-resolving-dependencies-for-type-infinite-loop
// https://github.com/springfox/springfox/issues/621 Cycles in Java classes cause infinite loop in ModelAttributeParameterExpander

    public List<OrderHref> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<OrderHref> subOrders) {
        this.subOrders = subOrders;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PartyHref getOrderOwner() {
        return orderOwner;
    }

    public void setOrderOwner(PartyHref orderOwner) {
        this.orderOwner = orderOwner;
    }


    public List<OrderChangeRequestHref> getChangeRequests() {
        return changeRequests;
    }

    public void setChangeRequests(List<OrderChangeRequestHref> changeRequests) {
        this.changeRequests = changeRequests;
    }


    public OrderHref getParentOrder() {
        return parentOrder;
    }

    public void setParentOrder(OrderHref parentOrder) {
        this.parentOrder = parentOrder;
    }

    public List<ResourceHref> getResourceOrService() {
        return resourceOrService;
    }

    public void setResourceOrService(List<ResourceHref> resourceOrService) {
        this.resourceOrService = resourceOrService;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public ResourceAccess getResourceAccess() {
        return resourceAccess;
    }

    public void setResourceAccess(ResourceAccess resourceAccess) {
        this.resourceAccess = resourceAccess;
    }
    
    

    @Override
    public String toString() {
        return "Order{" + "href=" + href + ", uuid=" + uuid + ", id=" + id + ", name=" + name + ", subOrders=" + subOrders + ", description=" + description + ", orderDate=" + orderDate + ", startDate=" + startDate + ", endDate=" + endDate + ", orderOwner=" + orderOwner + ", changeRequests=" + changeRequests + ", parentOrder=" + parentOrder + ", resourceOrService=" + resourceOrService + ", status=" + status + ", resourceAccess=" + resourceAccess + '}';
    }
    
    

}
