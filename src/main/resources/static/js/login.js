'use strict';

window.onload = function() {

    $("#login-button").click(() => login($("#username").val(), $("#password").val()));
    $("#register-button").click(() => register());
};

function login(username, password) {
    console.log("login");
    $.post("/login", {username: username, password: password}, function (data) {
        console.log("data is " + data);
        window.location.href = "/index";
    }).fail(function(response) {
        alert("Registration failed: " + response.responseText);
    });
}

function register() {
    console.log("register");
    window.location.href = "/register";
}