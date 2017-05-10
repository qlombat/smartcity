/**
 * Created by jsirjacq on 9/05/17.
 */
function updateValues(time, periods, sensor) {
    getSensorEvolution(sensor, 0, time, periods);
}

function getSensorEvolution(sensor, datasetIndex, time, periods) {
    $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/sensors/all/evolution/".concat(sensor).concat("?time=")
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
