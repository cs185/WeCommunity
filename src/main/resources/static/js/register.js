

$(function(){
    $("form").submit(check_data);
    $("input").focus(clear_error);
});

function check_data() {
    var pwd1 = $("#password").val();
    var pwd2 = $("#confirm-password").val();
    if(pwd1 != pwd2) {
        $("#confirm-password").addClass("is-invalid");
        return false;
    }
    return true;
}

function clear_error() {
    $(this).removeClass("is-invalid");
}


// 'use strict';
//
// window.onload = function() {
//
//     $("#button").click(() => register($("#username").val(), $("#password").val()));
// };
//
// function register(username, password) {
//     $.post("/register", {username: username, password: password}, function (data) {
//         // Redirect to login page on successful registration
//         window.location.href = "/login";
//         console.log(data);
//     }).fail(function(response) {
//         alert("Register failed: " + response.responseText);
//     });
// }