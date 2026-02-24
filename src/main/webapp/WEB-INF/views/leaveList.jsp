<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<title>Leave Requests</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<style>

.leave-container {
    width: 95%;
    margin: 50px auto;
    background: #ffffff;
    padding: 25px;
    border-radius: 12px;
    box-shadow: 0 6px 18px rgba(0,0,0,0.08);
}
.leave-container h2{
	margin-bottom:25px;
}

.leave-table {
    width: 100%;
    border-collapse: collapse;
}

.leave-table th {
    background: #f4f6f9;
    padding: 12px;
    text-align: center;
    font-weight: 600;
}

.leave-table td {
    padding: 12px;
    text-align: center;
    border-top: 1px solid #eee;
}

.status-pending {
    color: orange;
    font-weight: 600;
}

.status-approved {
    color: green;
    font-weight: 600;
}

.status-rejected {
    color: red;
    font-weight: 600;
}
.status-cancelled { 
	color: gray; 
	font-weight: 600; 
}


.approve-btn {
    padding: 5px 8px;
    background: #4CAF50;
    color: white;
    border-radius: 6px;
    margin-right:10px;
    text-decoration: none;
    font-size: 13px;
}

.reject-btn {
    padding: 5px 10px;
    background: #e53935;
    color: white;
    border-radius: 6px;
    text-decoration: none;
    font-size: 13px;
}

.approve-btn:hover {
    background: #43a047;
}

.reject-btn:hover {
    background: #c62828;
}

</style>
</head>

<body>
<div class="navbar">
    <div class="nav-left">
        Employee Management App
    </div>

   <div class="nav-right">
      	<a href="${pageContext.request.contextPath}/leaves" class="nav">Leave Applications</a>
        <a href="${pageContext.request.contextPath}/dashboard" class="nav">Dashboard</a>
        <a href="${pageContext.request.contextPath}/employees" class="nav">Employees</a>
        <div class="profile-menu">

    <div class="profile-circle" onclick="toggleProfileMenu()"
         title="${sessionScope.user} (${sessionScope.role})">

        <c:choose>
            <c:when test="${not empty sessionScope.photo}">
                <img src="${pageContext.request.contextPath}/employee_uploads/${sessionScope.photo}"
                     class="nav-profile-img">
            </c:when>
            <c:otherwise>
                ${sessionScope.user.substring(0,1)}
            </c:otherwise>
        </c:choose>

    </div>

    <div id="profileDropdown" class="profile-dropdown">
        <div class="profile-info">
            <strong>${sessionScope.user}</strong><br>
            <small>${sessionScope.role}</small>
        </div>

        <a href="${pageContext.request.contextPath}/employees/profile/${sessionScope.employeeId}">
            Edit Profile
        </a>

        <a href="${pageContext.request.contextPath}/change-password">
            Change Password
        </a>

        <a href="${pageContext.request.contextPath}/logout" class="logout-link">
            Logout ⏻
        </a>
    </div>

</div>

</div>
</div>

<div class="leave-container">

    <h2 style="text-align:center;">Leave Requests</h2>

    <table class="leave-table">
        <tr>
            <th>Leave ID</th>
            <th>Emp ID</th>
            <th>Employee</th>
            <th>From</th>
            <th>To</th>
            <th>Reason</th>
            <th>Status</th>
            <th>Action</th>
        </tr>

        <c:forEach var="leave" items="${leaves}">
            <tr>
                <td>${leave.id}</td>
                <td>${leave.employeeid}</td>
                <td>${leave.employeeName}</td>
                <td>${leave.fromDate}</td>
                <td>${leave.toDate}</td>
                <td>${leave.reason}</td>

                <td>
                    <c:choose>
                        <c:when test="${leave.status == 'pending'}">
                            <span class="status-pending">⏳ PENDING</span>
                        </c:when>
                        <c:when test="${leave.status == 'APPROVED'}">
                            <span class="status-approved">✅ APPROVED</span>
                        </c:when>
                        <c:when test="${leave.status == 'REJECTED'}">
                            <span class="status-rejected">❌ REJECTED</span>
                        </c:when>
                        <c:when test="${leave.status == 'CANCELLED'}">
                        	<span class="status-cancelled">CANCELLED </span>
                         </c:when>
                        <c:otherwise>
                            <span class="status-pending">⏳ PENDING</span>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <c:if test="${leave.status == 'pending'}">
                        <a class="approve-btn"
                           href="${pageContext.request.contextPath}/leaves/approve/${leave.id}">
                           Approve
                        </a>

                        <a class="reject-btn"
                           href="${pageContext.request.contextPath}/leaves/reject/${leave.id}">
                           Reject
                        </a>
                    </c:if>

                    <c:if test="${leave.status != 'pending'}">
                        -
                    </c:if>
                </td>

            </tr>
        </c:forEach>

    </table>

</div>

<script>

function toggleProfileMenu() {
    const dropdown = document.getElementById("profileDropdown");
    dropdown.style.display =
        dropdown.style.display === "block" ? "none" : "block";
}

window.addEventListener("click", function(e) {
    if (!e.target.closest(".profile-menu")) {
        document.getElementById("profileDropdown").style.display = "none";
    }
});
</script>

</body>
</html>