/**
 * Created by jsirjacq on 8/05/17.
 */
function getSensorValue(sensor, index, time, chartToUpdate) {
    $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/sensors/all/".concat(sensor).concat("?count=true").concat("&time=").concat(time), function (data) {
        updateChart(index, data.all.length, chartToUpdate);

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
    $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/sensors/all/evolution/".concat(sensor).concat("?time=")
        .concat(time).concat("&periods=").concat(periods), function (data) {
        updateLineChartValues(datasetIndex, data.list);
    })
}

function updateLineChartValues(datasetIndex, list) {
    lineChart.data.labels = list._1;
    dataList = [];
        $.each(list._2, function (elem) {
            dataList.push(list._2[elem].length);
        })
    lineChart.data.datasets[datasetIndex].data = dataList;
    lineChart.update();
}