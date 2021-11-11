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


    <div class="col-xs-6">
        <h2 class="sub-header">Subtitle</h2>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th class="col-md-1">#</th>
                        <th class="col-md-2">Header</th>
                        <th class="col-md-3">Header</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="col-md-1">1,001</td>
                        <td class="col-md-2">1,001</td>
                        <td class="col-md-3">1,001</td>
                    </tr>
                    <tr>
                        <td class="col-md-1">1,001</td>
                        <td class="col-md-2">1,001</td>
                        <td class="col-md-3">1,001</td>
                    </tr>
                    <tr>
                        <td class="col-md-1">1,001</td>
                        <td class="col-md-2">1,001</td>
                        <td class="col-md-3">1,001</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="col-xs-6">
        <h2 class="sub-header">Latest Incidents</h2>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th class="col-md-1">#</th>
                        <th class="col-md-2">Header</th>
                        <th class="col-md-3">Header</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="col-md-1">1,001</td>
                        <td class="col-md-2">1,001</td>
                        <td class="col-md-3">1,001</td>
                    </tr>
                    <tr>
                        <td class="col-md-1">1,001</td>
                        <td class="col-md-2">1,001</td>
                        <td class="col-md-3">1,001</td>
                    </tr>
                    <tr>
                        <td class="col-md-1">1,001</td>
                        <td class="col-md-2">1,001</td>
                        <td class="col-md-3">1,001</td>
                    </tr>
                </tbody>
            </table>
        </div>
</main>


<jsp:include page="footer.jsp" />
