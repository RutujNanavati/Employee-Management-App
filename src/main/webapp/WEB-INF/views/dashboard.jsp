<%@ page contentType="text/html;charset=UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
<title> Admin Dashboard </title>
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

<div class="container">

    <center><h2>Welcome, ${username}</h2></center>

    <p style="text-align:center; margin:15px 0; font-size:18px;">
        Total Employees: <strong>${totalEmployees}</strong>
    </p>

    <div style="margin-top:20px; text-align:center;">

        <a class="dashboard-btn"
           href="${pageContext.request.contextPath}/employees">
           View Employees
        </a>

        <a class="dashboard-btn"
           href="${pageContext.request.contextPath}/employees/add">
           Add Employee
        </a>

        <a class="logout-btn"
           href="${pageContext.request.contextPath}/logout">
           Logout
           
        </a>

    </div>

</div>
