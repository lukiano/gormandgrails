function getTweetCallback(data) {
    if (data) {
        var tweetLine = $("#tweetLine");
        tweetLine.empty();
        for (var i = 0; i < data.tweet.length; i++) {
            var tweet = data.tweet[i];
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
        tweetLine.prepend(buildHtml(tweet));
    }
}

function newTweet() {
    var tweet = $.trim($("#newTweetText").val());
    if (tweet) {
        $.post("/t/new", {tweet: tweet}, newTweetCallback, 'json');
    }
}

function searchText() {
    var tweet = $.trim($("#newTweetText").val());
    if (tweet) {
        $.post("/t/search", {tweet: tweet}, searchTextCallback, 'json');
    }
}

function searchTextCallback(data) {
    if (data) {
        var tweetLine = $("#tweetLine");
        tweetLine.empty();
        for (var i = 0; i < data.tweet.length; i++) {
            var tweet = data.tweet[i];
            tweetLine.append(buildHtml(tweet));
        }
    }
}

function buildHtml(tweet) {
    var html;
    if (tweet.owner.beingFollowed) {
        html = "<span><p>User: " + tweet.owner.username + " - Creation date: " + tweet.creationDate + "</p><p>" + tweet.tweet + "</p></span>";
    } else {
        html = "<span><p>User: <a href='/t/follow?name=" + tweet.owner.username + "'>" + tweet.owner.username + "</a> - Creation date: " + tweet.creationDate + "</p><p>" + tweet.tweet + "</p></span>";
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
    $(document).everyTime(1000, function(i) {
        processChunk(i);
    }, times);
}

