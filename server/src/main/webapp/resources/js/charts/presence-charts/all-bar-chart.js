/**
 * Created by jsirjacq on 4/05/17.
 */

$("#btn-bar-all").on("click", function () {
    timeLaps = "all";
    updateValues(timeLaps, barChart);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-bar-day").on("click", function () {
    timeLaps = "day";
    updateValues(timeLaps, barChart);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-bar-hour").on("click", function () {
    timeLaps = "hour";
    updateValues(timeLaps, barChart);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-bar-month").on("click", function () {
    timeLaps = "month";
    updateValues(timeLaps, barChart);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

var ctx = document.getElementById("bar-chart");
var barChart = new Chart(ctx, {
    type: 'bar',
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
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
            ],
            borderWidth: 1
        }]
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
var timeLaps = "all";
$("#btn-bar-all").toggleClass('active');
updateValues(timeLaps, barChart);