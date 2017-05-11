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
            // handle the invalid form...
        } else {
            event.preventDefault();
            // everything looks good!
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
    })
});
