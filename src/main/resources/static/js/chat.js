'use strict';

window.onload = function() {

    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => updateChatRoom(msg);

    $("#btn-send").click(() => sendPersonal($('#targetUsername').val(), $("#msg").val()));
};

function sendPersonal(targetUsername, msg) {
    var chatArea = document.getElementById('chatArea');
    $(chatArea).append(`<p>${msg}</p>`);

    if (targetUsername === $('#username').val())
        return;

    if (msg !== "" && targetUsername !== "") {
        const textMessage = JSON.stringify({
            strategy: "general",
            targetUsername: targetUsername,
            message: msg
        });

        webSocket.send(textMessage);
        $("#msg").val("");
    }
}

function updateChatRoom(message) {
    // Parse the JSON string to an object
    var data = message.data;
    // console.log(data);
    var messageObj = JSON.parse(data);

    // Assuming messageObj has a property called 'userMessage' that contains the HTML content
    var userMessage = messageObj.userMessage;

    // Assuming there's an element with id 'chat-area' where messages should be displayed
    var chatArea = document.getElementById('chatArea');

    // Append the new message to the chat area
    // .append() is a jQuery method, so ensure jQuery is included in your HTML.
    // If you're not using jQuery, you can use .innerHTML or .insertAdjacentHTML
    console.log(userMessage);
    $(chatArea).append(userMessage);
}

