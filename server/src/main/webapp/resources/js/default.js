$(document).ready(function () {
    var actorModal = $("#actor-modal");

    $("#start-actor").on("click", function () {
        actorModal.find(".modal-title").text("Starting the actors");

        actorModal.modal('show');

        $.get(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/actors/start", function (data) {
            actorModal.find(".fa-spin").addClass('hidden');
            actorModal.find(".modal-footer").removeClass('hidden');

            var splittedData = data.toString().split(" ");
            if (splittedData[0] === "Impossible") {
                actorModal.find(".modal-content").addClass("panel-danger");
                actorModal.find(".modal-header").addClass("panel-heading");
            } else {
                actorModal.find(".modal-content").addClass("panel-success");
                actorModal.find(".modal-header").addClass("panel-heading");
            }

            $("#actor-modal-content").text(data).removeClass('hidden');
            setTimeout(function() {actorModal.modal('hide');}, 2500);
        })
    });

    $("#stop-actor").on("click", function () {
        actorModal.find(".modal-title").text("Stopping the actors");

        actorModal.modal('show');

        $.get(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port +  "/actors/stop", function (data) {
            actorModal.find(".fa-spin").addClass('hidden');
            actorModal.find(".modal-footer").removeClass('hidden');

            var splittedData = data.toString().split(" ");
            if (splittedData[0] === "Impossible") {
                actorModal.find(".modal-content").addClass("panel-danger");
                actorModal.find(".modal-header").addClass("panel-heading");
            } else {
                actorModal.find(".modal-content").addClass("panel-success");
                actorModal.find(".modal-header").addClass("panel-heading");
            }

            $("#actor-modal-content").text(data).removeClass('hidden');
            setTimeout(function() {actorModal.modal('hide');}, 2500);
        })
    });


    actorModal.on('hidden.bs.modal', function () {
        actorModal.find(".modal-content").removeClass("panel-danger");
        actorModal.find(".modal-content").addClass("panel-success");
        actorModal.find(".modal-header").removeClass("panel-heading");
        actorModal.find(".fa").removeClass('hidden');
        actorModal.find(".modal-footer").addClass('hidden');
        $("#actor-modal-content").text("").addClass('hidden');
    });
});