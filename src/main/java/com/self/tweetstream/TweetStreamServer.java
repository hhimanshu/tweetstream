package com.self.tweetstream;

import java.io.IOException;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/tweets")
public class TweetStreamServer {
    @OnMessage
    public void tweets(final String message, Session client) throws IOException, InterruptedException {
        int i = 0;
        for (Session peer : client.getOpenSessions()) {
            while (i < 10) {
                System.out.println("sending ...");
                peer.getBasicRemote().sendText("Hello");
                Thread.sleep(2000);
                i++;
            }
        }
    }
}
