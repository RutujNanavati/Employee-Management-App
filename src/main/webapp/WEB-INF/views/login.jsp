<%@ page contentType="text/html;charset=UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
<title>Login</title>
<div class="container">

    <center> <h2> Admin Login </h2> </center>
    
    <p style="text-align:center;margin-top:10px;">
    <a href="${pageContext.request.contextPath}/signup">Create Account</a>
</p>
    

    <form action="${pageContext.request.contextPath}/login" method="post">

        <div class="input-group">
            <input type="text" name="username" placeholder="Username" required>
        </div>

        <div class="input-group">
            <input type="password" name="password" placeholder="Password" required>
        </div>

        <button type="submit">Login</button>

        <p style="color:red; text-align:center;">
            ${error}
        </p>

    </form>

</div>
