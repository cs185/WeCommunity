$(function(){
	$(".leave-btn").click(leave);
	$("#btn-create").click(create);
	$("#btn-join").click(join);
});

function create() {
	$.post(
		CONTEXT_PATH + "/group/create",
		{"groupName":$("#create-name").val()},
		function(data) {
			data = $.parseJSON(data);
			if(data.code == 0) {
				location.href = CONTEXT_PATH + "/group/" + data.msg;
			} else {
				alert(data.msg);
			}
		}
	);
}

function join() {
	$.post(
		CONTEXT_PATH + "/group/join",
		{"groupId":$("#join-id").val()},
		function(data) {
			data = $.parseJSON(data);
			if(data.code == 0) {
				alert("Join request sent: " + data.msg);
			} else {
				alert("Join failed: " + data.msg);
			}
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
		    CONTEXT_PATH + "/group/delete",
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