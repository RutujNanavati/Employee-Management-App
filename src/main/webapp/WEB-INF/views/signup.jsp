<%@ page contentType="text/html;charset=UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
<title>Sign Up</title>
<div class="container">

    <center><h2>Signup</h2></center>

    <form action="${pageContext.request.contextPath}/signup" method="post">

        <div class="input-group">
            <input type="text" name="username" placeholder="Username" required>
        </div>

        <div class="input-group">
            <input type="password" name="password" placeholder="Password" required>
        </div>

        <button type="submit">Signup</button>

    </form>

    <p style="text-align:center;margin-top:10px;">
        <a href="${pageContext.request.contextPath}/login">Already have account? Login</a>
    </p>

</div>
