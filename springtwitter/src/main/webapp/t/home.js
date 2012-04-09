function buildHtml(tweet) {
    "use strict";
    var html, date, dateString;
    date = new Date(tweet.creationDate);
    dateString = date.getFullYear() + '-' + date.getMonth() + '-' + date.getDay() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
    if (tweet.owner.canFollow) {
        html = "<span><p>User: " + tweet.owner.username + " - Creation date: " + dateString + "</p><p>" + tweet.tweet + "</p></span><hr/>";
    } else {
        html = "<span><p>User: <a href='follow?id=" + tweet.owner.id + "'>" + tweet.owner.username + "</a> - Creation date: " + dateString + "</p><p>" + tweet.tweet + "</p></span><hr/>";
    }
    return html;
}

function searchTextCallback(data) {
    "use strict";
    var tweetLine, tweet, tts, i;
    if (data) {
        tweetLine = $("#tweetLine");
        tweetLine.empty();
        for (i = 0; i < data.length; i += 1) {
            tweet = data[i];
            tweetLine.append(buildHtml(tweet));
        }
        tts = $("#textToSearch");
        tts.empty();
    }
}

function searchText() {
    "use strict";
    var tts, text;
    tts = $("#textToSearch");
    text = $.trim(tts.val());
    if (text) {
        $.get("search?text=" + text, null, searchTextCallback, 'json');
    }
}

function newTweetCallback(data) {
    "use strict";
    var tweetLine, ntt;
    if (data) {
        tweetLine = $("#tweetLine");
        tweetLine.prepend(buildHtml(data));
        ntt = $("#newTweetText");
        ntt.empty();
    }
}

function newTweet() {
    "use strict";
    var ntt, tweet;
    ntt = $("#newTweetText");
    tweet = $.trim(ntt.val());
    if (tweet) {
        $.post("new", {tweet: tweet}, newTweetCallback, 'json');
    }
}

function checkForNewTweetsCallback(data) {
    "use strict";
    if (data) {
        $("#unreadtweets").show();
    }
}

function checkForNewTweets() {
    "use strict";
    $.getJSON("shouldrefresh", null, checkForNewTweetsCallback);
}

function getTweetCallback(data) {
    "use strict";
    var tweetLine, tweet, i;
    if (data) {
        tweetLine = $("#tweetLine");
        tweetLine.empty();
        for (i = 0; i < data.length; i += 1) {
            tweet = data[i];
            tweetLine.append(buildHtml(tweet));
        }
    }
}

function getTweets() {
    "use strict";
    $.getJSON("get", null, getTweetCallback);
}

function ready() {
    "use strict";
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
    getTweets();
    setInterval(checkForNewTweets, 10000);
}

