function webSocketsCallback(response) {
	var i, tweet;
	var message = response.responseBody;
	try {
		var data = JSON.parse(message);
	} catch (e) {
		console.log('Error: ', message.data);
		return;
	}
	tweetLine = $("#tweetLine");
	for (i = 0; i < data.length; i++) {
		tweet = data[i];
		tweetLine.prepend(buildHtml(tweet));
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
    getTweets(initAtmosphere);
}

