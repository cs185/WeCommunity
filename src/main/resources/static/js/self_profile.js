'use strict';

window.onload = function() {
    $("#btn-friend-profile").click(function() {
        friendProfile($(this).val());
    });
    $("#btn-group").click(function() {
        groupPage($(this).val());
    });
    $("#btn-search").click(() => searchUser($("#user-name").val()));
    $("#btn-join").click(() => joinGroup($("#group-id").val()));
    $("#btn-create").click(() => createGroup($("#group-name").val()));
};

function groupPage(groupId) {
    console.log("redirect to: " + groupId);
    window.location.href='/group/' + groupId;
    // $.get("/group/" + groupId, function (data) {
    //     console.log("data is " + data);
    // });
}

function friendProfile(username) {
    console.log("redirect to: " + username);
    window.location.href='/profile/' + username;
    // $.get("/profile/" + username, function (data) {
    //     console.log("data is " + data);
    // });
}

function searchUser(username) {
    console.log("search user: " + username);
    $.get("/profile/search", {username: username})
        // .done(function(data, textStatus, jqXHR) {
        //     $('#message').text(data).css('color', 'red');
        //     // if (jqXHR.status === 200) {
        //     //
        //     //     $('#message').text(data).css('color', 'red');
        //     // } else if (jqXHR.status === 302) {
        //     //     window.location.href = data;
        //     // }
        // })
        .fail(function(jqXHR) {
            if (jqXHR.status === 404) {
                $('#message').text(jqXHR.responseText).css('color', 'red');
            } else {
                $('#message').text("An unexpected error occurred.").css('color', 'red');
            }
        });
}


function joinGroup(groupId) {
    console.log("join");
    $.post("/profile/join", {groupId: groupId}, function (data) {
        console.log("data is " + data);
        window.location.href = "/group/" + groupId;
    }).fail(function(response) {
        alert("Join failed: " + response.responseText);
    });
}

function createGroup(groupName) {
    console.log("create");
    $.post("/profile/create", {groupName: groupName}, function (data) {
        console.log("data is " + data);
        window.location.href = "/group/" + data;
    }).fail(function(response) {
        alert("Create failed: " + response.responseText);
    });
}


