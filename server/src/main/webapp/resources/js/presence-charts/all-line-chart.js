/**
 * Created by jsirjacq on 8/05/17.
 */

$("#btn-evolution-day").on("click", function () {
    timeLapsLine = "day";
    updateEvolutionValues(timeLapsLine, periods);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-evolution-hour").on("click", function () {
    timeLapsLine = "hour";
    updateEvolutionValues(timeLapsLine, periods);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-evolution-month").on("click", function () {
    timeLapsLine = "month";
    updateEvolutionValues(timeLapsLine, periods);
    $(this).parent(".btn-group").find(".active").toggleClass("active");
    $(this).toggleClass('active');
});

$("#btn-evolution-5").on("click", function () {
    periods = "5";
    updateEvolutionValues(timeLapsLine, periods);
});

$("#btn-evolution-10").on("click", function () {
    periods = "10";
    updateEvolutionValues(timeLapsLine, periods);
});

$("#btn-evolution-20").on("click", function () {
    periods = "20";
    updateEvolutionValues(timeLapsLine, periods);
});

$("#btn-evolution-50").on("click", function () {
    periods = "50";
    updateEvolutionValues(timeLapsLine, periods);
});

var ctx = document.getElementById("line-chart");
var lineChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ["1", "2", "3", "4"],
        datasets: [{
            label: 'North',
            data: [0, 0, 0, 0],
            backgroundColor: 'rgba(255, 99, 132, 0.2)',

            borderColor: 'rgba(255,99,132,1)',
            borderWidth: 1
        },
            {
                label: 'South',
                data: [0, 0, 0, 0],
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            },
            {
                label: 'West',
                data: [0, 0, 0, 0],
                backgroundColor: 'rgba(255, 206, 86, 0.2)',
                borderColor: 'rgba(255, 206, 86, 1)',
                borderWidth: 1
            },
            {
                label: 'East',
                data: [0, 0, 0, 0],
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }

        ]
    },
    options: {}
});
var timeLapsLine = "hour";
var periods = "10";
updateEvolutionValues(timeLapsLine, periods);