$(document).ready(function() {
    var socket = new SockJS('/stomp');
    var stompClient = Stomp.over(socket);
    stompClient.connect({ }, function(frame) {

        stompClient.subscribe("/topic/refresh", function(data) {
            location.reload(true);
        });

    });
});