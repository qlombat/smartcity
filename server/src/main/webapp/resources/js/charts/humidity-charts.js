/**
 * Created by jsirjacq on 9/05/17.
 */

$("#btn-day").on("click", function () {
    timeLapse = "day";
    updateValues(timeLapse, periods, sensor);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-hour").on("click", function () {
    timeLapse = "hour";
    updateValues(timeLapse, periods, sensor);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-month").on("click", function () {
    timeLapse = "month";
    updateValues(timeLapse, periods, sensor);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-5").on("click", function () {
    periods = "5";
    updateValues(timeLapse, periods, sensor);
});

$("#btn-10").on("click", function () {
    periods = "10";
    updateValues(timeLapse, periods, sensor);
});

$("#btn-20").on("click", function () {
    periods = "20";
    updateValues(timeLapse, periods, sensor);
});

$("#btn-50").on("click", function () {
    periods = "50";
    updateValues(timeLapse, periods, sensor);
});

var ctx = document.getElementById("line-chart");
var lineChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ["1", "2", "3", "4"],
        datasets: [{
            label: 'Humidity',
            data: [0, 0, 0, 0],
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        },
        ]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                }
            }]
        },
        legend: {
            display: false
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItem) {
                    return tooltipItem.yLabel;
                }
            }
        },
    }
});
$("#btn-hour").toggleClass('active');
var periods = "10";
var sensor = "humidity"
var timeLapse = "hour";
updateValues(timeLapse, periods, sensor);

