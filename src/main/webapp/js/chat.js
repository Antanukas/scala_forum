var subSocket;
$(function() {
    var socket = $.atmosphere;

    var subscription = {
        url: "/chat",
        contentType: "text/plain",
        logLevel: 'debug',
        transport: 'websocket',
        fallbackTransport: 'long-polling'
    };

    subscription.onMessage = function(resp) {
        console.log("got a message" + resp.responseBody)
    };

    subSocket = socket.subscribe(subscription);
});