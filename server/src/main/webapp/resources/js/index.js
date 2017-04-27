/**
 * Manages the index view.
 *
 * @author Noe Picard
 */
$(document).ready(function () {
    $.getJSON("http://localhost:8080/api/sensors/temperature", function (data) {
        $("#temp-value").text(data.value + "° C")
    });
    $.getJSON("http://localhost:8080/api/sensors/light", function (data) {
        $("#luminosity").text(data.value + " lux")
    });
    $.getJSON("http://localhost:8080/api/sensors/humidity", function (data) {
        $("#humidity").text(data.value + "%")
    });
});