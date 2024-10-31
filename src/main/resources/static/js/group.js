const webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp");

window.onload = function() {
    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => {
        var messageData = JSON.parse(msg.data);
        var member = members.find(m => m.id === messageData.userId);
        updateChatRoom(messageData.message, member.headerUrl, member.username);
    };
};

document.addEventListener("DOMContentLoaded", function() {
    // 初始化聊天记录或任何其他数据
});

function updateChatRoom(msgContent, header, username) {
    var chatArea = document.getElementById('chatArea');
    var li = document.createElement('li');
    li.className = "media pb-3 pt-3 mb-2 message-container " + (username === thisUserId ? "message-right" : "message-left");

    li.innerHTML = `
                <a href="profile.html">
                    <img src="${header}" class="mr-4 rounded-circle user-header" alt="用户头像">
                </a>
                <div class="toast show d-lg-block message-content ${username === thisUserId ? "message-content-right" : "message-content-left"}" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="toast-header">
                        <strong class="mr-auto">${username}</strong>
                        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="toast-body">
                        ${msgContent}
                    </div>
                </div>
            `;
    chatArea.appendChild(li);
}

$(function(){
    $("#sendBtn").click(sendMessage);
});

function sendMessage() {
    var content = $("#message-text").val();
    document.getElementById("message-text").value = '';

    updateChatRoom(content, thisHeader, thisUserId);

    // 发送消息到服务器
    if (content !== "") {
        const textMessage = JSON.stringify({
            type: "group",
            message: content,
            userId: thisUserId
        });

        webSocket.send(textMessage);
    }
}

function back() {
    location.href = "/group/list";
}

function getUserInfoById(userId) {
    return members.find(member => member.id === userId);
}

// todo: finish this
// function leaveGroup() {
//     // 请求后端处理退出群组逻辑
//     $.post(
//         "/group/leave",
//         {"groupId":/*[[${groupId}]]*/},
//         function (data) {
//             data = $.parseJSON(data);
//             if (data.code == 0) {
//                 alert("Left the group");
//                 back();
//             } else {
//                 alert("Failed to leave the group: " + data.msg);
//             }
//         }
//     );
// }