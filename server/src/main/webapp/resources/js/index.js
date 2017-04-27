/**
 * Manages the index view.
 *
 * @author Noe Picard
 * @author Quentin Lombat
 */
$(document).ready(function () {
    function updateSensors(){
        console.log("coucou");
        $.getJSON("http://localhost:8080/api/sensors/temperature", function (data) {
            if(typeof data.value === 'undefined'){
                $("#temp-value").text("Unavailable");
            }else{
                $("#temp-value").text(data.value + "° C");
            }
        });
        $.getJSON("http://localhost:8080/api/sensors/light", function (data) {
            if(typeof data.value === 'undefined'){
                $("#luminosity").text("Unavailable");
            }else{
                $("#luminosity").text(data.value + " lux");
            }
        });
        $.getJSON("http://localhost:8080/api/sensors/humidity", function (data) {
            if(typeof data.value === 'undefined'){
                $("#humidity").text("Unavailable");
            }else{
                $("#humidity").text(data.value + " %");
            }

        });
    }

    updateSensors();
    setInterval(updateSensors, 3000);
});