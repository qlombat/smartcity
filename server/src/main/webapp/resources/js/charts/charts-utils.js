/**
 * Created by jsirjacq on 9/05/17.
 */
function updateValues(time, periods, sensor) {
    getSensorEvolution(sensor, 0, time, periods);
}

function getSensorEvolution(sensor, datasetIndex, time, periods) {
    $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port +
        "/api/sensors/all/evolution/".concat(sensor).concat("?time=")
        .concat(time).concat("&periods=").concat(periods), function (data) {
            updateLineChartValues(datasetIndex, data.result);
    })
}

function updateLineChartValues(datasetIndex, result) {
    lineChart.data.labels = result._1;
    dataList = [];
    $.each(result._2, function (elem) {
        if (result._2[elem].length == 0){
            dataList.push(result._2[elem].length);
        }else{
            average = 0;
            $.each(result._2[elem], function (e) {
              average =average + result._2[elem][e].value;
            })
            average = average / result._2[elem].length;
            dataList.push(average);
            console.log(average);
        }
    })
    lineChart.data.datasets[datasetIndex].data = dataList;
    lineChart.update();
}
