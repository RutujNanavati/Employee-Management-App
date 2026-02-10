<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<div class="page-content">

    <div class="container">

        <c:choose>
            <c:when test="${mode eq 'add'}">
                <h2>Add Employee</h2>
            </c:when>
            <c:otherwise>
                <h2>Edit Employee</h2>
            </c:otherwise>
        </c:choose>

        <form action="${pageContext.request.contextPath}/employees/${mode eq 'edit' ? 'update' : 'save'}"
              method="post">

            <input type="hidden" name="id" value="${id}" />

            <div class="input-group">
                <input type="text" name="firstName" value="${firstName}" placeholder="First Name" required />
            </div>

            <div class="input-group">
                <input type="text" name="lastName" value="${lastName}" placeholder="Last Name" required />
            </div>

            <div class="input-group">
                <input type="text" name="username" value="${username}" placeholder="Username" required />
            </div>

            <div class="input-group">
                <input type="text" name="address" value="${address}" placeholder="Address" required />
            </div>

            <div class="input-group">
                <input type="text" name="contactNo" value="${contactNo}" placeholder="Contact No" required />
            </div>

            <c:choose>
                <c:when test="${mode eq 'edit'}">
                    <button type="submit">Update Employee</button>
                </c:when>
                <c:otherwise>
                    <button type="submit">Add Employee</button>
                </c:otherwise>
            </c:choose>

        </form>

    </div>

</div>
