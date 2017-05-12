/**
 * Handles the zones management page
 *
 * @author Noe Picard
 */

var table = $('#datatables-bus-schedules').DataTable({
    responsive: true
});

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


    $('.clockpicker').clockpicker({
        align: 'left',
        autoclose: true
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
            ).done(function (result) {
                updateTable();

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

    function updateTable() {
        $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port
            + "/api/bus_schedule/", function (result) {

            var html = '';
            $.each(result, function (index, item) {
                html += "<tr>" +
                    "<td>" + item.id + "</td>" +
                    "<td>" + item.day.charAt(0).toUpperCase() + item.day.slice(1) + "</td>" +
                    "<td>" + item.openingTime + "</td>" +
                    "<td>" + item.closingTime + "</td>" +
                    "<td>" + "<button id='delete-bs-" + item.id + "' class='btn btn-danger btn-xs'><span class='glyphicon glyphicon-trash'></span></button>" + "</td>" +
                    "</tr>";
            });

            $('#datatables-bus-schedule-body').html(html);


            $.each(result, function (index, item) {
                $('#delete-bs-' + item.id).on('click', function () {
                    var me = $(this);
                    $.ajax({
                        url: window.location.protocol + "//" + window.location.hostname + ":" + window.location.port
                        + "/api/bus_schedule/" + item.id,
                        type: 'DELETE',
                        success: function () {
                            $.notify({
                                // options
                                message: 'Bus schedule deleted'
                            }, {
                                // settings
                                type: 'danger'
                            });
                            updateTable();
                        }
                    });
                });
            });
        });
    }

    updateTable();

});
