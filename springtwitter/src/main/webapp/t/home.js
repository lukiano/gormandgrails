function buildHtml(tweet) {
    "use strict";
    var html;
    if (tweet.owner.beingFollowed) {
        html = "<span><p>User: " + tweet.owner.username + " - Creation date: " + tweet.creationDate + "</p><p>" + tweet.tweet + "</p></span><hr/>";
    } else {
        html = "<span><p>User: <a href='follow?name=" + tweet.owner.username + "'>" + tweet.owner.username + "</a> - Creation date: " + tweet.creationDate + "</p><p>" + tweet.tweet + "</p></span><hr/>";
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
        var tweetLine = $("#tweetLine");
        tweetLine.prepend("<span>You have new tweets to read.</span>");
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
    getTweets();
    setInterval(checkForNewTweets, 5000);
}

