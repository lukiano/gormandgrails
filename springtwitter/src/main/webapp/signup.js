function registerCallback(data) {
    alert("Data Loaded: " + data);
}

function register() {
    var usern = $("#j_username").val();
    var passw = $("#j_password").val();
    jQuery.post("register", { username:usern, password:passw }, registerCallback, 'json');
}

function checkUserCallback(data) {
    jQuery("#checkname").text(data);
}

function checkUser() {
    var usern = $("#j_username").val();
    jQuery.getJSON("exists?name=" + usern, null, checkUserCallback);
}

function ready() {
    jQuery("#register").click(register);
    jQuery("#j_username").blur(checkUser);
}