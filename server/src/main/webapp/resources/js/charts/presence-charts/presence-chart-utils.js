/**
 * Created by jsirjacq on 8/05/17.
 */
function getSensorValue(sensor, index, time, chartToUpdate) {
    $.getJSON("http://localhost:8080/api/sensors/all/".concat(sensor).concat("?count=true").concat("&time=").concat(time), function (data) {
        updateChart(index, data.size, chartToUpdate);

    })
        .fail(function () {
            updateChart(index, 0, chartToUpdate);
        })
}

function updateValues(time, chartToUpdate) {
    getSensorValue("auxiliaryCarDetectorActor2", 0, time, chartToUpdate);
    getSensorValue("auxiliaryCarDetectorActor1", 1, time, chartToUpdate);
    getSensorValue("mainCarDetectorActor1", 2, time, chartToUpdate);
    getSensorValue("mainCarDetectorActor2", 3, time, chartToUpdate);
}

function updateChart(indexToUpdate, valueToUpdate, chartToUpdate) {
    chartToUpdate.data.datasets[0].data[indexToUpdate] = valueToUpdate;
    chartToUpdate.update();
}

function updateEvolutionValues(time, periods) {
    getSensorEvolution("auxiliaryCarDetectorActor2", 0, time, periods);
    getSensorEvolution("auxiliaryCarDetectorActor1", 1, time, periods);
    getSensorEvolution("mainCarDetectorActor1", 2, time, periods);
    getSensorEvolution("mainCarDetectorActor2", 3, time, periods);
}

function getSensorEvolution(sensor, datasetIndex, time, periods) {
    $.getJSON("http://localhost:8080/api/sensors/all/evolution/".concat(sensor).concat("?time=")
        .concat(time).concat("&periods=").concat(periods), function (data) {
        if (typeof data._1.evolutionValues === "undefined"){
            var nullArray = [];
            for (var i = 0; i < periods.length; i++) {
                nullArray[i]=0;
            }
            updateLineChartValues(datasetIndex, nullArray, data._2.periods);
        }else{
            updateLineChartValues(datasetIndex, data._1.evolutionValues, data._2.periods);
        }
    })
}

function updateLineChartValues(datasetIndex, values, periods) {
    lineChart.data.labels = periods;
    lineChart.data.datasets[datasetIndex].data = values;
    lineChart.update();
}