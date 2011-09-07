function getTweetCallback(data) {
    if (data) {
        var tweetLine = $("#tweetLine");
        tweetLine.empty();
        for (var i = 0; i < data.length; i++) {
            var tweet = data[i];
            tweetLine.append(buildHtml(tweet));
        }
    }
}

function getTweets() {
    $.getJSON("/t/get", null, getTweetCallback);
}

function newTweetCallback(data) {
    if (data) {
        var tweetLine = $("#tweetLine");
        tweetLine.prepend(buildHtml(data));
        var ntt = $("#newTweetText");
        ntt.empty();
    }
}

function newTweet() {
    var ntt = $("#newTweetText");
    var tweet = $.trim(ntt.val());
    if (tweet) {
        $.post("/t/new", {tweet: tweet}, newTweetCallback, 'json');
    }
}

function searchText() {
    var tts = $("#textToSearch");
    var text = $.trim(tts.val());
    if (text) {
        $.get("/t/search?text=" + text, null, searchTextCallback, 'json');
    }
}

function searchTextCallback(data) {
    if (data) {
        var tweetLine = $("#tweetLine");
        tweetLine.empty();
        for (var i = 0; i < data.length; i++) {
            var tweet = data[i];
            tweetLine.append(buildHtml(tweet));
        }
        var tts = $("#textToSearch");
        tts.empty();
    }
}

function buildHtml(tweet) {
    var html;
    if (tweet.owner.beingFollowed) {
        html = "<span><p>User: " + tweet.owner.username + " - Creation date: " + tweet.creationDate + "</p><p>" + tweet.tweet + "</p></span><hr/>";
    } else {
        html = "<span><p>User: <a href='/t/follow?name=" + tweet.owner.username + "'>" + tweet.owner.username + "</a> - Creation date: " + tweet.creationDate + "</p><p>" + tweet.tweet + "</p></span><hr/>";
    }
    return html
}

function checkForNewTweetsCallback(data) {
    if (data) {
        var tweetLine = $("#tweetLine");
        tweetLine.prepend("<span>You have new tweets to read.</span>");
    }
}

function checkForNewTweets() {
    $.getJSON("/t/shouldrefresh", null, checkForNewTweetsCallback);
}

function ready() {
    $("#newTweetText").keypress(function(e) {
        if (e.which == 13) {
            newTweet();
        }
    });
    $("#textToSearch").keypress(function(e) {
        if (e.which == 13) {
            searchText();
        }
    });
    getTweets();
    setInterval(checkForNewTweets, 5000);
}

