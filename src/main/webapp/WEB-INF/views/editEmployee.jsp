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

<form action="${pageContext.request.contextPath}/employees/${action}" method="post">

    <input type="hidden" name="id" value="${id}" />

    <div class="input-group">
        <input type="text" name="firstName" value="${firstName}" placeholder="First Name" required>
    </div>

    <div class="input-group">
        <input type="text" name="lastName" value="${lastName}" placeholder="Last Name" required>
    </div>

    <div class="input-group">
        <input type="text" name="username" value="${username}" placeholder="Username" required>
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

let selectedCountry = "${selectedCountry}";
let selectedState = "${selectedState}";
let selectedCity = "${selectedCity}";

document.addEventListener("DOMContentLoaded", function(){

    loadCountries();

    document.getElementById("country").addEventListener("change", function(){
        loadStates(this.value);
    });

    document.getElementById("state").addEventListener("change", function(){
        loadCities(this.value);
    });

});

function loadCountries(){

    fetch("/EmployeeMavenProject/location/countries")
    .then(res => res.json())
    .then(data => {

        let dropdown = document.getElementById("country");

        dropdown.innerHTML = "<option value=''>Select Country</option>";

        data.forEach(function(c){
            let option = document.createElement("option");
            option.value = c.id;
            option.text = c.name;

            if(c.id == selectedCountry){
                option.selected = true;
            }

            dropdown.appendChild(option);
        });

        if(selectedCountry){
            loadStates(selectedCountry);
        }
    });
}

function loadStates(countryId){

    fetch("/EmployeeMavenProject/location/states/" + countryId)
    .then(res => res.json())
    .then(data => {

        let stateDropdown = document.getElementById("state");
        stateDropdown.innerHTML = "<option value=''>Select State</option>";

        data.forEach(function(s){
            let option = document.createElement("option");
            option.value = s.id;
            option.text = s.name;

            if(s.id == selectedState){
                option.selected = true;
            }

            stateDropdown.appendChild(option);
        });

        if(selectedState){
            loadCities(selectedState);
        }
    });
}

function loadCities(stateId){

    fetch("/EmployeeMavenProject/location/cities/" + stateId)
    .then(res => res.json())
    .then(data => {

        let cityDropdown = document.getElementById("city");
        cityDropdown.innerHTML = "<option value=''>Select City</option>";

        data.forEach(function(c){
            let option = document.createElement("option");
            option.value = c.id;
            option.text = c.name;

            if(c.id == selectedCity){
                option.selected = true;
            }

            cityDropdown.appendChild(option);
        });
    });
}

</script>
