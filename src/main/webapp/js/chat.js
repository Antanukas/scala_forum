var messageId = "#message",
    send_key_code = 13,
    big_number = 4654654654;
$(function() {
    var socket = $.atmosphere,
        subscription = {
            url: "/chat",
            contentType: "text/plain",
            logLevel: 'debug',
            transport: 'websocket',
            fallbackTransport: 'long-polling'
        };

    subscription.onMessage = function(resp) {
        var chat = $('#chat_window');
        console.log('omg messages...' + resp.responseBody)
        var json = JSON.parse(resp.responseBody)
        chat.append('<p>[' + json.user + ']' + ' ' + json.message + '</p>');
        chat.scrollTop(big_number)
        console.log("got a message" + resp.responseBody)
    };

    subSocket = socket.subscribe(subscription);

    send_message = function () {
        console.log('sending message')
        var msg_elem = $(messageId);
        var msg = msg_elem.val();
        if (msg.trim() != "") {
            $(messageId).val("");
            subSocket.push($(messageId).val());
        }
        return true;
    };
});

function send_message_if_send(event) {
    if (event.keyCode == send_key_code) {
        return send_message && send_message();
    }
};

function reset_message_if_send(event) {
    if (event.keyCode == send_key_code) {
        $(messageId).val("");
    }
};