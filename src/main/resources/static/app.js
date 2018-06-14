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
        	showReport(report.body);
        });
        stompClient.subscribe('/topic/robot', function (robot) {
        	showRobot(JSON.parse(robot.body).robotCoordinate);
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

function toggleConnection(tryToConnect) {
	if (tryToConnect) {
		connect();
	} else  {
		disconnect();
	}
}

function sendPolyline() {
    stompClient.send("/app/polyline", {}, JSON.stringify({'encodedPolyline': $("#polyline").val()}));
}

function showReport(message) {
	var report = JSON.parse(message);
	var alertClass = "";
	switch(report.level) {
    case "Good":
    	alertClass = "alert-success";
        break;
    case "Moderate":
    	alertClass = "alert-info";
        break;
    case "USG":
    	alertClass = "alert-warning";
        break;
    case "Unhealthy":
    	alertClass = "alert-danger";
        break;
    default:
    	alertClass = "alert-success";
	}
    $("#alert_container").append("<div class='" + alertClass + "'>" + message + "</span>");
}

function showRobot(message) {
    $("#lat").html(message.lat);
    $("#lng").html(message.lng);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendPolyline(); });
    $( '#connectToggle' ).bootstrapToggle({
        on: 'Connected',
        off: 'Disconnected'
      });
    $( '#connectToggle' ).change(function() { toggleConnection(this.checked); });
    
});