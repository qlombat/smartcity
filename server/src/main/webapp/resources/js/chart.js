/**
 * Created by jsirjacq on 4/05/17.
 */
    function getSensorValue(sensor, index) {
        $.getJSON("http://localhost:8080/api/sensors/all/".concat(sensor), function (data) {
            if (typeof data === 'undefined') {
            } else {
                updateChart(index, data.length);
            }
        });
    }

    function updateChart(indexToUpdate, valueToUpdate) {
        myChart.data.datasets[0].data[indexToUpdate] = valueToUpdate;
        myChart.update();
    }

    function updateValues() {
        getSensorValue("auxiliaryCarDetectorActor1", 0);
        getSensorValue("auxiliaryCarDetectorActor2", 1);
        getSensorValue("mainCarDetectorActor1", 2);
        getSensorValue("mainCarDetectorActor2", 3);
    }

    var ctx = document.getElementById("myChart");
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ["North", "South", "West", "East"],
            datasets: [{
                label: '# of Votes',
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
            }
        }
    });
    updateValues();
    setInterval(updateValues(), 3000);