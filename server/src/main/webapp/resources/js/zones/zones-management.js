/**
 * Handles the zones management page
 *
 * @author Noe Picard
 */

function showalert(message, alerttype) {
    $('#alert_placeholder').append('<div id="alertdiv" class="alert ' + alerttype + '"><a class="close" data-dismiss="alert">×</a><span>' + message + '</span></div>')

    setTimeout(function () { // this will automatically close the alert and remove this if the users doesnt close it in 5 secs
        $("#alertdiv").remove();
    }, 5000);
}

$(document).ready(function () {
    $('#close-open-zone-form').validator().on('submit', function (e) {
        if (e.isDefaultPrevented()) {
        } else {
            event.preventDefault();
            var open_check = $('#opened').is(":checked");
            var open_param = "close";
            var message = "closed";
            if (open_check) {
                open_param = "open";
                message = "opened";
            }

            $.post(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port
                + "/api/zones/" + $('#inputName').val()
                + "/" + open_param + "?full_name=" + $('#inputFullName').val()
            ).done(function () {
                $.notify({
                    // options
                    message: 'Zone ' + $('#inputFullName').val() + " " + message
                }, {
                    // settings
                    type: 'success'
                });
            }).fail(function () {
                $.notify({
                    // options
                    message: 'Unable to ' + open_param + ' zone ' + $('#inputFullName').val()
                }, {
                    // settings
                    type: 'danger'
                });
            });

        }
    });

    $('#bus-schedule-form').validator({
        custom: {
            'opening': function (el) {
                var regexp = /^(0[0-9]|1[0-9]|2[0-4]):[0-5][0-9]$/;

                return !regexp.test(el.val());
            },
            'closing': function (el) {
                var regexp = /^(0[0-9]|1[0-9]|2[0-4]):[0-5][0-9]$/;

                if (regexp.test(el.val())) {
                    var el_time = moment(el.val(), 'HH:mm');
                    var other_el_time = moment($('#opening-time').val(), 'HH:mm');
                    return !(el_time.isAfter(other_el_time));
                } else {
                    return true;
                }
            }
        }
    }).on('submit', function (e) {
        if (e.isDefaultPrevented()) {
        } else {
            event.preventDefault();

            $.post(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port
                + "/api/bus_schedule/" + $('#day').val()
                + "?opening_time=" + $('#opening-time').val()
                + "&closing_time=" + $('#closing-time').val()
            ).done(function () {
                $.notify({
                    // options
                    message: 'Bus schedule added'
                }, {
                    // settings
                    type: 'success'
                });
            }).fail(function () {
                $.notify({
                    // options
                    message: 'Unable to add the bus schedule'
                }, {
                    // settings
                    type: 'danger'
                });
            });

        }
    });

    $('.clockpicker').clockpicker({
        align: 'left',
        autoclose: true
    });
});
