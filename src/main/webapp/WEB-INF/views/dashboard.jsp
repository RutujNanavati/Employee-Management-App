<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Dashboard</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">


<style>

/* Page Background */
body {
    background: linear-gradient(135deg, lightgreen, green, #61db57);
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
}

/* Main Card */
.dashboard-card {
    width:60%;
    hight:50%;
    margin: 40px auto;
    margin-top:90px;
    background: #ffffff;
    padding: 30px;
    border-radius: 12px;
    box-shadow: 0 6px 18px rgba(0,0,0,0.08);
}

/* Title */
.dashboard-title {
    text-align: center;
    margin-bottom: 30px;
}

/* Stats Row */
.stats-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 40px;
}

/* Individual Stat Box */
.stat-box {
    flex: 1;
    margin: 0 10px;
    padding: 20px;
    border-radius: 10px;
    background: #f4f6f9;
    text-align: center;
}

.stat-box h4 {
    margin: 0;
    font-weight: 500;
    color: #555;
}

.stat-box h2 {
    margin-top: 10px;
}

.stat-box.total { border-top: 4px solid lightgreen; }
.stat-box.male { border-top: 4px solid blue; }
.stat-box.female { border-top: 4px solid pink; }
.stat-box.other { border-top: 4px solid yellow; }

/* Chart Container */
.chart-container {
    width: 350px;
    height: 350px;
    margin: 0 auto;
}

.stat-box {
    transition: 0.3s ease;
}

.stat-box:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 18px rgba(0,0,0,0.1);
}


</style>
</head>

<body>
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

<div class="dashboard-card">

    <h2 class="dashboard-title">Employee Analytics Dashboard</h2>

    <!-- Stats -->
    <div class="stats-row">
        <div class="stat-box total">
            <h4>Total Employees</h4>
            <h2 id="totalCount">0</h2>
        </div>

        <div class="stat-box male">
            <h4>Male</h4>
            <h2 id="maleCount">0</h2>
        </div>

        <div class="stat-box female">
            <h4>Female</h4>
            <h2 id="femaleCount">0</h2>
        </div>

        <div class="stat-box other">
            <h4>Other</h4>
            <h2 id="otherCount">0</h2>
        </div>
    </div>

    <!-- Chart -->
    <div class="chart-container">
        <canvas id="genderChart"></canvas>
    </div>

</div>

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
function animateValue(id, start, end, duration) {
    let range = end - start;
    let current = start;
    let increment = end > start ? 1 : -1;
    let stepTime = Math.abs(Math.floor(duration / range));
    let obj = document.getElementById(id);

    let timer = setInterval(function() {
        current += increment;
        obj.innerText = current;
        if (current == end) {
            clearInterval(timer);
        }
    }, stepTime);
}

window.onload = function() {
    animateValue("totalCount", 0, ${total}, 800);
    animateValue("maleCount", 0, ${male}, 800);
    animateValue("femaleCount", 0, ${female}, 800);
    animateValue("otherCount", 0, ${other}, 800);
};

const ctx = document.getElementById("genderChart");

const centerText = {
    id: 'centerText',
    beforeDraw(chart) {
        const { width } = chart;
        const { height } = chart;
        const ctx = chart.ctx;

        ctx.restore();
        ctx.font = "bold 24px Arial";
        ctx.textBaseline = "middle";

        const text = "${total}";
        const textX = Math.round((width - ctx.measureText(text).width) / 2);
        const textY = height / 2;

        ctx.fillText(text, textX, textY);
        ctx.save();
    }
};

new Chart(ctx, {
    type: 'doughnut',
    data: {
        labels: ['Male', 'Female', 'Other'],
        datasets: [{
            data: [${male}, ${female}, ${other}],
            backgroundColor: ['blue', 'pink', 'yellow']
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: true,
        cutout: '50%',
        plugins: {
            legend: {
                position: 'bottom'
            }
        }
    },
});
</script>


</body>
</html>
