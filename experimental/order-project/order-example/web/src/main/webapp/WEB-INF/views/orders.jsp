<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- start of contact.jsp selectedPage=${selectedPage}-->
<jsp:include page="header.jsp" />

<!-- Begin page content -->
<main role="main" class="container">    <div>
        <h1>Manage Orders</h1>
        <p>showing ${orderListSize} orders: </p>
        <table class="table">
            <thead>
                <tr>
                    <!--
    private Long id;
    private String uuid;                    
    private String href;
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
                    -->

                    <th scope="col">Id</th>
                    <th scope="col">uuid</th>
                    <th scope="col">href</th>
                    <th scope="col">name</th>
                    <th scope="col">status</th>
                    <th scope="col">resourceAccess</th>
                    <th scope="col">orderDate</th>
                    <th scope="col">startDate</th>
                    <th scope="col">endDate</th>

                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orderList}">
                    <tr>
                        <td>${order.id}</td>
                        <td>${order.uuid}</td>
                        <td><a href=".${order.href}" target="_blank" >${order.href}</a></td>
                        <td>${order.name}</td>
                        <td>${order.status}</td>
                        <td>${order.resourceAccess}</td>
                        <td><fmt:formatDate pattern="${DATE_FORMAT}" value="${order.orderDate}" /></td>
                        <td><fmt:formatDate pattern="${DATE_FORMAT}" value="${order.startDate}" /></td>
                        <td><fmt:formatDate pattern="${DATE_FORMAT}" value="${order.endDate}" /></td>

                        <td>
                            <form action="./viewModifyOrder" method="GET">
                                <input type="hidden" name="orderUuid" value="${order.uuid}">
                                <input type="hidden" name="action" value="viewOrderDetails">
                                <button class="btn" type="submit" >View Order Details</button>
                            </form> 
                        </td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>
        <h1>Create New Order</h1>
        <form action="./viewModifyOrder" method="POST">
            <!-- orderUuid ="" creates a new order -->
            <p>Select Owning Party</p>
            <select class="form-control" name="ownerPartyUUID" >
                <c:forEach var="party" items="${partyList}">
                    <option value="${party.uuid}">${party.firstName}  ${party.uuid}</option>
                </c:forEach>
            </select>
            <p>Select Order Type</p>
            <select class="form-control" name="orderResourceAccess" >
                <c:forEach var="resourceAccessValue" items="${resourceAccessValues}">
                    <option value="${resourceAccessValue}">${resourceAccessValue}</option>
                </c:forEach>
            </select>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="action" value="addNewOrder">
            <button class="btn" type="submit" >Add New Order</button>
        </form> 
</main>


<jsp:include page="footer.jsp" />
