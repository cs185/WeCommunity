'use strict';

const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");

/**
 * Entry point into chat room
 */
window.onload = function() {

    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => updateChatRoom(msg);

    $("#btn-msg").click(() => sendMessage($("#message").val()));
};

/**
 * Send a message to the server.
 * @param msg  The message to send to the server.
 */
function sendMessage(msg) {
    if (msg !== "") {
        console.log(msg);
        webSocket.send(msg);
        $("#message").val("");
    }
}

/**
 * Update the chat room with a message.
 * @param message  The message to update the chat room with.
 */
function updateChatRoom(message) {
    // TODO: convert the data to JSON and use .append(text) on a html element to append the message to the chat area
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
