/**
 * Handles the zones charts
 *
 * @author Noe Picard
 */
$(document).ready(function () {
    var zonesChart;

    function getDate(timestamp) {
        // Split timestamp into [ Y, M, D, h, m, s ]
        var t = timestamp.split(/[- :]/);

        // Apply each element to the Date function
        return new Date(Date.UTC(t[0], t[1] - 1, t[2].split("T")[0], t[2].split("T")[1],
            t[3], t[4].split("Z")[0]));
    }

    var colors = [
        'rgba(26, 188, 156, 0.2)',
        'rgba(46, 204, 113, 0.2)',
        'rgba(52, 152, 219, 0.2)',
        'rgba(155, 89, 182, 0.2)',
        'rgba(52, 73, 94, 0.2)',
        'rgba(22, 160, 133, 0.2)',
        'rgba(39, 174, 96, 0.2)',
        'rgba(41, 128, 185, 0.2)',
        'rgba(142, 68, 173, 0.2)',
        'rgba(44, 62, 80, 0.2)',
        'rgba(241, 196, 15, 0.2)',
        'rgba(230, 126, 34, 0.2)',
        'rgba(231, 76, 60, 0.2)',
        'rgba(236, 240, 241, 0.2)',
        'rgba(149, 165, 166, 0.2)',
        'rgba(243, 156, 18, 0.2)',
        'rgba(192, 57, 43, 0.2)',
        'rgba(189, 195, 199, 0.2)',
        'rgba(127, 140, 141, 0.2)'
    ];

    var colorsHover = [
        'rgba(26, 188, 156,1.0)',
        'rgba(46, 204, 113,1.0)',
        'rgba(52, 152, 219,1.0)',
        'rgba(155, 89, 182,1.0)',
        'rgba(52, 73, 94,1.0)',
        'rgba(22, 160, 133,1.0)',
        'rgba(39, 174, 96,1.0)',
        'rgba(41, 128, 185,1.0)',
        'rgba(142, 68, 173,1.0)',
        'rgba(44, 62, 80,1.0)',
        'rgba(241, 196, 15,1.0)',
        'rgba(230, 126, 34,1.0)',
        'rgba(231, 76, 60,1.0)',
        'rgba(236, 240, 241,1.0)',
        'rgba(149, 165, 166,1.0)',
        'rgba(243, 156, 18,1.0)',
        'rgba(192, 57, 43,1.0)',
        'rgba(189, 195, 199,1.0)',
        'rgba(127, 140, 141,1.0)'
    ];

    function getColor(index) {
        return colors[index % colors.length];
    }

    function getColorHover(index) {
        return colorsHover[index % colorsHover.length];
    }

    var colorIndex = 0;

    function updateData(chart, label, data) {
        chart.data.labels.push(label);
        chart.data.datasets[0].data.push(data);
        chart.data.datasets[0].backgroundColor.push(getColor(colorIndex));
        chart.data.datasets[0].hoverBackgroundColor.push(getColorHover(colorIndex));
        chart.data.datasets[0].borderColor.push(getColor(colorIndex));
        colorIndex++;
        chart.update();
    }

    function updateValues(chart, timeLapse) {
        chart.data.labels = [];
        chart.data.datasets[0].data = [];
        chart.data.datasets[0].backgroundColor = [];
        chart.data.datasets[0].hoverBackgroundColor = [];
        chart.data.datasets[0].borderColor = [];
        colorIndex = 0;

        $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port
            + "/api/zones/history?order=asc" + "&time=" + timeLapse,
            function (result) {
                var closedTime = {};

                console.log(result);

                for (var i = 0; i < result.length; i++) {
                    var current = result[i];
                    // The table doesn't contain the zone and the zone is closed
                    if (closedTime[current.name] === undefined
                        && current.opened === false) {

                        closedTime[current.name] = {
                            name: current.nameFull,
                            acc: 0,
                            last_closed: current
                        }

                    } else if (closedTime[current.name] !== undefined
                        && current.opened === false
                        && closedTime[current.name].last_closed === undefined) {

                        closedTime[current.name] = {
                            name: closedTime[current.name].name,
                            acc: closedTime[current.name].acc,
                            last_closed: current
                        }

                    } else if (closedTime[current.name] !== undefined
                        && current.opened === true
                        && closedTime[current.name].last_closed !== undefined) {

                        var last = closedTime[current.name].last_closed;

                        var start = moment(getDate(last.createdAt));
                        var end = moment(getDate(current.createdAt));

                        closedTime[current.name].acc = closedTime[current.name].acc + parseInt(end.diff(start, 'seconds'));
                        closedTime[current.name].last_closed = undefined;
                    }
                }

                for (var zone in closedTime) {
                    if (closedTime[zone].last_closed === undefined) {
                        updateData(chart, closedTime[zone].name, closedTime[zone].acc)
                    } else {
                        var last = closedTime[zone].last_closed;

                        var start = moment(getDate(last.createdAt));

                        closedTime[zone].acc = closedTime[zone].acc + parseInt(moment().diff(start, 'seconds'));
                        closedTime[zone].last_closed = undefined;

                        updateData(chart, closedTime[zone].name, closedTime[zone].acc)
                    }
                }
            });
    }


    $.getJSON(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/api/zones/history?order=asc&time=all", function (result) {
        var html = '';
        $.each(result, function (index, item) {
            var status;
            if (item.opened === true) {
                status = "Opened";
            } else {
                status = "Closed";
            }

            html += "<tr class='grade'" + item.name + ">" +
                "<td>" + item.name + "</td>" +
                "<td>" + item.nameFull + "</td>" +
                "<td>" + status + "</td>" +
                "<td>" + moment(getDate(item.createdAt)).format("dddd, MMMM Do YYYY, h:mm:ss a") + "</td>" +
                "</tr>"

        });

        $('#datatables-zones-body').html(html);

        $('#datatables-zones').DataTable({
            responsive: true,
            "order": [[3, "desc"]]
        });

        $('#loading-wrapper').hide();
        $('#table-wrapper').removeClass('hidden')
    });


    $("#btn-doughnut-hour").on("click", function () {
        updateValues(zonesChart, "hour");
        $(this).parent(".btn-group").find(".active").toggleClass("active");
        $(this).toggleClass('active');
    });

    $("#btn-doughnut-day").on("click", function () {
        updateValues(zonesChart, "day");
        $(this).parent(".btn-group").find(".active").toggleClass("active");
        $(this).toggleClass('active');
    });

    $("#btn-doughnut-month").on("click", function () {
        updateValues(zonesChart, "month");
        $(this).parent(".btn-group").find(".active").toggleClass("active");
        $(this).toggleClass('active');
    });

    $("#btn-doughnut-all").on("click", function () {
        updateValues(zonesChart, "all");
        $(this).parent(".btn-group").find(".active").toggleClass("active");
        $(this).toggleClass('active');
    });


    var ctx = document.getElementById("doughnut-chart");
    zonesChart = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: [],
            datasets: [{
                label: 'Times when zones where close',
                data: [],
                backgroundColor: [],
                hoverBackgroundColor: [],
                borderColor: [],
                borderWidth: 0.3
            }]
        },
        options: {
            animation: {
                animateScale: true
            },
            tooltips: {
                enabled: true,
                mode: 'single',
                callbacks: {
                    label: function (tooltipItem, data) {
                        var label = data.labels[tooltipItem.index];
                        var datasetLabel = moment.duration(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index], 'seconds').format("d[d] h:mm:ss");
                        return label + ': ' + datasetLabel;
                    }
                }
            }
        }
    });

    $("#btn-doughnut-day").toggleClass('active');
    updateValues(zonesChart, "day");
});