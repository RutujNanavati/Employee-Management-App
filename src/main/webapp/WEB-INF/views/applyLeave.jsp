<%@ page contentType="text/html;charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<div class="container">
    <center><h2>Apply Leave</h2></center>

    <form action="${pageContext.request.contextPath}/employees/apply-leave" method="post">

        <div class="input-group">
            <label>From Date</label>
            <input type="date" name="fromDate" required>
        </div>

        <div class="input-group">
            <label>To Date</label>
            <input type="date" name="toDate" required>
        </div>

        <div class="input-group">
            <label>Reason</label>
            <textarea name="reason" rows="3" required></textarea>
        </div>

        <button type="submit">Submit Leave</button>
    </form>
</div>