var send_message = function () {};
var messageId = "#message"
var send_key_code = 13;
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
        var chat = $('#chat_window');
        chat.val(chat.val() + '\n' + resp.responseBody);
        console.log("got a message" + resp.responseBody)
    };

    subSocket = socket.subscribe(subscription);

    send_message = function () {
        var msg_elem = $(messageId);
        var msg = msg_elem.val();
        if (msg.trim() != "") {
            subSocket.push($(messageId).val());
        }
        return true;
    };
});

function send_message_if_send(event) {
    if (event.keyCode == send_key_code) {
        return send_message();
    }
};

function reset_message_if_send(event) {
    if (event.keyCode == send_key_code) {
        $(messageId).val("");
    }
};