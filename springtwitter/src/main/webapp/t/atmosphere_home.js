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

function followUserCallback(data) {
    "use strict";
    alert(data);
}

function followUser(id) {
    "use strict";
    $.post("new", {id: id}, followUserCallback, 'json');
}

function webSocketsCallback(response) {
	var i;
	var message = response.responseBody;
	try {
		var data = JSON.parse(message);
	} catch (e) {
		console.log('Error: ', message.data);
		return;
	}
	tweetLine = $("#tweetLine");
	tweetLine.empty();
	for (i = 0; i < data.length; i += 1) {
		data = data[i];
		tweetLine.append(buildHtml(tweet));
	}
}

function initAtmosphere() {
	"use strict";
    var socket = $.atmosphere;
    var request = new $.atmosphere.AtmosphereRequest();
    request.url = 'websockets';
    request.contentType = "application/json";
	request.transport = 'websocket';
	request.fallbackTransport = 'long-polling';
	request.onMessage = webSocketsCallback;
	request.onReconnect = function (request, response) {
		socket.info("Reconnecting")
	};
	var subSocket = socket.subscribe(request);
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
    initAtmosphere();
}

