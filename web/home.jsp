<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Dashboard</title>
        <link href="static/styles.css" rel="stylesheet">
    </head>
    <body>
        <c:set var="auth" value="${sessionScope.Auth}" />
        <jsp:include page="includes/header.jsp" />
        <c:if test="${auth.getRole() == 'student'}">
            <jsp:include page="includes/homeStudent.jsp" />
        </c:if>
    </body>
</html>
