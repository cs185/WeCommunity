// JavaScript function to update the file name in the label
function updateFileName() {
	const input = document.getElementById('post-image'); // Get the file input element
	const label = document.getElementById('post-image-box'); // Get the label element
	const fileName = input.files[0]?.name || "Choose an Image File"; // Get the file name or fallback to default
	label.textContent = fileName; // Update the label text
}

$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// 构建 FormData 对象
	var formData = new FormData($("#publishForm")[0]);

	// 发送异步请求 (POST)
	$.ajax({
		url: CONTEXT_PATH + "/discuss/add",
		type: "POST",
		data: formData,
		processData: false, // 必须关闭，否则 jQuery 会尝试将 FormData 转换为字符串
		contentType: false, // 必须设置为 false，否则 jQuery 默认使用 application/x-www-form-urlencoded
		success: function(data) {
			data = $.parseJSON(data);
			// 在提示框中显示返回消息
			$("#hintBody").text(data.msg);
			// 显示提示框
			$("#hintModal").modal("show");
			// 2秒后, 自动隐藏提示框
			setTimeout(function() {
				$("#hintModal").modal("hide");
				// 刷新页面
				if (data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		},
		error: function(xhr, status, error) {
			console.error("Upload failed: " + error);
		}
	});
}
