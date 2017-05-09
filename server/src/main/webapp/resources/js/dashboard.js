/**
 * Manages the index view.
 *
 * @author Noe Picard
 * @author Quentin Lombat
 */
$(document).ready(function () {
    function updateSensors() {
        $.getJSON("http://localhost:8080/api/sensors/temperature", function (data) {
            if (typeof data.value === 'undefined') {
                $("#temp-value").text("Unavailable");
            } else {
                $("#temp-value").text(data.value + "° C");
            }
        });
        $.getJSON("http://localhost:8080/api/sensors/light", function (data) {
            if (typeof data.value === 'undefined') {
                $("#luminosity").text("Unavailable");
            } else {
                $("#luminosity").text(data.value + " lux");
            }
        });
        $.getJSON("http://localhost:8080/api/sensors/humidity", function (data) {
            if (typeof data.value === 'undefined') {
                $("#humidity").text("Unavailable");
            } else {
                $("#humidity").text(data.value + " %");
            }

        });
        $.getJSON("http://localhost:8080/api/rfid/parking/accessibility", function (data) {
            if (typeof data._1.taken === 'undefined') {
                $("#parking").text("Unavailable");
            } else {
                $("#parking").text(data._2.totalplaces - data._1.taken + " of " + data._2.totalplaces);
            }

        });
    }

    function updateZoneAlerts() {
        var zoneAlerts = $('#zones-alerts');
        $.getJSON("http://localhost:8080/api/zones/history?take=5", function (data) {
            if (data.length > 0) {
                zoneAlerts.parents().find('#non-empty-zones-alerts').removeClass('hidden');
                zoneAlerts.parents().find('#empty-zones-alerts').addClass('hidden')
            } else if (data.length < 1) {
                zoneAlerts.parents().find('#empty-zones-alerts').removeClass('hidden');
                zoneAlerts.parents().find('#non-empty-zones-alerts').addClass('hidden');
            }

            zoneAlerts.children().remove();

            $.each(data, function (i, item) {
                // Split timestamp into [ Y, M, D, h, m, s ]
                var t = item.createdAt.split(/[- :]/);
                // Apply each element to the Date function
                var d = new Date(Date.UTC(t[0], t[1] - 1, t[2].split("T")[0], t[2].split("T")[1],
                    t[3], t[4].split("Z")[0]));

                if (item.opened === true) {
                    zoneAlerts.append("<div class='list-group-item'>"
                        + "<i class='fa fa-circle-o fa-fw'></i>  "
                        + item.nameFull + " opened"
                        + "<span class='pull-right text-muted small'><em> "
                        + moment().from(d, true) + " ago </em> </span> </div>");
                } else {
                    zoneAlerts.append("<div class='list-group-item'>"
                        + "<i class='fa fa-dot-circle-o fa-fw'></i>  "
                        + item.nameFull + " closed"
                        + "<span class='pull-right text-muted small'><em> "
                        + moment().from(d, true) + " ago </em> </span> </div>");
                }
            });
        });

    }

    updateSensors();
    updateZoneAlerts();
    setInterval(updateSensors, 3000);
    setInterval(updateZoneAlerts, 3000)
});