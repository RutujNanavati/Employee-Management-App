<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">

<div class="container">

<c:choose>
    <c:when test="${mode eq 'edit'}">
        <center><h2>Edit Employee</h2></center>
        <c:set var="action" value="update"/>
        <c:set var="btn" value="Update Employee"/>
    </c:when>
    <c:otherwise>
        <center><h2>Add Employee</h2></center>
        <c:set var="action" value="save"/>
        <c:set var="btn" value="Add Employee"/>
    </c:otherwise>
	</c:choose>

<form action="${pageContext.request.contextPath}/employees/${action}"
      method="post"
      enctype="multipart/form-data">

    <input type="hidden" name="id" value="${id}">


    <div class="input-group">
        <input type="text" name="firstName" value="${firstName}" placeholder="First Name" required>
    </div>

    <div class="input-group">
        <input type="text" name="lastName" value="${lastName}" placeholder="Last Name" required>
    </div>

    <div class="input-group">
        <input type="text" name="username" value="${username}" placeholder="Username" required>
    </div>
    
    <c:if test="${mode == 'add'}">
    <div class="input-group">
        <input type="password" name="password" placeholder="Password" required>
    </div>
	</c:if>
    
    
<div class="input-group">
    <div class="input-group gender-row">

        <label class="gender-label">Gender:</label>

        <div class="gender-options">
			<label>
			    <input type="radio" name="gender" value="Male"
			    ${gender != null && gender.equals("Male") ? "checked" : ""}>
			    👨Male
			</label>
			
			<label>
			    <input type="radio" name="gender" value="Female"
			    ${gender != null && gender.equals("Female") ? "checked" : ""}>
			    👩Female
			</label>
			
			<label>
			    <input type="radio" name="gender" value="Other"
			    ${gender != null && gender.equals("Other") ? "checked" : ""}>
			    ⚧ Other
			</label>
        </div>
    </div>
</div>

    <div class="input-group">
    	<input type="file" name="photoFile" accept="image/*">
	</div>
    <div class="input-group">
        <input type="text" name="address" value="${address}" placeholder="Address" required>
    </div>

    <div class="input-group">
        <input type="text" name="contactNo" value="${contactNo}" placeholder="Contact No" required>
    </div>

    <!-- COUNTRY -->
    <div class="input-group">
        <select id="country" name="countryId">
            <option value="">Select Country</option>
        </select>
    </div>

    <!-- STATE -->
    <div class="input-group">
        <select id="state" name="stateId">
            <option value="">Select State</option>
        </select>
    </div>

    <!-- CITY -->
    <div class="input-group">
        <select id="city" name="cityId">
            <option value="">Select City</option>
        </select>
    </div>

    <button type="submit">${btn}</button>
</form>
</div>

<script>

let allCities = [];
let selectedCountry = "${selectedCountry}";
let selectedState = "${selectedState}";
let selectedCity = "${selectedCity}";

document.addEventListener("DOMContentLoaded", async function(){

    await loadAllCities();
    loadCountries();

    document.getElementById("country").addEventListener("change", function(){
        selectedState = null;
        selectedCity = null;
        loadStates(this.value);
        resetCity();
    });

    document.getElementById("state").addEventListener("change", function(){
        selectedCity = null;
        loadCities(this.value);
    });

});

async function loadAllCities(){
    let res = await fetch("/EmployeeMavenProject/location/allCities");
    allCities = await res.json();
}

function loadCountries(){
    fetch("/EmployeeMavenProject/location/countries")
    .then(res => res.json())
    .then(data => {

        let dropdown = document.getElementById("country");
        dropdown.innerHTML = "<option>Select Country</option>";

        data.forEach(c=>{
            let option = document.createElement("option");
            option.value = c.id;
            option.text = c.name;

            if(String(c.id) === String(selectedCountry)){
                option.selected = true;
                loadStates(c.id);
            }

            dropdown.appendChild(option);
        });

    });
}

function loadStates(countryId){

    if(!countryId){
        document.getElementById("state").innerHTML = "<option>Select State</option>";
        resetCity();
        return;
    }

    fetch("/EmployeeMavenProject/location/states/" + countryId)
    .then(res => res.json())
    .then(data => {

        let stateDropdown = document.getElementById("state");
        stateDropdown.innerHTML = "<option>Select State</option>";

        data.forEach(s=>{
            let option = document.createElement("option");
            option.value = s.id;
            option.text = s.name;

            if(String(s.id) === String(selectedState)){
                option.selected = true;
                loadCities(s.id);
            }

            stateDropdown.appendChild(option);
        });

    });
}

function loadCities(stateId){

    let cityDropdown = document.getElementById("city");
    cityDropdown.innerHTML = "<option>Select City</option>";

    if(!stateId) return;

    let filtered = allCities.filter(c =>
        String(c.state_id) === String(stateId)
    );

    filtered.forEach(c=>{
        let option = document.createElement("option");
        option.value = c.id;
        option.text = c.name;

        if(String(c.id) === String(selectedCity)){
            option.selected = true;
        }

        cityDropdown.appendChild(option);
    });
}

function resetCity(){
    document.getElementById("city").innerHTML = "<option>Select City</option>";
}

</script>

