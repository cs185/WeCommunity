'use strict';
/**
 * Entry point into chat room
 */
window.onload = function() {

    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => updateChatRoom(msg);

    $("#btn-leave").click(() => leave($("#btn-leave").val()));

    let msgType = $("#msg-general").val();

    function handleButtonClick() {
        msgType = $(this).val();
        console.log('message type button value:', msgType);
    }

    $('#msg-anonymous').click(handleButtonClick);
    $('#msg-general').click(handleButtonClick);
    $('#msg-emphasis').click(handleButtonClick);
    $('#msg-important').click(handleButtonClick);

    $("#btn-msg").click(() => sendMessage(msgType, $("#message").val()));

};

function leave(groupId) {
    console.log("leave");
    $.post("/group/leave", {groupId: groupId}, function (data) {
        console.log("data is " + data);
        window.location.href = "/index";
    });
}

// function changeType(messageType) {
//         console.log(messageType);
//         return messageType;
//         // $.post("/group/type", {messageType: messageType}, function (data) {
//         //     console.log("data is " + data);
//         //     // window.location.href = "/group/" + groupId;
//         // }).fail(function(response) {
//         //     alert("Message type changing failed: " + response.responseText);
//         // })
// }
function sendMessage(strategyType, msg) {
    if (msg !== "") {
        const textMessage = JSON.stringify({
            strategy: strategyType,
            targetUserId: null,
            message: msg
        });
        webSocket.send(textMessage);
        $("#message").val("");
    }
}

/**
 * Update the chat room with a message.
 * @param message  The message to update the chat room with.
 */
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

