package com.self.tweetstream;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class TweetStreamClient {
    public static CountDownLatch latch;
    public static String response;

    @OnOpen
    public void onOpen(Session session) {
        try {
            session.getBasicRemote().sendText("Hello World!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void printTweets(final String tweet) {
        System.out.println("Tweet:" + tweet);
        response = tweet;
        latch.countDown();
    }
}
