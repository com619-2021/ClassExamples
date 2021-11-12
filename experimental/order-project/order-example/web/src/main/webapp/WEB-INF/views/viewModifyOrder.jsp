<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var = "selectedPage" value = "contact" scope="request"/>
<!-- start of contact.jsp selectedPage=${selectedPage}-->
<jsp:include page="header.jsp" />

<!-- Begin page content -->

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
<main role="main" class="container">
    <H1>View and Modify Order</H1>
    

    <!-- left hand side current order -->
    <div class="col-xs-6">
        <h3 class="sub-header">Current Order</h3>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th class="col-md-1">Property</th>
                        <th class="col-md-2">Current Value</th>
                        <th class="col-md-3"></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="col-md-1">order Id</td>
                        <td class="col-md-2"><input type="text" name="orderId" value="${order.id}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Uuid</td>
                        <td class="col-md-2"><input type="text" name="orderUuid" value="${order.uuid}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Href</td>
                        <td class="col-md-2"><input type="text" name="orderHref" value="${order.href}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Name</td>
                        <td class="col-md-2"><input type="text" name="orderName" value="${order.name}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Status</td>
                        <td class="col-md-2"><input type="text" name="orderStatus" value="${order.status}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Resource Access</td>
                        <td class="col-md-2"><input type="text" name="orderStatus" value="${order.resourceAccess}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Latest Update to Order</td>
                        <td class="col-md-2"><input type="text" name="orderDate" value="${order.orderDate}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Start Date of Order</td>
                        <td class="col-md-2"><input type="text" name="orderDate" value="${order.startDate}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">End Date of Order</td>
                        <td class="col-md-2"><input type="text" name="orderDate" value="${order.endDate}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>

                </tbody>
            </table>
        </div>
    </div>
    <div class="col-xs-6">

            <select class="form-control" name="changeRequestUUID" >
                <c:forEach var="changeRequest" items="${order.changeRequests}">
                    <option value="${changeRequest.uuid}">${changeRequest.requestDate} ${changeRequest.name}  ${changeRequest.uuid}</option>
                </c:forEach>
            </select>


        <h3 class="sub-header">change request</h3>
         <table class="table table-striped"  >
                <thead>
                    <tr>
                        <th class="col-md-1">Property</th>
                        <th class="col-md-2">Current Value</th>
                        <th class="col-md-3"></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="col-md-1">changeOrder Id</td>
                        <td class="col-md-2"><input type="text" name="changeOrderId" value="${changeOrder.id}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">change Order Uuid</td>
                        <td class="col-md-2"><input type="text" name="changeOrderUuid" value="${changeOrder.uuid}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Href</td>
                        <td class="col-md-2"><input type="text" name="changeOrderHref" value="${changeOrder.href}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Name</td>
                        <td class="col-md-2"><input type="text" name="changeOrderName" value="${changeOrder.name}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Status</td>
                        <td class="col-md-2"><input type="text" name="changeOrderStatus" value="${changeOrder.status}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Resource Access</td>
                        <td class="col-md-2"><input type="text" name="changeOrderStatus" value="${changeOrder.resourceAccess}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Latest Update to Order</td>
                        <td class="col-md-2"><input type="text" name="changeOrderDate" value="${changeOrder.orderDate}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Start Date of Order</td>
                        <td class="col-md-2"><input type="text" name="changeOrderDate" value="${changeOrder.startDate}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">End Date of Order</td>
                        <td class="col-md-2"><input type="text" name="changeendDate" value="${changeOrder.endDate}" readonly /></td>
                        <td class="col-md-3"></td>
                    </tr>

                </tbody>
            </table>
        </div>
</main>


<jsp:include page="footer.jsp" />
