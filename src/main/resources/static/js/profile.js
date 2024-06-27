'use strict';

window.onload = function() {
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

function chatPage(username) {
    console.log("redirect to: " + username);
    window.location.href='/chat/' + username;
    // $.get("/chat/" + username, function (data) {
    //     console.log("data is " + data);
    // });
}




