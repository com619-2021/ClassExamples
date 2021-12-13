<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
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
    <!-- print error message if there is one -->
    <div style="color:red;">${errorMessage}</div>
    <div style="color:green;">${message}</div>

    <div class="row">

        <div class="col-xs-6">
            <c:if test="${changeOrder.resourceAccess != 'EXTERNAL'}">
                <H1>View and Modify Order</H1>
                </c:if>
                <c:if test="${changeOrder.resourceAccess == 'EXTERNAL'}">
                <H1>View and Modify External Order</H1>
                    <c:if test="${order.orderDate != null }">
                    <form action="./viewModifyOrder" method="POST">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="action" value="synchroniseOrder">
                        <input type="hidden" name="orderUuid" value="${order.uuid}"/></td>
                        <button class="btn" type="submit" >Synchronise Order</button>
                    </form>
                </c:if>
            </c:if>
        </div>
        <div class="col-xs-6">
            <c:if test="${orderChangeRequest.status != 'PENDING_EXTERNAL'}">
                <form action="./viewModifyOrder" method="get">
                    <input type="hidden" name="action" value="viewChangeRequestUUID"/>
                    <select class="form-control" name="changeRequestUUID" onchange="this.form.submit()">
                        <c:forEach var="changeRequest" items="${order.changeRequests}">
                            <option value="${changeRequest.uuid}"
                                    <c:if test="${changeRequest.uuid == changeRequestUUID}">selected</c:if>
                                    >Date: <fmt:formatDate pattern="${DATE_FORMAT}" value="${changeRequest.requestDate}" /> Name: ${changeRequest.name}  UUID: ${changeRequest.uuid}</option>
                        </c:forEach>
                    </select>
                </form>
                <form action="./viewModifyOrder" method="post">
                    <input type="hidden" name="action" value="newChangeRequest"/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="orderUuid" value="${order.uuid}">
                    <button class="btn" type="submit" >New Change Request</button>
                </form>
            </c:if>
            <br>
            <table class="table table-striped">
                <tbody>
                    <tr>
                        <td class="col-md-1">Request UUID</td>
                        <td class="col-md-1">${orderChangeRequest.uuid}</td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Request href</td>
                        <td class="col-md-1"><a href=".${orderChangeRequest.href}" target="_blank" >${orderChangeRequest.href}</a></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Request Status</td>
                        <td class="col-md-2">${orderChangeRequest.status}</td>
                        <td class="col-md-3"></td>
                    <tr>
                        <td class="col-md-1">Request Date</td>
                        <td class="col-md-1"><fmt:formatDate pattern="${DATE_FORMAT}" value="${orderChangeRequest.requestDate}" /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Approved Date</td>
                        <td class="col-md-2"><fmt:formatDate pattern="${DATE_FORMAT}" value="${orderChangeRequest.approvedDate}" /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Change Reason</td>
                        <td class="col-md-2"><input form="form3" type="text" id="changeReason"  name="changeReason" value="${changeReason}" <c:if test="${changeReason == null}">disabled </c:if> /></td>
                        <td class="col-md-3"><button class="btn btn-sm" type="button" onclick="toggleVisabilityAndDisabled('changeReason')" <c:if test="${! allowChangeButtons}">disabled style="display:none"</c:if>>change</button></td>
                        </tr>
                        <tr>
                            <td class="col-md-1">Response Description</td>
                            <td class="col-md-2"><input form="form3" type="text" id="responseDescription"  name="responseDescription" value="${responseDescription}" <c:if test="${responseDescription == null}">disabled </c:if> /></td>
                        <td class="col-md-3"><button class="btn btn-sm" type="button" onclick="toggleVisabilityAndDisabled('responseDescription')" <c:if test="${! allowChangeButtons}">disabled style="display:none"</c:if>>change</button></td>
                        </tr>
                </table>
            </div>
        </div>
    </div>

    <div class="row">

        <!-- left hand side current order -->
        <div class="col-xs-6">
            <h3 class="sub-header">Current Order</h3>
            <h4 class="sub-header">Current Order Resources</h4>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th class="col-md-1">Name</th>
                            <th class="col-md-2">uuid</th>
                            <th class="col-md-3">href</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="resourceHref" items="${order.resourceOrService}">
                        <tr>
                            <td class="col-md-1">${resourceHref.name}</td>
                            <td class="col-md-2">${resourceHref.uuid}</td>
                            <td class="col-md-3"><a href="<c:if test="${! fn:startsWith(resourceHref.href , 'http')}">.</c:if>${resourceHref.href}" target="_blank" >${resourceHref.href}</a></td>
                            </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- right hand side -->
    <div class="col-xs-6">
        <h3 class="sub-header">Change Request Values</h3>
        <h4 class="sub-header">Change Order Resources</h4>

        <form form="form2" action="./viewModifyOrder" method="POST">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th class="col-md-1">Name</th>
                            <th class="col-md-2">uuid</th>
                            <th class="col-md-3">href</th>
                            <th class="col-md-4">BUTTON</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="resourceHref" items="${changeOrder.resourceOrService}">
                            <tr>
                                <td class="col-md-1">${resourceHref.name}</td>
                                <td class="col-md-2">${resourceHref.uuid}</td>
                                <td class="col-md-3"><a href="<c:if test="${! fn:startsWith(resourceHref.href , 'http')}">.</c:if>${resourceHref.href}" target="_blank" >${resourceHref.href}</a></td>
                                <td class="col-md-4">Remove <input type="checkbox"  name="removeChangeRequestResources" value="${resourceHref.uuid}"></td>  
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="action" value="deleteChangeRequestResources">
            <input type="hidden" name="changeRequestUUID" value="${orderChangeRequest.uuid}">
            <input type="hidden" name="orderUuid" value="${order.uuid}"/>
            <input type="hidden" name="changeOrderResourceAccess" value="${changeOrder.resourceAccess}"/>
            <c:if test="${allowChangeButtons}">
                <button class="btn" type="submit" >Delete Selected Resources</button>
            </c:if>
        </form>
        <c:if test="${allowChangeButtons}">
            <input class="btn" type="button" name="s1" value="Add Resources" 
                   onClick="window.open('resourceselect?action=viewResources&changeRequestUUID=${orderChangeRequest.uuid}&changeOrderResourceAccess=${changeOrder.resourceAccess}&orderOwnerUuid=${order.orderOwner.uuid}',
                                   'mywindow', 'width=600,  height=400,toolbar=no,resizable=yes,menubar=yes')" />
        </c:if>


    </div>
