package com.self.tweetstream;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twitter.hbc.core.Client;

@ServerEndpoint("/tweets")
public class TweetStreamServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TweetStreamServer.class);

    @OnMessage
    public void tweets(final String message, final Session session) throws IOException, InterruptedException {
        System.out.println("session id:" + session.getId() + ", search term: " + message);
        final Client twitterClient = TwitterHoseBird.getInstance(message);
        while (!session.getOpenSessions().isEmpty()) {
            for (final Session s : session.getOpenSessions()) {
                if (twitterClient.isDone()) {
                    System.out.println("Twitter Client Done, waiting ...");
                }
                s.getBasicRemote().sendText(TwitterHoseBird.getMsgQueue().take());
            }
        }
    }
/*
    @OnMessage
    public void tweets(final String message, final Session client) throws IOException, InterruptedException {
        System.out.println("session id:" + client.getId() + ", search term: " + message);
        final TwitterHoseBird twitterHoseBird = new TwitterHoseBird();
        final Client twitterClient = twitterHoseBird.getInstance(message);
        while (!twitterClient.isDone()) {
            client.getBasicRemote().sendText(twitterHoseBird.getMsgQueue().take());
        }
    }
*/

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        LOGGER.warn("closing session: {}, remaining session: {}", session.getId(), session.getOpenSessions().size());
    }
}
