/**
 * Created by jsirjacq on 8/05/17.
 */

$("#btn-doughnut-all").on("click", function () {
    timeLapsDoughnut = "all";
    updateValues(timeLapsDoughnut, doughnutChart);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-doughnut-day").on("click", function () {
    timeLapsDoughnut = "day";
    updateValues(timeLapsDoughnut, doughnutChart);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-doughnut-hour").on("click", function () {
    timeLapsDoughnut = "hour";
    updateValues(timeLapsDoughnut, doughnutChart);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-doughnut-month").on("click", function () {
    timeLapsDoughnut = "month";
    updateValues(timeLapsDoughnut, doughnutChart);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

var ctx = document.getElementById("doughnut-chart");
var doughnutChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
        labels: ["North", "South", "West", "East"],
        datasets: [{
            label: 'Number of passages',
            data: [0, 0, 0, 0],
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)'
            ],
            hoverBackgroundColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)'
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)'
            ],
            borderWidth: 0.3
        }]
    },
});
var timeLapsDoughnut = "all";
$("#btn-doughnut-all").toggleClass('active');
updateValues(timeLapsDoughnut, doughnutChart);