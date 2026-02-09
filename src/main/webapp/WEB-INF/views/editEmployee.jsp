<%@ page contentType="text/html;charset=UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<div class="container">

    <h2>Edit Employee</h2>

    <form action="${pageContext.request.contextPath}/employees/update" method="post">

        <input type="hidden" name="id" value="${id}" />

        <div class="input-group">
            <input type="text" name="firstName" value="${firstName}" placeholder="First Name" />
        </div>

        <div class="input-group">
            <input type="text" name="lastName" value="${lastName}" placeholder="Last Name" />
        </div>

        <div class="input-group">
            <input type="text" name="username" value="${username}" placeholder="Username" />
        </div>

        <div class="input-group">
            <input type="text" name="address" value="${address}" placeholder="Address" />
        </div>

        <div class="input-group">
            <input type="text" name="contactNo" value="${contactNo}" placeholder="Contact No" />
        </div>

        <button type="submit">Update Employee</button>

    </form>

</div>
