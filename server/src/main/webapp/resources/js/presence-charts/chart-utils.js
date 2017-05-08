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