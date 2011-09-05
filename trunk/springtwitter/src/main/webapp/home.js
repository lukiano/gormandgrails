function getTweetCallback(data) {
    if (data) {
        $("#checkname").text("User already exists with that name");
    } else {
        $("#checkname").text("Name is free to use.");
    }
}

function getTweets() {
    $.getJSON("/t/get", null, getTweetCallback);
}

function ready() {
    getTweets();
}