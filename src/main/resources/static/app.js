var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/reports', function (report) {
            //showReport(JSON.parse(report.body).content);
        	showReport(report.body);
        });
        stompClient.subscribe('/topic/robot', function (robot) {
            //showReport(JSON.parse(report.body).content);
        	showRobot(robot.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendPolyline() {
    stompClient.send("/app/polyline", {}, JSON.stringify({'encodedPolyline': $("#polyline").val()}));
}

function showReport(message) {
    $("#reports").append("<tr><td>" + message + "</td></tr>");
}

function showRobot(message) {
    $("#robot").html("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendPolyline(); });
});