</div>

<div class="row">

    <!-- left hand side current order -->
    <div class="col-xs-6">
        <h4>Order Details</h4>
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
                        <td class="col-md-1">Order Id</td>
                        <td class="col-md-2">${order.id}</td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Uuid</td>
                        <td class="col-md-2">${order.uuid}</td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Href</td>
                        <td class="col-md-2"><a href=".${order.href}" target="_blank" >${order.href}</a></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Owner</td>
                        <td class="col-md-2">${order.orderOwner.name}</td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Name</td>
                        <td class="col-md-2">${order.name}</td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Description</td>
                        <td class="col-md-2">${order.description}</td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Status</td>
                        <td class="col-md-2">${order.status}</td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Order Resource Access</td>
                        <td class="col-md-2">${order.resourceAccess}</td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Latest Update to Order</td>
                        <td class="col-md-2"><fmt:formatDate pattern="${DATE_FORMAT}" value="${order.orderDate}" /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">Start Date of Order</td>
                        <td class="col-md-2"><fmt:formatDate pattern="${DATE_FORMAT}" value="${order.startDate}" /></td>
                        <td class="col-md-3"></td>
                    </tr>
                    <tr>
                        <td class="col-md-1">End Date of Order</td>
                        <td class="col-md-2"><fmt:formatDate pattern="${DATE_FORMAT}" value="${order.endDate}" /></td>
                        <td class="col-md-3"></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- right hand side -->
    <div class="col-xs-6">

        <h4>Change Order Details</h4>
        <form form="form3" action="./viewModifyOrder" method="POST">
            <div class="table-responsive">
                <table class="table table-striped"  >
                    <thead>
                        <tr>
                            <th class="col-md-1">Property</th>
                            <th class="col-md-2">Change Request Value</th>
                            <th class="col-md-3"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="col-md-1">Order Id</td>
                            <th class="col-md-2">${changeOrder.id}</th>
                            <th class="col-md-3"></th>
                        </tr>
                        <tr>
                            <td class="col-md-1">Order Uuid</td>
                            <th class="col-md-2">${changeOrder.uuid}</th>
                            <th class="col-md-3"></th>
                        </tr>
                        <tr>
                            <td class="col-md-1">Order Href</td>
                            <th class="col-md-2"><a href=".${changeOrder.href}" target="_blank" >${changeOrder.href}</a></th>
                            <th class="col-md-3"></th>
                        </tr>
                        <tr>
                            <td class="col-md-1">Order Owner</td>
                            <td class="col-md-2">${changeOrder.orderOwner.name}</td>
                            <td class="col-md-3"></td>
                        </tr>
                        <tr>
                            <td class="col-md-1">Order Name</td>
                            <td class="col-md-2"><input  type="text" id="changeOrderName" name="changeOrderName" value="${changeOrder.name}" <c:if test="${changeOrder.name == null}">disabled style="display:none"</c:if> /></td>
                            <td class="col-md-3"><button class="btn btn-sm" type="button" onclick="toggleVisabilityAndDisabled('changeOrderName')" <c:if test="${! allowChangeButtons}">disabled style="display:none"</c:if>>change</button></td>
                            </tr>
                            <tr>
                                <td class="col-md-1">Order Description</td>
                                <td class="col-md-2"><input type="text" id="changeOrderDescription" name="changeOrderDescription" value="${changeOrder.description}" <c:if test="${changeOrder.description == null}">disabled style="display:none"</c:if> /></td>
                            <td class="col-md-3"><button class="btn btn-sm" type="button" onclick="toggleVisabilityAndDisabled('changeOrderDescription')" <c:if test="${! allowChangeButtons}">disabled style="display:none"</c:if>>change</button></td>
                            </tr>
                            <tr>
                                <td class="col-md-1">Order Status</td>
                                <td class="col-md-2">${changeOrder.status}</td>
                            <td class="col-md-3"></td>
                        </tr>
                        <tr>
                            <td class="col-md-1">Order Resource Access</td>
                            <td class="col-md-2">${changeOrder.resourceAccess}</td>
                            <td class="col-md-3"></td>
                        </tr>
                        <tr>
                            <td class="col-md-1">Latest Update to Order</td>
                            <td class="col-md-2"><fmt:formatDate pattern="${DATE_FORMAT}" value="${changeOrder.orderDate}" /></td>
                            <td class="col-md-3"></td>
                        </tr>
                        <tr>
                            <td class="col-md-1">Start Date of Order</td>
                            <td class="col-md-2">
                                <div class="form-group">
                                    <div class="input-group date" id="datetimepicker1" >
                                        <input class="form-control" name="changeOrderStartDate" id="changeOrderStartDate" type="text" value="<fmt:formatDate pattern='${DATE_FORMAT}' value='${changeOrder.startDate}' />"  />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                    </div>
                                </div>
                                <script type="text/javascript">$(function () {
                                        $('#datetimepicker1').datetimepicker({
                                            locale: 'en-gb'
                                        });
                                    });
                                </script>
                            </td>
                            <td class="col-md-3">
                                <button class="btn btn-sm" type="button" onclick="toggleVisabilityAndDisabled('datetimepicker1')" <c:if test="${! allowChangeButtons}">disabled style="display:none"</c:if> >change</button>
                                </td>
                            </tr>
                            <tr>
                                <td class="col-md-1">End Date of Order</td>
                                <td class="col-md-2">
                                    <div class="form-group">
                                        <div class="input-group date" id="datetimepicker2" >
                                            <input class="form-control" name="changeOrderEndDate" id="changeOrderEndDate" type="text" value="<fmt:formatDate pattern='${DATE_FORMAT}' value='${changeOrder.endDate}' />"  />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                    </div>
                                </div>
                                <script type="text/javascript">$(function () {
                                        $('#datetimepicker2').datetimepicker({
                                            locale: 'en-gb'
                                        });
                                    });
                                </script>
                            </td>
                            <td class="col-md-3">
                                <button class="btn btn-sm" type="button" onclick="toggleVisabilityAndDisabled('datetimepicker2')" <c:if test="${! allowChangeButtons}">disabled style="display:none"</c:if> >change</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

            <c:if test="${allowChangeButtons}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="action" value="updateChangeRequest">
                <input type="hidden" name="changeRequestUUID" value="${orderChangeRequest.uuid}">
                <input type="hidden" name="orderUuid" value="${order.uuid}"/>
                <input type="hidden" name="changeOrderResourceAccess" value="${changeOrder.resourceAccess}"/>
                <button class="btn" type="submit" >Update Change Request</button>
            </form>

            <form action="./viewModifyOrder" method="POST">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="action" value="submitChangeRequest">
                <input type="hidden" name="changeRequestUUID" value="${orderChangeRequest.uuid}">
                <input type="hidden" name="orderUuid" value="${order.uuid}"/>
                <input type="hidden" name="changeOrderResourceAccess" value="${changeOrder.resourceAccess}"/>
                <button class="btn" type="submit" >Submit Change Request</button>
            </form>
        </c:if>
        <c:if test="${! allowChangeButtons}">
            <input  type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input  type="hidden" name="action" value="acceptChangeRequest">
            <input  type="hidden" name="changeRequestUUID" value="${orderChangeRequest.uuid}">
            <input  type="hidden" name="orderUuid" value="${order.uuid}"/>
            <button  class="btn" type="submit" >Accept Change Request</button>
            </form>
            <form action="./viewModifyOrder" method="POST">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="action" value="rejectChangeRequest">
                <input type="hidden" name="changeRequestUUID" value="${orderChangeRequest.uuid}">
                <input type="hidden" name="orderUuid" value="${order.uuid}"/>
                <button class="btn" type="submit" >Reject Change Request</button>
            </form>
        </c:if>

    </div>
</div>
</main>
<!--<script>
    function toggleVisabilityAndDisabled(elmt) {
        x = document.getElementById(elmt);
        if (x.getAttribute('disabled') === null) {
            x.disabled = true;
            x.style.display = 'none';
        } else {
            x.removeAttribute('disabled')
            x.style.display = 'block';
        }
    }
</script> -->
<script>
    function toggleVisabilityAndDisabled(elmt) {
        x = document.getElementById(elmt);
        if (x.style.display === "none") {
            x.removeAttribute('disabled');
            x.style.display = "block";
        } else {
            x.disabled = true;
            x.style.display = "none";
        }
    }
</script>
<jsp:include page="footer.jsp" />
