<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Employee Register</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
</head>
<body>

<div class="container">
    <center><h2>Employee Registration</h2></center>

    <form action="${pageContext.request.contextPath}/register"
      method="post"
      enctype="multipart/form-data">


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
        
		<div class="input-group gender-row">
		    
		    <label class="gender-label">Gender</label>
		
		    <div class="gender-options">
		        <label>
		            <input type="radio" name="gender" value="Male" required>
			    👨Male
		        </label>
		
		        <label>
		            <input type="radio" name="gender" value="Female">
			    👩Female
		        </label>
		
		        <label>
		            <input type="radio" name="gender" value="Other">
			    ⚧ Other
		        </label>
		    </div>
		
		</div>
		
		<div class="input-group">
    	<input type="file" name="photoFile" accept="image/*">
	</div>
      

        <div class="input-group">
            <input type="text" name="address" placeholder="Address">
        </div>

        <div class="input-group">
            <input type="text" name="contactNo" placeholder="Contact No">
        </div>

        <!-- COUNTRY -->
        <div class="input-group">
            <select id="country" name="countryId">
                <option value="">Select Country</option>
            </select>
        </div>

        <!-- STATE -->
        <div class="input-group">
            <select id="state" name="stateId" disabled>
                <option value="">Select State</option>
            </select>
        </div>

        <!-- CITY -->
        <div class="input-group">
            <select id="city" name="cityId" disabled>
                <option value="">Select City</option>
            </select>
        </div>

        <button type="submit">Register</button>
    </form>
    
     <p style="text-align:center;margin-top:10px;font-size:18px;">
     Already have an account? 
    <a href="${pageContext.request.contextPath}/login">Login</a>
</p>
   

</div>

<script>

let allCities = [];

document.addEventListener("DOMContentLoaded", async function(){

    await loadAllCities();   
    loadCountries();

    document.getElementById("country").addEventListener("change", function(){
        loadStates(this.value);
        document.getElementById("city").innerHTML = "<option>Select City</option>";
    });

    document.getElementById("state").addEventListener("change", function(){
        loadCities(this.value);
    });

});

async function loadAllCities(){
    let res = await fetch("/EmployeeMavenProject/location/allCities");
    allCities = await res.json();
    console.log("Cities Loaded:", allCities);
}

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

            dropdown.appendChild(option);
        });

    })
    .catch(err => console.log("Country error:", err));
}


function loadStates(countryId){

    fetch("/EmployeeMavenProject/location/states/" + countryId)
    .then(res => res.json())
    .then(data => {

        let stateDropdown = document.getElementById("state");
        stateDropdown.disabled = false;
        stateDropdown.innerHTML = "<option value=''>Select State</option>";

        document.getElementById("city").innerHTML = "<option value=''>Select City</option>";
        document.getElementById("city").disabled = false;

        data.forEach(function(s){

            let option = document.createElement("option");
            option.value = s.id;
            option.text = s.name;

            stateDropdown.appendChild(option);
        });

    })
    .catch(err => console.log("State error:", err));
}

function loadCities(stateId){

    let cityDropdown = document.getElementById("city");
    cityDropdown.innerHTML = "<option>Select City</option>";

    if(!stateId) return;

    let filtered = allCities.filter(c => String(c.state_id) === String(stateId));

    console.log("Filtered Cities:", filtered);

    filtered.forEach(c=>{
        let option = document.createElement("option");
        option.value = c.id;
        option.text = c.name;
        cityDropdown.appendChild(option);
    });
}


/* function loadCities(stateId){

    fetch("/EmployeeMavenProject/location/cities/" + stateId)
    .then(res => res.json())
    .then(data => {

        let cityDropdown = document.getElementById("city");
        cityDropdown.disabled = false;
        cityDropdown.innerHTML = "<option value=''>Select City</option>";

        data.forEach(function(c){

            let option = document.createElement("option");
            option.value = c.id;
            option.text = c.name;

            cityDropdown.appendChild(option);	
        });

    })
    .catch(err => console.log("City error:", err));
} */


</script>


</body>
</html>
