package com.self.tweetstream;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/tweets")
public class TweetStreamServer {
    @OnMessage
    public String tweets(final String message) {
        return "Tweets!";
    }
}
