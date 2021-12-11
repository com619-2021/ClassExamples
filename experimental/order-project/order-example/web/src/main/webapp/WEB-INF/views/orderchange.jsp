<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- start of orderchange.jsp selectedPage=${selectedPage}-->
<jsp:include page="header.jsp" />

<!-- Begin page content -->
<main role="main" class="container">
    <div>
        <h1>Order Changes</h1>
        <p>showing ${orderChangeRequestListSize} order changes: </p>
        <table class="table">
            <thead>
                <tr>
                    <th scope="col">Change Id</th>
                    <th scope="col">Change uuid</th>
                    <th scope="col">Change Href</th>
                    <th scope="col">Request Date</th>
                    <th scope="col">Change Status</th>
                    <th scope="col">Order Name</th>
                    <th scope="col">Order Status</th>
                    <th scope="col">Order UUID</th>
                    <th scope="col">Order Href</th>
                    <th scope="col">Order Access</th>
                    <th scope="col">Change Reason</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="orderChangeRequest" items="${orderChangeRequestList}">
                    <tr>
                        <td>${orderChangeRequest.id}</td>
                        <td>${orderChangeRequest.uuid}</td>
                        <td><a href=".${orderChangeRequest.href}" target="_blank" >${orderChangeRequest.href}</a></td>
                        <td>${orderChangeRequest.requestDate}</td>
                        <td>${orderChangeRequest.status}</td>
                        <td>${orderChangeRequest.changeRequest.name}</td>
                        <td>${orderChangeRequest.changeRequest.status}</td>
                        <td>${orderChangeRequest.changeRequest.uuid}</td>
                        <td><a href=".${orderChangeRequest.changeRequest.href}"target="_blank" >${orderChangeRequest.changeRequest.href}</a></td>
                        <td>${orderChangeRequest.changeRequest.resourceAccess}</td>
                        <td>${orderChangeRequest.changeReason}</td>
                        <td>
                            <form action="./viewModifyOrder" method="GET">
                                <input type="hidden" name="changeRequestUUID" value="${orderChangeRequest.uuid}">
                                <input type="hidden" name="action" value="viewChangeRequestUUID">
                                <button class="btn" type="submit" >View Change Request</button>
                            </form> 
                        </td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>

</main>


<jsp:include page="footer.jsp" />
