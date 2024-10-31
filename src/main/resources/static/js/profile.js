'use strict';

window.onload = function() {
    $(".follow-btn").click(follow);

    $("#btn-chat").click(function() {
        chatPage($(this).val());
    });

    $("#btn-friendship").click(function() {
        var buttonText = $(this).text();
        var username = $("#username").val();

        var url;
        switch (buttonText) {
            case 'Add Friend':
                url = '/friend/add';
                break;
            case 'Accept Request':
                url = '/friend/accept';
                break;
            case 'Delete Friend':
                url = '/friend/delete';
                break;
            default:
                return;
        }

        $.post(url, { username: username })
            .done(function(data) {
                alert(data);
                location.reload();
            })
            .fail(function(response) {
                alert("Operation failed: " + response.responseText);
            });
    });
};

function follow() {
    var btn = this;
    if($(btn).hasClass("btn-info")) {
        // 关注TA
        $.post(
            CONTEXT_PATH + "/follow",
            {"entityType":3,"entityId":$(btn).prev().val()},
            function(data) {
                data = $.parseJSON(data);
                if(data.code == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
        // $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
    } else {
        // 取消关注
        $.post(
            CONTEXT_PATH + "/follow",
            {"entityType":3,"entityId":$(btn).prev().val()},
            function(data) {
                data = $.parseJSON(data);
                if(data.code == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
        //$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
    }
}

function chatPage(username) {
    console.log("redirect to: " + username);
    window.location.href='/chat/' + username;
    // $.get("/chat/" + username, function (data) {
    //     console.log("data is " + data);
    // });
}




