<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<title>Employee List</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<!-- NAV BAR -->

<div class="navbar">
    <div class="nav-left">
        Employee Management App
    </div>

   <div class="nav-right">
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/employees">Employees</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
</div>
</div>

<!-- search class -->
<div class="container-large">

<div class="top-controls">

       <!-- TITLE + ADD BUTTON ROW -->
    <div class="header-row">
        <center><h2>Employee List</h2></center>

        <a href="${pageContext.request.contextPath}/employees/add"
           class="add-btn">
            + Add Employee
        </a>
    </div>

    <!-- SEARCH + FILTER ROW -->
    <div class="controls-row">
    <form method="get"
          action="${pageContext.request.contextPath}/employees">

        <!-- Search Input -->
        <input type="text"
               name="keyword"
               value="${keyword}"
               placeholder="Search..."
               class="search-input"/>

        <!-- Search Button -->
        <button type="submit" class="search-btn">Search</button>

        <!-- Page Size Dropdown -->
        <select name="size"
                class="dropdown-size"
                onchange="this.form.submit()">

            <option value="5" ${size==5?'selected':''}>5</option>
            <option value="10" ${size==10?'selected':''}>10</option>
            <option value="20" ${size==20?'selected':''}>20</option>
        </select>

        <!-- Sort Dropdown -->
        <select name="sort"
                class="dropdown-filter"
                onchange="this.form.submit()">

            <option value="id" ${sort=='id'?'selected':''}>By ID</option>
            <option value="firstName" ${sort=='firstName'?'selected':''}>Name</option>
            <option value="username" ${sort=='username'?'selected':''}>Username</option>
            <option value="latest" ${sort=='latest'?'selected':''}>Latest</option>
        </select>
        

        <!-- Reset -->
        <button type="button"
        class="reset-btn"
        onclick="window.location.href='${pageContext.request.contextPath}/employees'">
    Reset
</button>


    </form>
</div>
</div>

<!-- Employee Table -->
 
<table class="employee-table">
<tr>
    <th>ID</th>
    <th>Name</th>
    <th>Username</th>
    <th class="gender-header">
    	<div class="gender-filter">
        <span onclick="toggleGenderDropdown()" class="gender-title">
            Gender▼
        </span>
        	<div id="genderDropdown" class="gender-dropdown">
            <a href="${pageContext.request.contextPath}/employees?page=1&size=${size}&sort=${sort}&keyword=${keyword}&gender=">All
</a>
<a href="${pageContext.request.contextPath}/employees?page=1&size=${size}&sort=${sort}&keyword=${keyword}&gender=Male">
    Male👨
</a>
<a href="${pageContext.request.contextPath}/employees?page=1&size=${size}&sort=${sort}&keyword=${keyword}&gender=Female">
    Female👩
</a>
<a href="${pageContext.request.contextPath}/employees?page=1&size=${size}&sort=${sort}&keyword=${keyword}&gender=Other">
    Other ⚧
</a>
	        </div>
	    </div>
	</th>
    <th>Contact</th>
<th>Action</th>

</tr>

<c:forEach var="emp" items="${employees}">
<tr>
    <td>${emp.id}</td>
    <td>${emp.firstName} ${emp.lastName}</td>
    <td>${emp.username}</td>
    <td>
    <c:choose>
        <c:when test="${emp.gender == 'Male'}">👨 Male</c:when>
        <c:when test="${emp.gender == 'Female'}">👩 Female</c:when>
        <c:when test="${emp.gender == 'Other' }">⚧ Other</c:when>
    </c:choose>
	</td>

    <td>${emp.contactNo}</td>
    <td class="action-cell">

    <!-- 👁 View Button -->
    <a class="view-btn"
       href="${pageContext.request.contextPath}/employees/profile/${emp.id}">
        👁️
    </a>

    <!-- Edit -->
    <c:if test="${sessionScope.role == 'ADMIN' || sessionScope.role == 'HR'}">
        <a class="edit-btn"
           href="${pageContext.request.contextPath}/employees/edit/${emp.id}">
            Edit
        </a>
    </c:if>

    <!-- Delete -->
    <c:if test="${sessionScope.role == 'ADMIN'}">
        <a class="delete-btn"
           href="#"
           onclick="openDeleteModal('${pageContext.request.contextPath}/employees/delete/${emp.id}')">
            Delete
        </a>
    </c:if>

</td>

</tr>
</c:forEach>

</table>
</div>

<!-- DELETE MODAL -->
<div id="deleteModal" class="modal-overlay">
    <div class="modal-box">
        <h3>⚠️ Confirm Deletion</h3>
        <p>This action cannot be undone.<br>Do you really want to delete this employee?</p>

        <div class="modal-buttons">
            <button class="cancel-btn" onclick="closeDeleteModal()">Cancel</button>
            <a id="confirmDeleteBtn" class="confirm-btn">Delete</a>
        </div>
    </div>
</div>


<!-- PAGINATION -->

<div class="pagination">

    <c:if test="${currentPage > 1}">
        <a href="${pageContext.request.contextPath}/employees?page=${currentPage-1}&size=${size}&sort=${sort}&keyword=${keyword}&gender=${selectedGender}"
           class="page-btn">
            Previous
        </a>
    </c:if>

    <c:forEach begin="1" end="${totalPages}" var="i">
        <a href="${pageContext.request.contextPath}/employees?page=${i}&size=${size}&sort=${sort}&keyword=${keyword}&gender=${selectedGender}"
           class="page-btn ${i == currentPage ? 'active-page' : ''}">
            ${i}
        </a>
    </c:forEach>

    <c:if test="${currentPage < totalPages}">
        <a href="${pageContext.request.contextPath}/employees?page=${currentPage+1}&size=${size}&sort=${sort}&&keyword=${keyword}&gender=${selectedGender}"
           class="page-btn">
            Next
        </a>
        
    </c:if>

</div>

<script>
function toggleGenderDropdown() {
    var dropdown = document.getElementById("genderDropdown");
    dropdown.style.display =
        dropdown.style.display === "block" ? "none" : "block";
}

window.onclick = function(e) {
    if (!e.target.matches('.gender-title')) {
        document.getElementById("genderDropdown").style.display = "none";
    }
}


//Delete Function//     

let deleteUrl = "";

function openDeleteModal(url) {
    deleteUrl = url;
    document.getElementById("confirmDeleteBtn").href = deleteUrl;
    document.getElementById("deleteModal").style.display = "flex";
}

function closeDeleteModal() {
    document.getElementById("deleteModal").style.display = "none";
}

window.onclick = function(e) {
    const modal = document.getElementById("deleteModal");
    if (e.target === modal) {
        closeDeleteModal();
    }
}


</script>



