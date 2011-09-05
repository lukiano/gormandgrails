function registerCallback(data) {
    alert("Data Loaded: " + data);
}

function register() {
    var usern = $("#j_username").val();
    var passw = $("#j_password").val();
    $.post("/register", { username:usern, password:passw }, registerCallback, 'json');
}

function checkUserCallback(data) {
    if (data) {
        $("#checkname").text("User already exists with that name");
    } else {
        $("#checkname").text("Name is free to use.");
    }
}

function checkUser() {
    var usern = $("#j_username").val();
    $.getJSON("/exists?name=" + usern, null, checkUserCallback);
}

function ready() {
    $("#register").click(register);
    $("#j_username").blur(checkUser);
}