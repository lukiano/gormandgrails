function buildHtml(tweet) {
    "use strict";
    var html, date, dateString;
    date = new Date(tweet.creationDate);
    dateString = date.getFullYear() + '-' + date.getMonth() + '-'
        + date.getDay() + ' ' + date.getHours() + ':'
        + date.getMinutes() + ':' + date.getSeconds();
    if (tweet.owner.canFollow) {
        html = "<span><p>" + userString + ": <a href='#' onclick='followUser("
            + tweet.owner.id + ");return false;'>" + tweet.owner.username
            + "</a> - " + creationDateString + ": " + dateString + "</p><p>" + tweet.tweet
            + "</p></span><hr/>";
    } else {
        html = "<span><p>" + userString + ": " + tweet.owner.username + " - Creation date: "
            + dateString + "</p><p>" + tweet.tweet + "</p></span><hr/>";
    }
    return html;
}

function searchTextCallback(data) {
    "use strict";
    var tweetLine, tweet, tts, i;
    if (data) {
        tweetLine = $("#searchLine");
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
    //commented. The new tweet will come from getTweets.
    /*
    var tweetLine, ntt;
    if (data) {
        tweetLine = $("#tweetLine");
        tweetLine.prepend(buildHtml(data));
        ntt = $("#newTweetText");
        ntt.empty();
    }
    */
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

function getTweetCallback(data, postFunction) {
    "use strict";
    var tweetLine, tweet, i, lastTimestamp = null;
    if (data) {
        tweetLine = $("#tweetLine");
        tweetLine.empty();
        for (i = 0; i < data.length; i++) {
            tweet = data[i];
        	if (i == 0) {
        		lastTimesamp = tweet.creationDate;
        	}
            tweetLine.append(buildHtml(tweet));
        }
    }
    postFunction(lastTimestamp);
}

function getTweets(postFunction, from) {
    "use strict";
    var r = null;
    if (from) {
    	r = {from: from};
    }
    $.getJSON("get", r, function(data) { getTweetCallback(data, postFunction); });
}


function followUserCallback(data) {
    "use strict";
    alert(data);
}

function followUser(id) {
    "use strict";
    $.post("new", {id: id}, followUserCallback, 'json');
}

