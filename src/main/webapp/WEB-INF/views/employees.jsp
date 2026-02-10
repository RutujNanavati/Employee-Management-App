<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<title>Employee List</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">


<div class="container-large">

<center> <h1>Employee List</h1> </center>

<div class="add-box">
    <a href="${pageContext.request.contextPath}/employees/add">+ Add Employee</a>
</div>

<table class="employee-table">
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Username</th>
    <th>Contact</th>
    <th>Action</th>
</tr>

<c:forEach var="emp" items="${employees}">
<tr>
    <td>${emp.id}</td>
    <td>${emp.firstName} ${emp.lastName}</td>
    <td>${emp.username}</td>
    <td>${emp.contactNo}</td>
    <td>
        <a class="edit-btn" href="${pageContext.request.contextPath}/employees/edit/${emp.id}">Edit</a>
        <a class="delete-btn" href="${pageContext.request.contextPath}/employees/delete/${emp.id}">Delete</a>
    </td>
</tr>
</c:forEach>

</table>
</div>
