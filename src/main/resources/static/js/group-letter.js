window.onload = function() {
	webSocket.onclose = () => alert("WebSocket connection closed");
	webSocket.onmessage = (msg) => {
		const data = JSON.parse(msg.data);
		updateChatRoom(data.content, data.header, data.id, data.senderName);
	};
};

document.addEventListener("DOMContentLoaded", function() {
	letters.forEach(function(letter) {
		const msgContent = letter.letter.content;
		const header = letter.fromUser.headerUrl;
		const userId = letter.fromUser.id;
		const userName = letter.fromUser.username;
		updateChatRoom(msgContent, header, userId, userName);
	});
});

function updateChatRoom(msgContent, header, userId, userName) {
	const chatArea = document.getElementById('chatArea');
	const li = document.createElement('li');
	li.className = "media mb-3 " + (userId === thisUserId ? "message-right" : "message-left");

	li.innerHTML = `
		<a href="profile.html">
			<img src="${header}" class="mr-3 rounded-circle user-header" alt="user header">
		</a>
		<div class="toast show d-lg-block message-content ${userId === thisUserId ? "message-content-right" : "message-content-left"}" role="alert" aria-live="assertive" aria-atomic="true">
			<div class="toast-header">
				<strong class="mr-auto">${userName}</strong>
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

$(function() {
	$("#sendBtn").click(sendMessage);
	$(".close").click(deleteMessage);
});

function sendMessage() {
	const content = $("#message-text").val();
	document.getElementById("message-text").value = '';

	// Display message in chat room immediately
	updateChatRoom(content, thisHeader, thisUserId, thisUsername);

	// Send message to WebSocket server and database
	if (content !== "") {
		const textMessage = JSON.stringify({
			type: 5,
			targetId: group.id,
			message: content
		});

		webSocket.send(textMessage);
		console.log("Message sent to group! " + textMessage);
	}
}

function deleteMessage() {
	$(this).parents(".media").remove();
}

function toSetting() {
	window.location.href = CONTEXT_PATH + "/group/" + group.id + "/setting";
}

function back() {
	location.href = CONTEXT_PATH + "/group/list";
}
