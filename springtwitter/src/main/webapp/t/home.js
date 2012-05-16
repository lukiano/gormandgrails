var lastUpdate;

function emptyPostFunction() {}

function checkForNewTweetsCallback(data) {
    "use strict";
    if (data) {
    	getTweets(emptyPostFunction, lastUpdate);
        $("#unreadtweets").show();
    }
}

function checkForNewTweets() {
    "use strict";
    $.getJSON("shouldrefresh", null, checkForNewTweetsCallback);
}

function initTimerAndUpdateTimestamp(lastTimestamp) {
	lastUpdate = lastTimestamp;
    var refreshIntervalId = window.setInterval(checkForNewTweets, 10000);
}

function ready() {
    "use strict";
    $("#tabs").tabs();
    $("#newTweetText").keypress(function (e) {
        if (e.which === 13) {
            newTweet();
        }
    });
    $("#textToSearch").keypress(function (e) {
        if (e.which === 13) {
            searchText();
        }
    });
    $("#inputBox").ajaxError(function (e, jqxhr, settings, exception) {
        $(this).text("Triggered ajaxError handler.");
    });
    getTweets(initTimerAndUpdateTimestamp);
}

