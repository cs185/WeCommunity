window.onload = function() {

	webSocket.onclose = () => alert("WebSocket connection closed");
	webSocket.onmessage = (msg) => updateChatRoom (msg.data, targetHeader, targetUserId);
};

document.addEventListener("DOMContentLoaded", function() {
	letters.forEach(function(letter) {
		var msgContent = letter.letter.content;
		console.log(msgContent);
		var header = letter.fromUser === users.thisUser.id ? thisHeader : targetHeader;
		console.log(header);
		var userId = letter.fromUser === users.thisUser.id ? thisUserId : targetUserId;
		console.log(userId);
		updateChatRoom(msgContent, header, userId);
	});
});

function updateChatRoom(msgContent, header, userId) {

	var chatArea = document.getElementById('chatArea');

	var li = document.createElement('li');
	li.className = "media pb-3 pt-3 mb-2 message-container " + (userId === thisUserId ? "message-right" : "message-left");

	li.innerHTML = `
            <a href="profile.html">
                <img src="${header}" class="mr-4 rounded-circle user-header" alt="user header">
            </a>
            <div class="toast show d-lg-block message-content ${userId === thisUserId ? "message-content-right" : "message-content-left"}" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header">
                    <strong class="mr-auto">${userId}</strong>
<!--                    <small>${new Date().toLocaleString()}</small>-->
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
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	// $("#sendModal").modal("hide");

	var toName = targetUserId;
	var content = $("#message-text").val();

	document.getElementById("message-text").value = '';

	updateChatRoom(content, thisHeader, thisUserId);

	// whenever send, do 2 things: 1. send the message to target user in real time (websocket) 2. save to database (post)
	if (content !== "") {
		const textMessage = JSON.stringify({
			type: 4,
			targetId: targetUserId,
			message: content
		});

		webSocket.send(textMessage);
		console.log("message sent!");
	}

}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}

function back() {
	location.href = CONTEXT_PATH + "/letter/list";
}