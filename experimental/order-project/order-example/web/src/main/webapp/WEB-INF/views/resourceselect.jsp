<%-- 
    Document   : resourceselect
    Created on : 12 Dec 2021, 23:38:45
    Author     : cgallen
    used to populate resource select modal with correct resources
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.util.List" %>
<%@ page import="org.solent.com504.project.model.resource.dto.Resource" %>
<%
    // using java in preferecne to tags because of matching on lists
    List<String> addResourcesList = (List) request.getAttribute("addResources");
    List<Resource> abstractResourceList = (List) request.getAttribute("abstractResourceList");

%>
<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Select Resources</title>
        <!-- Bootstrap core CSS -->
        <link href="./resources/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="./resources/css/navbar.css" rel="stylesheet">
        <script>
            function refreshParent() {
                window.opener.location.href = './viewModifyOrder?action=viewChangeRequestUUID&changeRequestUUID=${changeRequestUUID}';
            }

            function refreshParent2() {
                window.opener.location.reload(true);
            }
        </script>
    </head>
    <body onunload="javascript:refreshParent()">
        <!--<body>-->
        <main role="main" class="container">
            <div>
                <h1>Select Resources to update change request ${changeRequestUUID}</h1>
                <p>showing ${abstractResourceListSize} resources: </p>
                <form  action="./resourceselect" method="POST">
                    <table class="table">
                        <thead>
                            <tr>
                                <th scope="col">id</th>
                                <th scope="col">name</th>
                                <th scope="col">resourceTypeName</th>
                                <th scope="col">resourceController</th>
                                <th scope="col">uuid</th>
                                <th scope="col">href</th>
                                <th scope="col">resourceOwner</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>

                            <% for (Resource abstractResource : abstractResourceList) {%>

                            <tr>
                                <td><%=abstractResource.getId()%></td>
                                <td><%=abstractResource.getName()%></td>
                                <td><%=abstractResource.getResourceTypeName()%></td>
                                <td><%=abstractResource.getResourceController()%></td>
                                <td><%=abstractResource.getUuid()%></td>
                                <td><%=abstractResource.getHref()%></td>
                                <td></td>
                                <td>
                                    Add <input type="checkbox"  
                                               name="addResources" 
                                               value="<%=abstractResource.getUuid()%>"
                                               <%= addResourcesList.contains(abstractResource.getUuid()) ? "selected" : ""%> ">      
                                </td>
                            </tr>
                            <%
                                }
                            %>

                        </tbody>
                    </table>

                    <input  type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input  type="hidden" name="action" value="updateOrderChangeRequest"/>

                    <input  type="hidden" name="orderResourceAccess" value="${orderResourceAccess}" />
                    <input  type="hidden" name="ownerPartyUUID"" value="${ownerPartyUUID}" />
                    <input  type="hidden" name="changeRequestUUID"" value="${changeRequestUUID}" />
                    <button class="btn" type="submit" >Add Selected Resources</button>
                </form>
            </div><BR>
            <button class="btn" type="button" 
                    onclick="refreshParent(); window.open('', '_self', ''); window.close();">Close</button>

        </main>
    </body>
</html>
