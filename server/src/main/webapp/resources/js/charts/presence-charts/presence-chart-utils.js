/**
 * Created by jsirjacq on 8/05/17.
 */
function getSensorValue(sensor, index, time, chartToUpdate) {
    $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/sensors/all/".concat(sensor).concat("?time=").concat(time), function (data) {
        updateChart(index, data.all.length, chartToUpdate);

    })
        .fail(function () {
            updateChart(index, 0, chartToUpdate);
        })
}

function updateValues(time, chartToUpdate) {
    getSensorValue("auxiliaryCarDetectorActorNorth", 0, time, chartToUpdate);
    getSensorValue("auxiliaryCarDetectorActorSouth", 1, time, chartToUpdate);
    getSensorValue("mainCarDetectorActorEast", 2, time, chartToUpdate);
    getSensorValue("mainCarDetectorActorWest", 3, time, chartToUpdate);
}

function updateChart(indexToUpdate, valueToUpdate, chartToUpdate) {
    chartToUpdate.data.datasets[0].data[indexToUpdate] = valueToUpdate;
    chartToUpdate.update();
}

function updateEvolutionValues(time, periods) {
    getSensorEvolution("auxiliaryCarDetectorActorNorth", 0, time, periods);
    getSensorEvolution("auxiliaryCarDetectorActorSouth", 1, time, periods);
    getSensorEvolution("mainCarDetectorActorEast", 2, time, periods);
    getSensorEvolution("mainCarDetectorActorWest", 3, time, periods);
}

function getSensorEvolution(sensor, datasetIndex, time, periods) {
    $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/sensors/all/evolution/".concat(sensor).concat("?time=")
        .concat(time).concat("&periods=").concat(periods), function (data) {
        updateLineChartValues(datasetIndex, data.result);
    })
}

function updateLineChartValues(datasetIndex, result) {
    lineChart.data.labels = result._1;
    dataList = [];
        $.each(result._2, function (elem) {
            dataList.push(result._2[elem].length);
        })
    lineChart.data.datasets[datasetIndex].data = dataList;
    lineChart.update();
}