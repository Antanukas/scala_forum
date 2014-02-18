var messageId = "#message",
    send_key_code = 13,
    big_number = 4654654654,
    send_message;
$(function() {
    "use strict";
    var socket = $.atmosphere,
        subscription = {
            url: "/chat",
            contentType: "text/plain",
            logLevel: 'debug',
            transport: 'websocket',
            fallbackTransport: 'long-polling'
        };

    subscription.onMessage = function(resp) {
        console.log('got message' + resp.responseBody);
        var chat = $('#chat_window');
        try {
            var json = JSON.parse(resp.responseBody);
        } catch (e)  {
            console.log('This doesn\'t look like a valid JSON: ', resp.responseBody);
            return;
        }
        var div = $('<div class="chat-message"></div>');
        div.append('    <p class="chat-time">' + json.time.split('+')[0] + '</p>');
        div.append('    <p class="chat-user">' + json.user + '</p>');
        div.append('    <p class="chat-message-text">' + escape_with_emoticons(json.message) + '</p>');
        chat.append(div);
        chat.scrollTop(big_number)
    };

    var subSocket = socket.subscribe(subscription);

    send_message = function () {
        console.log('sending msg');
        var msg_elem = $(messageId);
        var msg = msg_elem.val();
        if ($.trim(msg) != "") {
            subSocket.push(msg);
            msg_elem.val("");
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