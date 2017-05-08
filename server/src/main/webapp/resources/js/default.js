$(document).ready(function () {
    var actorModal = $("#actor-modal");

    $("#start-actor").on("click", function () {
        actorModal.find(".modal-title").text("Starting the actors");

        actorModal.modal('show');

        $.get("http://localhost:8080/actors/start", function (data) {
            actorModal.find(".fa-refresh").hide();
            actorModal.find(".modal-footer").show();

            var splittedData = data.toString().split(" ");
            if (splittedData[0] === "Impossible") {
                actorModal.find(".modal-content").addClass("panel-danger");
                actorModal.find(".modal-header").addClass("panel-heading");
            } else {
                actorModal.find(".modal-content").addClass("panel-success");
                actorModal.find(".modal-header").addClass("panel-heading");
            }

            $("#actor-modal-content").text(data).show();
        })
    });

    $("#stop-actor").on("click", function () {
        actorModal.find(".modal-title").text("Stopping the actors");

        actorModal.modal('show');

        $.get("http://localhost:8080/actors/stop", function (data) {
            actorModal.find(".fa-refresh").hide();
            actorModal.find(".modal-footer").show();

            var splittedData = data.toString().split(" ");
            if (splittedData[0] === "Impossible") {
                actorModal.find(".modal-content").addClass("panel-danger");
                actorModal.find(".modal-header").addClass("panel-heading");
            } else {
                actorModal.find(".modal-content").addClass("panel-success");
                actorModal.find(".modal-header").addClass("panel-heading");
            }

            $("#actor-modal-content").text(data).show();
        })
    });


    actorModal.on('hidden.bs.modal', function () {
        actorModal.find(".modal-content").removeClass("panel-danger");
        actorModal.find(".modal-content").addClass("panel-success");
        actorModal.find(".modal-header").removeClass("panel-heading");
        actorModal.find(".fa").show();
        actorModal.find(".modal-footer").hide();
        $("#actor-modal-content").text("").hide();
    });
});