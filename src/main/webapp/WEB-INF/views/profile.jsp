<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>Employee Profile</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<style>

/* Background */
body {
    background: linear-gradient(135deg, lightgreen, green, #61db57);
    font-family: 'Segoe UI', sans-serif;
    margin: 0;
    padding: 0;
}

/* Layout */
.profile-wrapper {
    width: 900px;
    margin: 80px auto;
    display: flex;
    gap: 30px;
}

/* Left Card */
.profile-left {
    width: 300px;
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(0,0,0,0.08);
    text-align: center;
    padding: 30px;
}

.avatar-wrapper {
    position: relative;
    width: 110px;
    height: 110px;
    margin: 0 auto 20px auto;
}

.profile-img,
.avatar-circle {
    width: 110px;
    height: 110px;
    box-shadow: 0 4px 10px rgba(0,0,0,1);
    border-radius: 50%;
    object-fit: cover;
    transition: 1s;
}
.profile-img:hover,.avtar-circle:hover{
transform: scale(1.25);
display:.profile-img;}

.camera-icon {
display:inline-block;
    position: absolute;
    bottom: 5px;
    right: 5px;
    background: ;
    color: white;
    font-size: 14px;
    padding: 3px;
    margin-bottom:1px;
    border-radius: 50%;
    cursor: pointer;
    box-shadow: 0 4px 10px rgba(0,0,0,0.3);
    transition: 1s;
}

.camera-icon:hover {
    background: ;
    transform: scale(1.35);
    box-shadow: 0 4px 10px rgba(0,0,0,1);

}


/* Avatar (for letter fallback) */
.avatar-circle {
    width: 110px;
    height: 110px;
    border-radius: 50%;
    background: #4CAF50;
    color: #fff;
    font-size: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 20px auto;
}

/* Profile Image */
.profile-img {
    width: 110px;
    height: 110px;
    border-radius: 50%;
    object-fit: cover;
    margin: 0 auto 20px auto;
    display: block;
}

/* Name */
.profile-name {
    font-size: 20px;
    font-weight: 600;
}

.profile-username {
    color: #777;
    margin: 8px 0 15px 0;
}

/* Role Badge */
.role-badge {
    display: inline-block;
    padding: 6px 14px;
    background: #e8f5e9;
    color: #2e7d32;
    font-size: 12px;
    border-radius: 20px;
}

/* Right Card */
.profile-right {
    flex: 1;
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(0,0,0,0.08);
    padding: 30px;
}

/* Info Rows */
.info-row {
    display: flex;
    justify-content: space-between;
    padding: 14px 0;
    border-bottom: 1px solid #eee;
}

.info-row:last-child {
    border-bottom: none;
}

.info-label {
    font-weight: 600;
    color: #555;
}

.info-value {
    color: #333;
    text-align: right;
}

/* Button */
.profile-actions {
    margin-top: 25px;
    text-align: right;
}

.back-btn {
    padding: 8px 18px;
    background: #4CAF50;
    color: white;
    border-radius: 6px;
    text-decoration: none;
    transition: 0.2s ease;
}

.back-btn:hover {
    background: #43a047;
}

</style>
</head>

<body>

<div class="profile-wrapper">

    <!-- LEFT SIDE -->
    <div class="profile-left">

      <form action="${pageContext.request.contextPath}/employees/changePhoto"
      method="post"
      enctype="multipart/form-data"
      id="photoForm">

    <input type="hidden" name="id" value="${id}">

    <div class="avatar-wrapper">

        <c:choose>
            <c:when test="${not empty photo}">
                <img src="${pageContext.request.contextPath}/employee_uploads/${photo}"
                     class="profile-img">
            </c:when>
            <c:otherwise>
                <div class="avatar-circle">
                    ${firstName.substring(0,1)}
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Hidden File -->
        <input type="file"
               name="photoFile"
               id="photoInput"
               accept="image/*"
               style="display:none"
               onchange="document.getElementById('photoForm').submit();">

        <!-- Camera Icon -->
        <div class="camera-icon"
             onclick="document.getElementById('photoInput').click();">
            📷
        </div>

    </div>
</form>


        <div class="profile-name">
            ${firstName} ${lastName}
        </div>

        <div class="profile-username">
            @${username}
        </div>

        <div class="role-badge">
            ${profileRole}
        </div>

    </div>

    <!-- RIGHT SIDE -->
    <div class="profile-right">

        <div class="info-row">
            <div class="info-label">Employee ID</div>
            <div class="info-value">${id}</div>
        </div>

        <div class="info-row">
            <div class="info-label">Gender</div>
            <div class="info-value">${gender}</div>
        </div>

        <div class="info-row">
            <div class="info-label">Contact</div>
            <div class="info-value">${contactNo}</div>
        </div>

        <div class="info-row">
            <div class="info-label">Address</div>
            <div class="info-value">${address}</div>
        </div>

        <div class="profile-actions">
            <a href="${pageContext.request.contextPath}/employees" class="back-btn">
                Back
            </a>
        </div>

    </div>

</div>

</body>
</html>
