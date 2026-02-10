<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<div class="container">

<c:choose>
    <c:when test="${mode eq 'edit'}">
       <center>  <h2>Edit Employee</h2> </center>
        <c:set var="action" value="update"/>
        <c:set var="btn" value="Update Employee"/>
    </c:when>
    <c:otherwise>
        <center>  <h2>Add Employee</h2> </center>
        <c:set var="action" value="save"/>
        <c:set var="btn" value="Add Employee"/>
    </c:otherwise>
</c:choose>

<form action="${pageContext.request.contextPath}/employees/${action}" method="post">

    <input type="hidden" name="id" value="${id}" />

    <div class="input-group">
        <input type="text" name="firstName" value="${firstName}" placeholder="First Name" required>
    </div>

    <div class="input-group">
        <input type="text" name="lastName" value="${lastName}" placeholder="Last Name" required>
    </div>

    <div class="input-group">
        <input type="text" name="username" value="${username}" placeholder="Username" required>
    </div>

    <div class="input-group">
        <input type="text" name="address" value="${address}" placeholder="Address" required>
    </div>

    <div class="input-group">
        <input type="text" name="contactNo" value="${contactNo}" placeholder="Contact No" required>
    </div>

    <button type="submit">${btn}</button>

</form>
</div>
