<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<title>My Leave History</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<style>

.leave-container h2{
	margin-bottom:30px;
}
.leave-container {
    width: 85%;
    margin: 80px auto;
    background: white;
    padding: 25px;
    border-radius: 12px;
    box-shadow: 0 6px 18px rgba(0,0,0,0.08);
}

.leave-table {
    width: 100%;
    border-collapse: collapse;
}

.leave-table th {
    background: #61db57;
    color: white;
    padding: 12px;
    text-align: center;
}

.leave-table td {
    padding: 12px;
    text-align: center;
    border-top: 1px solid #eee;
}

.status-pending { color: orange; font-weight: 600; }
.status-approved { color: green; font-weight: 600; }
.status-rejected { color: red; font-weight: 600; }
.status-cancelled { color: gray; font-weight: 600; }

.cancel-btn {
    padding: 5px 10px;
    background: #e53935;
    color: white;
    border-radius: 6px;
    text-decoration: none;
    font-size: 13px;
}

.cancel-btn:hover {
    background: #c62828;
}

</style>
</head>

<body>

<div class="leave-container">

<h2 style="text-align:center;">My Leave History</h2>

<table class="leave-table">
<tr>
    <th>ID</th>
    <th>From</th>
    <th>To</th>
    <th>Reason</th>
    <th>Status</th>
    <th>Applied On</th>
    <th>Action</th>
</tr>

<c:forEach var="leave" items="${leaves}">
<tr>

    <td>${leave.id}</td>
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
                <span class="status-cancelled">🚫 CANCELLED</span>
            </c:when>
        </c:choose>
    </td>

    <td>${leave.appliedOn}</td>

    <td>
        <c:if test="${leave.status == 'pending'}">
            <a class="cancel-btn"
               href="${pageContext.request.contextPath}/leaves/cancel/${leave.id}">
               Cancel
            </a>
        </c:if>
    </td>

</tr>
</c:forEach>

</table>

</div>

</body>
</html>