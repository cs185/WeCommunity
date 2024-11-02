window.onload = function() {
	webSocket.onclose = () => alert("WebSocket connection closed");
	webSocket.onmessage = (msg) => {
		const data = JSON.parse(msg.data);
		updateChatRoom(data.message, data.header, data.userId);
	};
};

document.addEventListener("DOMContentLoaded", function() {
	letters.forEach(function(letter) {
		const msgContent = letter.letter.content;
		const header = letter.fromUser.id === thisUserId ? thisHeader : letter.fromUser.headerUrl;
		const userId = letter.fromUser.id;
		updateChatRoom(msgContent, header, userId);
	});
});

function updateChatRoom(msgContent, header, userId) {
	const chatArea = document.getElementById('chatArea');
	const li = document.createElement('li');
	li.className = "media mb-3 " + (userId === thisUserId ? "message-right" : "message-left");

	li.innerHTML = `
		<a href="profile.html">
			<img src="${header}" class="mr-3 rounded-circle user-header" alt="用户头像">
		</a>
		<div class="toast show d-lg-block message-content ${userId === thisUserId ? "message-content-right" : "message-content-left"}" role="alert" aria-live="assertive" aria-atomic="true">
			<div class="toast-header">
				<strong class="mr-auto">${userId}</strong>
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
	updateChatRoom(content, thisHeader, thisUserId);

	// Send message to WebSocket server and database
	if (content !== "") {
		const textMessage = JSON.stringify({
			type: "group",
			groupId: group.id,
			userId: thisUserId,
			message: content
		});

		webSocket.send(textMessage);
		console.log("Message sent to group!");
	}
}

function deleteMessage() {
	$(this).parents(".media").remove();
}

function back() {
	location.href = CONTEXT_PATH + "/group/list";
}
