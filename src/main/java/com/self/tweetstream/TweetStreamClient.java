package com.self.tweetstream;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;

@ClientEndpoint
public class TweetStreamClient {
    @OnMessage
    public void printTweets(final String tweet) {
        System.out.println(tweet);
    }
}
