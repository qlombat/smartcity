/**
 * Manages the index view.
 *
 * @author Noe Picard
 * @author Quentin Lombat
 * @author Justin Sirjacques
 */
$(document).ready(function () {
    function updateSensors() {
        $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/sensors/temperature", function (data) {
            if (typeof data.value === 'undefined') {
                $("#temp-value").text("N/A");
            } else {
                $("#temp-value").text(data.value + "° C");
            }
        }).fail(function () {
            $("#temp-value").text("N/A");
        });
        $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/sensors/light", function (data) {
            if (typeof data.value === 'undefined') {
                $("#luminosity").text("N/A");
            } else {
                $("#luminosity").text(data.value + " lux");
            }
        }).fail(function () {
            $("#luminosity").text("N/A");
        });
        $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/sensors/humidity", function (data) {
            if (typeof data.value === 'undefined') {
                $("#humidity").text("N/A");
            } else {
                $("#humidity").text(data.value + " %");
            }

        }).fail(function () {
            $("#humidity").text("N/A");
        });
        $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/parking/accessibility", function (data) {
            if (typeof data._1.taken === 'undefined') {
                $("#parking").text("N/A");
            } else {
                if (data._2.totalplaces - data._1.taken == 0) {
                    $("#parking").text("Full");
                } else {
                    $("#parking").text(data._2.totalplaces - data._1.taken + " free");
                }
            }
        }).fail(function () {
            $("#parking").text("N/A");
        });
    }

    function updateZoneAlerts() {
        var zoneAlerts = $('#zones-alerts');
        $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/zones/history?take=5", function (data) {
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