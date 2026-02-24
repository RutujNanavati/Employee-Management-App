<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Change Password</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<style>
.change-box {
    width: 400px;
    margin: 80px auto;
    padding: 30px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 8px 20px rgba(0,0,0,0.1);
}

.change-box h2 {
    text-align: center;
    margin-bottom: 20px;
}

.change-box input {
    width: 100%;
    padding: 10px;
    margin: 10px 0;
    border-radius: 5px;
    border: 1px solid #ccc;
}

.change-box button {
    width: 100%;
    padding: 10px;
    background: #4CAF50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

.change-box button:hover {
    background: #43a047;
}
</style>
</head>

<body>

<div class="change-box">
    <h2>Change Password</h2>

    <form method="post" action="${pageContext.request.contextPath}/change-password">
        <input type="password" name="oldPassword" placeholder="Old Password" required>
        <input type="password" name="newPassword" placeholder="New Password" required>
        <button type="submit">Update Password</button>
    </form>

    <p style="color:red">${error}</p>
    <p style="color:green">${success}</p>

</div>

</body>
</html>