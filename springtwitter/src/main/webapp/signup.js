function registerCallback(data) {
    "use strict";
    window.alert(data);
}

function register() {
    "use strict";
    var usern = $("#j_username").val();
    var passw = $("#j_password").val();
    jQuery.post("register", { username: usern, password: passw }, registerCallback, 'json');
}

function checkUserCallback(data) {
    "use strict";
    if (data) {
        jQuery("#checkname").text("User already exists with that name");
    } else {
        jQuery("#checkname").text("Name is free to use.");
    }
}

function checkUser() {
    "use strict";
    var usern = $("#j_username").val();
    jQuery.getJSON("exists?name=" + usern, null, checkUserCallback);
}

function ready() {
    "use strict";
    jQuery("#register").click(register);
    jQuery("#j_username").blur(checkUser);
}