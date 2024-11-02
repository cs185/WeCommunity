$(function(){
	$(".leave-btn").click(leave);
	$("#btn-create").click(create);
});

function create() {
	$.post(
		CONTEXT_PATH + "/group/create",
		{"groupName":$("#group-name").val()},
		function(data) {
			data = $.parseJSON(data);
			console.log(data);
		}
	);
}
function leave() {
	var btn = this;
	if($(btn).hasClass("btn-info")) {
		// leave group
		$.post(
		    CONTEXT_PATH + "/group/leave",
		    {"groupId":$("#entityId").val()},
		    function(data) {
		        data = $.parseJSON(data);
		        if(data.code == 0) {
                    window.location.reload();
		        } else {
                    alert(data.msg);
		        }
		    }
		);
	} else {
		// delete group
		$.post(
		    CONTEXT_PATH + "/unfollow",
			{"groupId":$("#entityId").val()},
		    function(data) {
		        data = $.parseJSON(data);
		        if(data.code == 0) {
                    window.location.reload();
		        } else {
                    alert(data.msg);
		        }
		    }
		);
	}
}

function back() {
	location.href = CONTEXT_PATH + "/group/list";
}