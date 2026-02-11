<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<title>Employee List</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<!-- NAV BAR -->

<div class="navbar">
    <div class="nav-left">
        Employee Management App
    </div>

   <div class="nav-right">
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/employees">Employees</a>
        <b href="${pageContext.request.contextPath}/logout">Logout</b>
</div>
</div>

<!-- search class -->
<div class="container-large">

<div class="search-container">
    <form method="get"
          action="${pageContext.request.contextPath}/employees">

        <input type="text"
               name="keyword"
               value="${keyword}"
               placeholder="Search by name, username or contact..."
               class="search-input"/>

        <button type="submit" class="search-btn">
            Search
        </button>

        <button type="button"
                class="reset-btn"
                onclick="window.location.href='${pageContext.request.contextPath}/employees'">
            Reset
        </button>

    </form>
    
</div>

<!-- Employee Table -->

<div class="table-header">
    <h2 class="page-title">Employee List</h2>

    <a href="${pageContext.request.contextPath}/employees/add"
       class="add-btn">
        + Add Employee
    </a>
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
        <a class="delete-btn"
   href="${pageContext.request.contextPath}/employees/delete/${emp.id}"
   onclick="return confirm('⚠️ This action cannot be undone. Do you really want to delete Employee?');">
   Delete
</a>

    </td>
</tr>
</c:forEach>

</table>
</div>

<!-- PAGINATION -->

<div class="pagination">

    <c:if test="${currentPage > 1}">
        <a href="${pageContext.request.contextPath}/employees?page=${currentPage - 1}&keyword=${keyword}" 
           class="page-btn">
           Previous
        </a>
    </c:if>

    <c:forEach begin="1" end="${totalPages}" var="i">
        <a href="${pageContext.request.contextPath}/employees?page=${i}&keyword=${keyword}"
           class="page-btn ${i == currentPage ? 'active-page' : ''}">
           ${i}
        </a>
    </c:forEach>

    <c:if test="${currentPage < totalPages}">
        <a href="${pageContext.request.contextPath}/employees?page=${currentPage + 1}&keyword=${keyword}" 
           class="page-btn">
           Next
        </a>
    </c:if>

</div>

