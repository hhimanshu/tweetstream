package com.self.tweetstream;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/tweets")
public class TweetStreamServer {
    @OnMessage
    public String tweets(final String message) {
        int count = 0;
        String returnString = "response - ";
        while (count < 10000) {
            returnString = returnString + count++;
        }
        return "Tweets!";
    }
}
