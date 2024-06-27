'use strict';

window.onload = function() {

    $("#button").click(() => register($("#username").val(), $("#password").val()));
};

function register(username, password) {
    $.post("/register", {username: username, password: password}, function (data) {
        // Redirect to login page on successful registration
        window.location.href = "/login";
        console.log(data);
    }).fail(function(response) {
        alert("Register failed: " + response.responseText);
    });
}