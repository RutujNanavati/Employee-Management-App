<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<div class="container-large">

<a href="${pageContext.request.contextPath}/employees/add" class="add-btn">
    + Add Employee
</a>


    <h2>Employee List</h2>

    <div class="table-wrapper">
        <table class="employee-table">

            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Username</th>
                    <th>Contact</th>
                    <th>Action</th>
                </tr>
            </thead>

            <tbody>
                <c:forEach var="emp" items="${employees}">
                    <tr>
                        <td>${emp.id}</td>
                        <td>${emp.firstName} ${emp.lastName}</td>
                        <td>${emp.username}</td>
                        <td>${emp.contactNo}</td>
                        <td>
                            <a class="edit-btn"
                               href="${pageContext.request.contextPath}/employees/edit/${emp.id}">
                                Edit
                            </a>

                            <a class="delete-btn"
                               href="${pageContext.request.contextPath}/employees/delete/${emp.id}">
                                Delete
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>

        </table>
    </div>

</div>
