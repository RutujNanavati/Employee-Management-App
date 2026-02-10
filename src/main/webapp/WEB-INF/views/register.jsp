<!DOCTYPE html>
<html>
<head>
<title>Employee Register</title>
<link rel="stylesheet" href="resources/style.css">
</head>
<body>

<div class="container">
    <center> <h2>Employee Registration</h2> </center>

<form action="${pageContext.request.contextPath}/register" method="post">

        <div class="input-group">
            <input type="text" name="firstName" placeholder="First Name" required>
        </div>

        <div class="input-group">
            <input type="text" name="lastName" placeholder="Last Name" required>
        </div>

        <div class="input-group">
            <input type="text" name="username" placeholder="Username" required>
        </div>

        <div class="input-group">
            <input type="password" name="password" placeholder="Password" required>
        </div>

        <div class="input-group">
            <input type="text" name="address" placeholder="Address">
        </div>

        <div class="input-group">
            <input type="text" name="contactNo" placeholder="Contact No">
        </div>

        <button type="submit">Register</button>
    </form>
    
    
</div>

</body>
</html>
